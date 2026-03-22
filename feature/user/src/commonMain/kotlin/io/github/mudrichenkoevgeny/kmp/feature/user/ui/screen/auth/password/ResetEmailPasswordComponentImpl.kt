package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.password

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.onError
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.core.security.error.naming.SecurityErrorCodes
import io.github.mudrichenkoevgeny.kmp.core.security.usecase.ValidatePasswordUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.password.PasswordRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.password.ResetEmailPasswordUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.password.SendResetPasswordConfirmationToEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.utils.FieldValidator
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Default [ResetEmailPasswordComponent]: send reset code, cooldowns, and password update via use cases.
 *
 * @param componentContext Decompose [ComponentContext].
 * @param passwordRepository exposes remaining reset-confirmation delay per email.
 * @param sendResetPasswordConfirmationToEmailUseCase sends the reset verification code.
 * @param resetEmailPasswordUseCase applies the new password after code verification.
 * @param validatePasswordUseCase enforces password rules before submit.
 * @param onBack pops this screen or returns to email step.
 * @param onFinished invoked when the password was reset successfully.
 */
class ResetEmailPasswordComponentImpl(
    componentContext: ComponentContext,
    private val passwordRepository: PasswordRepository,
    private val sendResetPasswordConfirmationToEmailUseCase: SendResetPasswordConfirmationToEmailUseCase,
    private val resetEmailPasswordUseCase: ResetEmailPasswordUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val onBack: () -> Unit,
    private val onFinished: () -> Unit
) : ResetEmailPasswordComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()
    private var timerJob: Job? = null
    private var passwordValidationJob: Job? = null

    private val _state = MutableValue<ResetEmailPasswordScreenState>(
        ResetEmailPasswordScreenState.EmailInput()
    )
    override val state: Value<ResetEmailPasswordScreenState> = _state

    override fun onEmailChanged(email: String) {
        val current = _state.value as? ResetEmailPasswordScreenState.EmailInput ?: return
        val isValid = FieldValidator.isEmailValid(email)

        _state.value = current.copy(
            email = email,
            isEmailValid = isValid,
            actionError = null
        )

        if (isValid) {
            val remaining = passwordRepository.getRemainingResetPasswordConfirmationDelayInSeconds(email)
            if (remaining > 0) {
                moveToResetInput(email, remaining)
            }
        }
    }

    override fun onPasswordChanged(password: String) {
        val current = _state.value as? ResetEmailPasswordScreenState.ResetInput ?: return

        _state.value = current.copy(
            newPassword = password,
            actionError = null
        )

        passwordValidationJob?.cancel()
        passwordValidationJob = scope.launch {
            val result = validatePasswordUseCase(password)

            val isTooShort = result is AppResult.Error &&
                    result.error.code == SecurityErrorCodes.PASSWORD_TOO_SHORT

            val updated = _state.value as? ResetEmailPasswordScreenState.ResetInput ?: return@launch
            _state.value = updated.copy(
                isPasswordValid = !isTooShort
            )
        }
    }

    override fun onCodeChanged(code: String) {
        val current = _state.value as? ResetEmailPasswordScreenState.ResetInput ?: return
        if (code.length <= current.codeLength) {
            _state.value = current.copy(code = code, actionError = null)
        }
    }

    override fun onSendCodeClick() {
        val email = when (val s = _state.value) {
            is ResetEmailPasswordScreenState.EmailInput -> s.email
            is ResetEmailPasswordScreenState.ResetInput -> s.email
            else -> return
        }

        updateLoading(true)

        scope.launch {
            sendResetPasswordConfirmationToEmailUseCase.execute(email)
                .onSuccess { data ->
                    moveToResetInput(email, data.retryAfterSeconds)
                }
                .onError { error ->
                    if (error is UserError.TooManyConfirmationRequests) {
                        moveToResetInput(email, error.retryAfterSeconds)
                    } else {
                        updateError(error)
                    }
                }
        }
    }

    override fun onConfirmResetClick() {
        val current = _state.value as? ResetEmailPasswordScreenState.ResetInput ?: return
        if (!current.canConfirm) return

        _state.value = current.copy(actionLoading = true, actionError = null)

        scope.launch {
            resetEmailPasswordUseCase.execute(
                email = current.email,
                newPassword = current.newPassword,
                confirmationCode = current.code
            )
                .onSuccess { onFinished() }
                .onError { error ->
                    _state.value = current.copy(actionLoading = false, actionError = error)
                }
        }
    }

    override fun onResetEmailClick() {
        timerJob?.cancel()
        val email = (_state.value as? ResetEmailPasswordScreenState.ResetInput)?.email ?: ""
        _state.value = ResetEmailPasswordScreenState.EmailInput(email = email)
    }

    override fun onBackClick() {
        if (_state.value is ResetEmailPasswordScreenState.ResetInput) {
            onResetEmailClick()
        } else {
            onBack()
        }
    }

    private fun moveToResetInput(email: String, seconds: Int) {
        _state.value = ResetEmailPasswordScreenState.ResetInput(
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
        val current = _state.value as? ResetEmailPasswordScreenState.ResetInput ?: return
        _state.value = current.copy(resendTimerSeconds = seconds)
    }

    private fun updateLoading(isLoading: Boolean) {
        val current = _state.value
        _state.value = when (current) {
            is ResetEmailPasswordScreenState.EmailInput -> current.copy(actionLoading = isLoading, actionError = null)
            is ResetEmailPasswordScreenState.ResetInput -> current.copy(actionLoading = isLoading, actionError = null)
            else -> current
        }
    }

    private fun updateError(error: AppError) {
        val current = _state.value
        _state.value = when (current) {
            is ResetEmailPasswordScreenState.EmailInput -> current.copy(actionLoading = false, actionError = error)
            is ResetEmailPasswordScreenState.ResetInput -> current.copy(actionLoading = false, actionError = error)
            else -> current
        }
    }
}