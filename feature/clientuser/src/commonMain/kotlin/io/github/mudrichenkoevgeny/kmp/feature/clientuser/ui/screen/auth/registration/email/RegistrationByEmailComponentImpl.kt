package io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.registration.email

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackDispatcher
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.onError
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.core.common.time.resendCountdown
import io.github.mudrichenkoevgeny.kmp.core.security.error.naming.ClientSecurityErrorCodes
import io.github.mudrichenkoevgeny.kmp.core.security.usecase.ValidatePasswordUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.registration.RegistrationRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.registration.RegistrationByEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.registration.SendRegistrationConfirmationToEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.utils.FieldValidator
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.client.auth.registration.email.ClientRegistrationByEmailParams
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Default [RegistrationByEmailComponent]: confirmation email, timers, and registration use case.
 *
 * @param componentContext Decompose [ComponentContext].
 * @param params Screen destination parameters.
 * @param registrationRepository exposes remaining confirmation delay per email.
 * @param sendRegistrationConfirmationToEmailUseCase sends the email verification code.
 * @param registrationByEmailUseCase completes registration with email, password, and code.
 * @param validatePasswordUseCase enforces password rules before submit.
 * @param onBack pops this screen or returns to email step.
 * @param onFinished invoked when registration succeeds.
 */
class RegistrationByEmailComponentImpl(
    componentContext: ComponentContext,
    params: ClientRegistrationByEmailParams = ClientRegistrationByEmailParams(),
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

    override val stackNavigation = StackNavigation<RegistrationByEmailStepConfig>()

    override val stack: Value<ChildStack<RegistrationByEmailStepConfig, RegistrationByEmailComponent.Child>> =
        childStack(
            source = stackNavigation,
            serializer = RegistrationByEmailStepConfig.serializer(),
            initialConfiguration = RegistrationByEmailStepConfig.EmailInput(initialEmail = params.email ?: ""),
            handleBackButton = true,
            childFactory = ::createChild
        )

    private val _state = MutableValue(
        RegistrationByEmailScreenState(
            email = params.email ?: ""
        )
    )
    override val state: Value<RegistrationByEmailScreenState> = _state

    private fun createChild(
        config: RegistrationByEmailStepConfig,
        context: ComponentContext
    ): RegistrationByEmailComponent.Child = when (config) {
        is RegistrationByEmailStepConfig.EmailInput -> RegistrationByEmailComponent.Child.EmailInput(config.initialEmail)
        is RegistrationByEmailStepConfig.ConfirmInput -> RegistrationByEmailComponent.Child.ConfirmInput(config.email)
    }

    override fun onEmailChanged(email: String) {
        val isValid = FieldValidator.isEmailValid(email)

        _state.value = _state.value.copy(
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
        val current = _state.value
        if (current.actionLoading) {
            return
        }

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
                        _state.value = _state.value.copy(actionLoading = false, actionError = error)
                    }
                }
        }
    }

    override fun onCodeChanged(code: String) {
        if (code.length <= _state.value.codeLength) {
            _state.value = _state.value.copy(code = code, actionError = null)
        }
    }

    override fun onPasswordChanged(password: String) {
        _state.value = _state.value.copy(
            password = password,
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

    override fun onTogglePasswordVisibility() {
        _state.value = _state.value.copy(isPasswordVisible = !_state.value.isPasswordVisible)
    }

    override fun onRegisterClick() {
        val current = _state.value
        if (current.actionLoading || !current.canRegister) {
            return
        }

        _state.value = current.copy(actionLoading = true, actionError = null)

        scope.launch {
            validatePasswordUseCase(current.password)
                .onSuccess {
                    registrationByEmailUseCase.execute(
                        email = current.email,
                        password = current.password,
                        confirmationCode = current.code
                    ).onSuccess {
                        _state.value = _state.value.copy(actionLoading = false)
                        onFinished()
                    }.onError { error ->
                        _state.value = _state.value.copy(actionLoading = false, actionError = error)
                    }
                }
                .onError { error ->
                    _state.value = _state.value.copy(
                        actionLoading = false,
                        actionError = error
                    )
                }
        }
    }

    override fun onBackClick() {
        if ((backHandler as? BackDispatcher)?.back() != true) {
            onBack()
        }
    }

    private fun moveToRegistrationInput(email: String, seconds: Int) {
        _state.value = _state.value.copy(
            email = email,
            resendTimerSeconds = seconds,
            actionLoading = false,
            actionError = null
        )
        stackNavigation.bringToFront(RegistrationByEmailStepConfig.ConfirmInput(email, seconds))
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
}
