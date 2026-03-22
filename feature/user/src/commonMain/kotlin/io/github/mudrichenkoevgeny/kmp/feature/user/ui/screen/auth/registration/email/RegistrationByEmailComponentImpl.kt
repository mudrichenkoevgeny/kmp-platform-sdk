package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.registration.email

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.onError
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.core.security.error.naming.SecurityErrorCodes
import io.github.mudrichenkoevgeny.kmp.core.security.usecase.ValidatePasswordUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.registration.RegistrationRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.registration.RegistrationByEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.registration.SendRegistrationConfirmationToEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.utils.FieldValidator
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Default [RegistrationByEmailComponent]: confirmation email, timers, and registration use case.
 *
 * @param componentContext Decompose [ComponentContext].
 * @param registrationRepository exposes remaining confirmation delay per email.
 * @param sendRegistrationConfirmationToEmailUseCase sends the email verification code.
 * @param registrationByEmailUseCase completes registration with email, password, and code.
 * @param validatePasswordUseCase enforces password rules before submit.
 * @param onBack pops this screen or returns to email step.
 * @param onFinished invoked when registration succeeds.
 */
class RegistrationByEmailComponentImpl(
    componentContext: ComponentContext,
    private val registrationRepository: RegistrationRepository,
    private val sendRegistrationConfirmationToEmailUseCase: SendRegistrationConfirmationToEmailUseCase,
    private val registrationByEmailUseCase: RegistrationByEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val onBack: () -> Unit,
    private val onFinished: () -> Unit
) : RegistrationByEmailComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()
    private var timerJob: Job? = null
    private var passwordValidationJob: Job? = null

    private val _state = MutableValue<RegistrationByEmailScreenState>(
        RegistrationByEmailScreenState.EmailInput()
    )
    override val state: Value<RegistrationByEmailScreenState> = _state

    override fun onEmailChanged(email: String) {
        val current = _state.value as? RegistrationByEmailScreenState.EmailInput ?: return
        val isValid = FieldValidator.isEmailValid(email)

        _state.value = current.copy(
            email = email,
            isEmailValid = isValid,
            actionError = null
        )

        if (isValid) {
            val remaining = registrationRepository.getRemainingRegistrationConfirmationDelayInSeconds(email)
            if (remaining > 0) {
                moveToRegistrationInput(email, remaining)
            }
        }
    }

    override fun onSendCodeClick() {
        val current = _state.value as? RegistrationByEmailScreenState.EmailInput ?: return
        if (current.actionLoading) return

        _state.value = current.copy(actionLoading = true, actionError = null)

        scope.launch {
            sendRegistrationConfirmationToEmailUseCase.execute(current.email)
                .onSuccess { data ->
                    moveToRegistrationInput(current.email, data.retryAfterSeconds)
                }
                .onError { error ->
                    if (error is UserError.TooManyConfirmationRequests) {
                        moveToRegistrationInput(current.email, error.retryAfterSeconds)
                    } else {
                        _state.value = current.copy(actionLoading = false, actionError = error)
                    }
                }
        }
    }

    override fun onCodeChanged(code: String) {
        val current = _state.value as? RegistrationByEmailScreenState.RegistrationInput ?: return
        if (code.length <= current.codeLength) {
            _state.value = current.copy(code = code, actionError = null)
        }
    }

    override fun onPasswordChanged(password: String) {
        val current = _state.value as? RegistrationByEmailScreenState.RegistrationInput ?: return

        _state.value = current.copy(
            password = password,
            actionError = null
        )

        passwordValidationJob?.cancel()
        passwordValidationJob = scope.launch {
            val result = validatePasswordUseCase(password)

            val isTooShort = result is AppResult.Error &&
                    result.error.code == SecurityErrorCodes.PASSWORD_TOO_SHORT

            val updated = _state.value as? RegistrationByEmailScreenState.RegistrationInput ?: return@launch
            _state.value = updated.copy(
                isPasswordValid = !isTooShort
            )
        }
    }

    override fun onTogglePasswordVisibility() {
        val current = _state.value as? RegistrationByEmailScreenState.RegistrationInput ?: return
        _state.value = current.copy(isPasswordVisible = !current.isPasswordVisible)
    }

    override fun onRegisterClick() {
        val current = _state.value as? RegistrationByEmailScreenState.RegistrationInput ?: return
        if (current.actionLoading || !current.canRegister) return

        _state.value = current.copy(actionLoading = true, actionError = null)

        scope.launch {
            validatePasswordUseCase(current.password)
                .onSuccess {
                    registrationByEmailUseCase.execute(
                        email = current.email,
                        password = current.password,
                        confirmationCode = current.code
                    ).onSuccess {
                        onFinished()
                    }.onError { error ->
                        _state.value = current.copy(actionLoading = false, actionError = error)
                    }
                }
                .onError { error ->
                    _state.value = current.copy(
                        actionLoading = false,
                        actionError = error
                    )
                }
        }
    }

    override fun onBackClick() {
        if (_state.value is RegistrationByEmailScreenState.RegistrationInput) {
            timerJob?.cancel()
            val email = (_state.value as? RegistrationByEmailScreenState.RegistrationInput)?.email ?: ""
            _state.value = RegistrationByEmailScreenState.EmailInput(email = email)
        } else {
            onBack()
        }
    }

    private fun moveToRegistrationInput(email: String, seconds: Int) {
        _state.value = RegistrationByEmailScreenState.RegistrationInput(
            email = email,
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
        val current = _state.value as? RegistrationByEmailScreenState.RegistrationInput ?: return
        _state.value = current.copy(resendTimerSeconds = seconds)
    }
}