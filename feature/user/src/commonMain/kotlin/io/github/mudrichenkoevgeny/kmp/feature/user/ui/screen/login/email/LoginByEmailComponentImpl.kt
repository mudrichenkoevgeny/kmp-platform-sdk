package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.login.email

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.onError
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.core.security.error.naming.SecurityErrorCodes
import io.github.mudrichenkoevgeny.kmp.core.security.usecase.ValidatePasswordUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.LoginByEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.utils.FieldValidator
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginByEmailComponentImpl(
    componentContext: ComponentContext,
    private val loginByEmailUseCase: LoginByEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val onNavigateToRegistrationByEmail: () -> Unit,
    private val onNavigateToForgotPassword: () -> Unit,
    private val onBack: () -> Unit,
    private val onFinished: () -> Unit
) : LoginByEmailComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()
    private var passwordValidationJob: Job? = null

    private val _state = MutableValue<LoginByEmailScreenState>(
        LoginByEmailScreenState.Content()
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

            val isTooShort = result is AppResult.Error &&
                    result.error.code == SecurityErrorCodes.PASSWORD_TOO_SHORT

            val updated = _state.value as? LoginByEmailScreenState.Content ?: return@launch
            _state.value = updated.copy(
                isPasswordValid = !isTooShort
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