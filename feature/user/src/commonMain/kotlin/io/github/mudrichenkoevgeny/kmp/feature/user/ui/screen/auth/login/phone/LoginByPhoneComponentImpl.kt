package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.phone

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.result.onError
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.login.LoginRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.LoginByPhoneUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.SendLoginConfirmationToPhoneUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.utils.FieldValidator
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Default [LoginByPhoneComponent]: send-code, cooldown handling, and login-by-phone confirmation.
 *
 * @param componentContext Decompose [ComponentContext].
 * @param loginRepository reads remaining confirmation delay for a phone number.
 * @param sendLoginConfirmationToPhoneUseCase requests an SMS code.
 * @param loginByPhoneUseCase completes login with phone and code.
 * @param onBack pops this flow on the parent stack when appropriate.
 * @param onFinished invoked when login succeeds.
 */
class LoginByPhoneComponentImpl(
    componentContext: ComponentContext,
    private val loginRepository: LoginRepository,
    private val sendLoginConfirmationToPhoneUseCase: SendLoginConfirmationToPhoneUseCase,
    private val loginByPhoneUseCase: LoginByPhoneUseCase,
    private val onBack: () -> Unit,
    private val onFinished: () -> Unit
) : LoginByPhoneComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()
    private var timerJob: Job? = null

    private val _state = MutableValue<LoginByPhoneScreenState>(
        LoginByPhoneScreenState.PhoneInput()
    )
    override val state: Value<LoginByPhoneScreenState> = _state

    override fun onPhoneChanged(phone: String) {
        val current = _state.value as? LoginByPhoneScreenState.PhoneInput ?: return
        val isValid = FieldValidator.isPhoneNumberValid(phone)

        _state.value = current.copy(
            phoneNumber = phone,
            isPhoneNumberValid = isValid,
            actionError = null
        )

        if (isValid) {
            val remaining = loginRepository.getRemainingLoginConfirmationDelayInSeconds(phone)
            if (remaining > 0) {
                moveToCodeInput(phone, remaining)
            }
        }
    }

    override fun onCodeChanged(code: String) {
        // todo wait for api
        val current = _state.value as? LoginByPhoneScreenState.CodeInput ?: return

        if (code.length <= 6) {
            _state.value = current.copy(code = code, actionError = null)
        }

        if (code.length == 6) {
            onConfirmCodeClick()
        }
    }

    override fun onSendCodeClick() {
        val current = _state.value as? LoginByPhoneScreenState.PhoneInput ?: return
        if (current.actionLoading) return

        _state.value = current.copy(actionLoading = true, actionError = null)

        scope.launch {
            sendLoginConfirmationToPhoneUseCase.execute(current.phoneNumber)
                .onSuccess { data ->
                    moveToCodeInput(current.phoneNumber, data.retryAfterSeconds)
                }
                .onError { error ->
                    if (error is UserError.TooManyConfirmationRequests) {
                        moveToCodeInput(current.phoneNumber, error.retryAfterSeconds)
                    } else {
                        _state.value = current.copy(actionLoading = false, actionError = error)
                    }
                }
        }
    }

    override fun onConfirmCodeClick() {
        val current = _state.value as? LoginByPhoneScreenState.CodeInput ?: return
        if (current.actionLoading || !current.isCodeFullLength) return

        _state.value = current.copy(actionLoading = true, actionError = null)

        scope.launch {
            loginByPhoneUseCase.execute(current.phoneNumber, current.code)
                .onSuccess { onFinished() }
                .onError { error ->
                    _state.value = current.copy(actionLoading = false, actionError = error)
                }
        }
    }

    override fun onResetPhoneClick() {
        timerJob?.cancel()
        val phone = (_state.value as? LoginByPhoneScreenState.CodeInput)?.phoneNumber ?: ""
        _state.value = LoginByPhoneScreenState.PhoneInput(phoneNumber = phone)
    }

    override fun onBackClick() {
        if (_state.value is LoginByPhoneScreenState.CodeInput) {
            onResetPhoneClick()
        } else {
            onBack()
        }
    }

    private fun moveToCodeInput(phone: String, seconds: Int) {
        _state.value = LoginByPhoneScreenState.CodeInput(
            phoneNumber = phone,
            resendTimerSeconds = seconds
        )
        startTimer(seconds)
    }

    private fun startTimer(seconds: Int) {
        timerJob?.cancel()
        if (seconds <= 0) return

        timerJob = scope.launch {
            var left = seconds
            while (left > 0) {
                delay(1000)
                left--
                updateTimerState(left)
            }
        }
    }

    private fun updateTimerState(seconds: Int) {
        val current = _state.value as? LoginByPhoneScreenState.CodeInput ?: return
        _state.value = current.copy(resendTimerSeconds = seconds)
    }
}