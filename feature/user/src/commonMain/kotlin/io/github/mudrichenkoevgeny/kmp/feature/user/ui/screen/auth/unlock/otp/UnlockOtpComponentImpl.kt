package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.otp

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.time.resendCountdown
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.UnlockMethod
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock.SendUnlockEmailConfirmationUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock.SendUnlockPhoneConfirmationUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock.UnlockByEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock.UnlockByPhoneUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/** Default implementation of [UnlockOtpComponent]. */
class UnlockOtpComponentImpl(
    componentContext: ComponentContext,
    method: UnlockMethod,
    target: String,
    initialDelaySeconds: Int = 0,
    private val unlockByEmailUseCase: UnlockByEmailUseCase,
    private val unlockByPhoneUseCase: UnlockByPhoneUseCase,
    private val sendUnlockEmailConfirmationUseCase: SendUnlockEmailConfirmationUseCase,
    private val sendUnlockPhoneConfirmationUseCase: SendUnlockPhoneConfirmationUseCase,
    private val onUnlockSuccess: () -> Unit,
    private val onBack: () -> Unit
) : UnlockOtpComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()
    private var timerJob: Job? = null

    private val _state = MutableValue(
        UnlockOtpScreenState(
            method = method,
            target = target,
            remainingDelaySeconds = initialDelaySeconds
        )
    )
    override val state: Value<UnlockOtpScreenState> = _state

    init {
        if (initialDelaySeconds > 0) {
            startTimer(initialDelaySeconds)
        }
    }

    override fun onCodeChanged(code: String) {
        _state.update { it.copy(codeInput = code, actionError = null) }
    }

    override fun onUnlockClick() {
        val currentState = _state.value
        val code = currentState.codeInput.trim()
        if (code.isBlank()) return

        _state.update { it.copy(actionLoading = true, actionError = null) }
        scope.launch {
            val result = when (currentState.method) {
                UnlockMethod.EMAIL -> unlockByEmailUseCase.execute(currentState.target, code)
                UnlockMethod.PHONE -> unlockByPhoneUseCase.execute(currentState.target, code)
            }
            when (result) {
                is AppResult.Success -> {
                    _state.update { currentStateCopy -> currentStateCopy.copy(actionLoading = false) }
                    onUnlockSuccess()
                }
                is AppResult.Error -> {
                    _state.update { currentStateCopy -> currentStateCopy.copy(actionLoading = false, actionError = result.error) }
                }
            }
        }
    }

    override fun onResendCodeClick() {
        val currentState = _state.value
        if (currentState.remainingDelaySeconds > 0) return

        _state.update { it.copy(actionLoading = true, actionError = null) }
        scope.launch {
            val result = when (currentState.method) {
                UnlockMethod.EMAIL -> sendUnlockEmailConfirmationUseCase.execute(currentState.target)
                UnlockMethod.PHONE -> sendUnlockPhoneConfirmationUseCase.execute(currentState.target)
            }
            when (result) {
                is AppResult.Success -> {
                    _state.update { currentStateCopy -> currentStateCopy.copy(actionLoading = false) }
                    startTimer(result.data.retryAfterSeconds)
                }
                is AppResult.Error -> {
                    val error = result.error
                    _state.update { currentStateCopy -> currentStateCopy.copy(actionLoading = false, actionError = error) }
                    if (error is UserError.TooManyConfirmationRequests) {
                        startTimer(error.retryAfterSeconds)
                    }
                }
            }
        }
    }

    override fun onBackClick() {
        timerJob?.cancel()
        onBack()
    }

    private fun startTimer(seconds: Int) {
        timerJob?.cancel()
        if (seconds <= 0) return

        _state.update { it.copy(remainingDelaySeconds = seconds) }
        timerJob = scope.launch {
            resendCountdown(
                totalSeconds = seconds,
                onTick = { remaining ->
                    _state.update { it.copy(remainingDelaySeconds = remaining) }
                }
            )
        }
    }
}
