package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.resetpassword

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackDispatcher
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.onError
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.core.common.time.resendCountdown
import io.github.mudrichenkoevgeny.kmp.core.security.error.naming.ClientSecurityErrorCodes
import io.github.mudrichenkoevgeny.kmp.core.security.usecase.ValidatePasswordUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.resetpassword.ResetPasswordRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.resetpassword.ResetEmailPasswordUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.resetpassword.SendResetPasswordConfirmationToEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.utils.FieldValidator
import kotlinx.coroutines.Job
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.auth.resetpassword.ResetPasswordParams
import kotlinx.coroutines.launch

/**
 * Default [ResetEmailPasswordComponent]: send reset code, cooldowns, and password update via use cases.
 *
 * @param componentContext Decompose [ComponentContext].
 * @param params Screen destination parameters.
 * @param resetPasswordRepository exposes remaining reset-confirmation delay per email.
 * @param sendResetPasswordConfirmationToEmailUseCase sends the reset verification code.
 * @param resetEmailPasswordUseCase applies the new password after code verification.
 * @param validatePasswordUseCase enforces password rules before submit.
 * @param onBack pops this screen or returns to email step.
 * @param onFinished invoked when the password was reset successfully.
 */
class ResetEmailPasswordComponentImpl(
    componentContext: ComponentContext,
    params: ResetPasswordParams = ResetPasswordParams(),
    private val resetPasswordRepository: ResetPasswordRepository,
    private val sendResetPasswordConfirmationToEmailUseCase: SendResetPasswordConfirmationToEmailUseCase,
    private val resetEmailPasswordUseCase: ResetEmailPasswordUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val onBack: () -> Unit,
    private val onFinished: () -> Unit
) : ResetEmailPasswordComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()
    private var timerJob: Job? = null
    private var passwordValidationJob: Job? = null

    private val startStepConfig: ResetPasswordStepConfig = if (!params.code.isNullOrBlank() || !params.email.isNullOrBlank()) {
        if (!params.code.isNullOrBlank()) {
            ResetPasswordStepConfig.ConfirmInput(email = params.email ?: "")
        } else {
            ResetPasswordStepConfig.EmailInput(initialEmail = params.email ?: "")
        }
    } else {
        ResetPasswordStepConfig.EmailInput()
    }

    override val stackNavigation = StackNavigation<ResetPasswordStepConfig>()

    override val stack: Value<ChildStack<ResetPasswordStepConfig, ResetEmailPasswordComponent.Child>> =
        childStack(
            source = stackNavigation,
            serializer = ResetPasswordStepConfig.serializer(),
            initialConfiguration = startStepConfig,
            handleBackButton = true,
            childFactory = ::createChild
        )

    private val _state = MutableValue(
        ResetEmailPasswordScreenState(
            email = when (startStepConfig) {
                is ResetPasswordStepConfig.EmailInput -> startStepConfig.initialEmail
                is ResetPasswordStepConfig.ConfirmInput -> startStepConfig.email
            },
            resendTimerSeconds = when (startStepConfig) {
                is ResetPasswordStepConfig.EmailInput -> 0
                is ResetPasswordStepConfig.ConfirmInput -> startStepConfig.resendTimerSeconds
            }
        )
    )
    override val state: Value<ResetEmailPasswordScreenState> = _state

    init {
        if (startStepConfig is ResetPasswordStepConfig.ConfirmInput && startStepConfig.resendTimerSeconds > 0) {
            startTimer(startStepConfig.resendTimerSeconds)
        }
    }

    private fun createChild(
        config: ResetPasswordStepConfig,
        context: ComponentContext
    ): ResetEmailPasswordComponent.Child = when (config) {
        is ResetPasswordStepConfig.EmailInput -> ResetEmailPasswordComponent.Child.EmailInput(config.initialEmail)
        is ResetPasswordStepConfig.ConfirmInput -> ResetEmailPasswordComponent.Child.ConfirmInput(config.email)
    }

    override fun onEmailChanged(email: String) {
        val isValid = FieldValidator.isEmailValid(email)

        _state.value = _state.value.copy(
            email = email,
            isEmailValid = isValid,
            actionError = null
        )

        if (isValid) {
            val remaining =
                resetPasswordRepository.getRemainingResetPasswordConfirmationDelayInSeconds(email)
            if (remaining > 0) {
                moveToResetInput(email, remaining)
            }
        }
    }

    override fun onPasswordChanged(password: String) {
        _state.value = _state.value.copy(
            newPassword = password,
            actionError = null
        )

        passwordValidationJob?.cancel()
        passwordValidationJob = scope.launch {
            val result = validatePasswordUseCase(password)

            val isPasswordValid = when (result) {
                is AppResult.Success -> true
                is AppResult.Error -> result.error.code != ClientSecurityErrorCodes.PASSWORD_TOO_SHORT &&
                        result.error.code != ClientSecurityErrorCodes.PASSWORD_POLICY_UNAVAILABLE
            }

            _state.value = _state.value.copy(
                isPasswordValid = isPasswordValid
            )
        }
    }

    override fun onCodeChanged(code: String) {
        if (code.length <= _state.value.codeLength) {
            _state.value = _state.value.copy(code = code, actionError = null)
        }
    }

    override fun onSendCodeClick() {
        val email = _state.value.email
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
        val current = _state.value
        if (!current.canConfirm) {
            return
        }

        _state.value = current.copy(actionLoading = true, actionError = null)

        scope.launch {
            validatePasswordUseCase(current.newPassword)
                .onSuccess {
                    resetEmailPasswordUseCase.execute(
                        email = current.email,
                        newPassword = current.newPassword,
                        confirmationCode = current.code
                    )
                        .onSuccess { onFinished() }
                        .onError { error ->
                            _state.value = _state.value.copy(actionLoading = false, actionError = error)
                        }
                }
                .onError { error ->
                    _state.value = _state.value.copy(actionLoading = false, actionError = error)
                }
        }
    }

    override fun onResetEmailClick() {
        timerJob?.cancel()
        val email = _state.value.email
        _state.value = _state.value.copy(
            code = "",
            newPassword = "",
            isPasswordValid = false,
            actionError = null,
            resendTimerSeconds = 0
        )
        stackNavigation.bringToFront(ResetPasswordStepConfig.EmailInput(email))
    }

    override fun onBackClick() {
        if ((backHandler as? BackDispatcher)?.back() != true) {
            onBack()
        }
    }

    private fun moveToResetInput(email: String, seconds: Int) {
        _state.value = _state.value.copy(
            email = email,
            resendTimerSeconds = seconds,
            actionLoading = false,
            actionError = null
        )
        stackNavigation.bringToFront(ResetPasswordStepConfig.ConfirmInput(email, seconds))
        startTimer(seconds)
    }

    private fun startTimer(seconds: Int) {
        timerJob?.cancel()
        if (seconds <= 0) {
            return
        }

        timerJob = scope.launch {
            resendCountdown(
                totalSeconds = seconds,
                onTick = ::updateTimerState
            )
        }
    }

    private fun updateTimerState(seconds: Int) {
        _state.value = _state.value.copy(resendTimerSeconds = seconds)
    }

    private fun updateLoading(isLoading: Boolean) {
        _state.value = _state.value.copy(
            actionLoading = isLoading,
            actionError = null
        )
    }

    private fun updateError(error: AppError) {
        _state.value = _state.value.copy(
            actionLoading = false,
            actionError = error
        )
    }
}
