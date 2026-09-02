package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.email

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.onError
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.core.security.error.naming.ClientSecurityErrorCodes as LocalSecurityErrorCodes
import io.github.mudrichenkoevgeny.kmp.core.security.usecase.ValidatePasswordUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.model.apptype.AppType
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.LoginByEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.utils.FieldValidator
import io.github.mudrichenkoevgeny.shared.foundation.core.security.error.naming.SecurityErrorArgs
import io.github.mudrichenkoevgeny.shared.foundation.core.security.error.naming.SecurityErrorCodes
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Default [LoginByEmailComponent]: validates credentials and performs email login through [LoginByEmailUseCase].
 *
 * @param componentContext Decompose [ComponentContext].
 * @param appType Defines the application context (Client/Management) to toggle features like registration.
 * @param loginByEmailUseCase Performs sign-in with email and password.
 * @param validatePasswordUseCase Enforces password rules before network calls.
 * @param onNavigateToRegistrationByEmail Opens the registration screen on the parent stack.
 * @param onNavigateToForgotPassword Opens the reset-password screen on the parent stack.
 * @param onNavigateToTotp Opens the MFA/TOTP screen on the parent stack.
 * @param onBack Pops this screen on the parent stack.
 * @param onFinished Invoked when login succeeds so the host can close the flow.
 */
class LoginByEmailComponentImpl(
    componentContext: ComponentContext,
    appType: AppType,
    private val loginByEmailUseCase: LoginByEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val onNavigateToRegistrationByEmail: () -> Unit,
    private val onNavigateToForgotPassword: () -> Unit,
    private val onNavigateToTotp: (mfaToken: String) -> Unit,
    private val onBack: () -> Unit,
    private val onFinished: () -> Unit
) : LoginByEmailComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()
    private var passwordValidationJob: Job? = null

    private val _state = MutableValue<LoginByEmailScreenState>(
        LoginByEmailScreenState.Content(
            isRegistrationAvailable = appType == AppType.CLIENT
        )
    )
    override val state: Value<LoginByEmailScreenState> = _state

    override fun onEmailChanged(email: String) {
        val current = _state.value as? LoginByEmailScreenState.Content ?: return
        _state.value = current.copy(
            email = email,
            isEmailValid = FieldValidator.isEmailValid(email),
            actionError = null
        )
    }

    override fun onPasswordChanged(password: String) {
        val current = _state.value as? LoginByEmailScreenState.Content ?: return

        _state.value = current.copy(
            password = password,
            actionError = null
        )

        passwordValidationJob?.cancel()
        passwordValidationJob = scope.launch {
            val result = validatePasswordUseCase(password)

            val isPasswordValid = when (result) {
                is AppResult.Success -> true
                is AppResult.Error -> result.error.code != LocalSecurityErrorCodes.PASSWORD_TOO_SHORT &&
                        result.error.code != LocalSecurityErrorCodes.PASSWORD_POLICY_UNAVAILABLE
            }

            val updated = _state.value as? LoginByEmailScreenState.Content ?: return@launch
            _state.value = updated.copy(
                isPasswordValid = isPasswordValid
            )
        }
    }

    override fun onTogglePasswordVisibility() {
        val current = _state.value as? LoginByEmailScreenState.Content ?: return
        _state.value = current.copy(isPasswordVisible = !current.isPasswordVisible)
    }

    override fun onLoginClick() {
        val current = _state.value as? LoginByEmailScreenState.Content ?: return
        if (!current.canLogin) return

        _state.value = current.copy(actionLoading = true, actionError = null)

        scope.launch {
            validatePasswordUseCase(current.password)
                .onSuccess {
                    loginByEmailUseCase.execute(current.email, current.password)
                        .onSuccess { onFinished() }
                        .onError { error ->
                            if (error.code == SecurityErrorCodes.TOTP_CONFIRMATION_REQUIRED) {
                                val mfaToken = error.args?.get(SecurityErrorArgs.MFA_TOKEN)
                                if (mfaToken != null) {
                                    onNavigateToTotp(mfaToken)
                                    return@onError
                                }
                            }

                            _state.value = current.copy(
                                actionLoading = false,
                                actionError = error
                            )
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

    override fun onForgotPasswordClick() {
        onNavigateToForgotPassword()
    }

    override fun onRegistrationClick() {
        onNavigateToRegistrationByEmail()
    }

    override fun onBackClick() {
        onBack()
    }
}