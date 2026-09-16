package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.otp

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.UnlockMethod
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock.SendUnlockEmailConfirmationUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock.SendUnlockPhoneConfirmationUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock.UnlockByEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock.UnlockByPhoneUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/** Default implementation of [UnlockOtpComponent]. */
class UnlockOtpComponentImpl(
    componentContext: ComponentContext,
    method: UnlockMethod,
    target: String,
    private val unlockByEmailUseCase: UnlockByEmailUseCase,
    private val unlockByPhoneUseCase: UnlockByPhoneUseCase,
    private val sendUnlockEmailConfirmationUseCase: SendUnlockEmailConfirmationUseCase,
    private val sendUnlockPhoneConfirmationUseCase: SendUnlockPhoneConfirmationUseCase,
    private val onUnlockSuccess: () -> Unit,
    private val onBack: () -> Unit,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
) : UnlockOtpComponent, ComponentContext by componentContext {

    private val _state = MutableValue(
        UnlockOtpScreenState(method = method, target = target)
    )
    override val state: Value<UnlockOtpScreenState> = _state

    override fun onCodeChanged(code: String) {
        _state.update { it.copy(codeInput = code, actionError = null) }
    }

    override fun onUnlockClick() {
        val currentState = _state.value
        val code = currentState.codeInput.trim()
        if (code.isBlank()) return

        _state.update { it.copy(actionLoading = true, actionError = null) }
        coroutineScope.launch {
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
        _state.update { it.copy(actionLoading = true, actionError = null) }
        coroutineScope.launch {
            val result = when (currentState.method) {
                UnlockMethod.EMAIL -> sendUnlockEmailConfirmationUseCase.execute(currentState.target)
                UnlockMethod.PHONE -> sendUnlockPhoneConfirmationUseCase.execute(currentState.target)
            }
            when (result) {
                is AppResult.Success -> {
                    _state.update { currentStateCopy -> currentStateCopy.copy(actionLoading = false) }
                }
                is AppResult.Error -> {
                    _state.update { currentStateCopy -> currentStateCopy.copy(actionLoading = false, actionError = result.error) }
                }
            }
        }
    }

    override fun onBackClick() {
        onBack()
    }
}
