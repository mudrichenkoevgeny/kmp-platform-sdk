package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.welcome

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.platform.externallauncher.ExternalLauncher
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.onError
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.core.settings.usecase.GetGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.LoginByGoogleUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings.GetAvailableUserAuthProvidersUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserAuthProvider
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LoginWelcomeComponentImpl(
    componentContext: ComponentContext,
    private val externalLauncher: ExternalLauncher,
    private val getGlobalSettingsUseCase: GetGlobalSettingsUseCase,
    private val getAvailableUserAuthProvidersUseCase: GetAvailableUserAuthProvidersUseCase,
    private val loginByGoogleUseCase: LoginByGoogleUseCase,
    private val onNavigateToLoginByEmail: () -> Unit,
    private val onNavigateToLoginByPhone: () -> Unit,
    private val onFinished: () -> Unit
) : LoginWelcomeComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()

    private val _state = MutableValue<LoginWelcomeScreenState>(
        LoginWelcomeScreenState.Loading
    )
    override val state: Value<LoginWelcomeScreenState> = _state

    init {
        initScreen()
    }

    private fun initScreen() {
        scope.launch {
            _state.value = LoginWelcomeScreenState.Loading

            val authProvidersDeferred = async { getAvailableUserAuthProvidersUseCase() }
            val globalSettingsDeferred = async { getGlobalSettingsUseCase() }

            val authProvidersResult = authProvidersDeferred.await()
            val globalSettingsResult = globalSettingsDeferred.await()

            if (authProvidersResult is AppResult.Success && globalSettingsResult is AppResult.Success) {
                _state.value = LoginWelcomeScreenState.Content(
                    availableAuthProviders = authProvidersResult.data,
                    privacyPolicyUrl = globalSettingsResult.data.privacyPolicyUrl,
                    termsOfServiceUrl = globalSettingsResult.data.termsOfServiceUrl
                )
            } else {
                authProvidersResult.onError { error ->
                    _state.value = LoginWelcomeScreenState.InitializationError(error)
                }
                globalSettingsResult.onError { error ->
                    _state.value = LoginWelcomeScreenState.InitializationError(error)
                }
            }
        }
    }

    override fun onRetryInitClick() {
        initScreen()
    }

    override fun onLoginClick(authProvider: UserAuthProvider) {
        if (isActionStateLoading()) {
            return
        }
        startActionState()

        when (authProvider) {
            UserAuthProvider.EMAIL -> {
                onNavigateToLoginByEmail()
                stopActionState()
            }
            UserAuthProvider.PHONE -> {
                onNavigateToLoginByPhone()
                stopActionState()
            }
            UserAuthProvider.GOOGLE -> {
                loginByGoogle()
            }
            UserAuthProvider.APPLE -> {
                // todo iOS development
                stopActionState(
                    UserError.ExternalAuthFailed(
                        Exception("Apple auth not supported")
                    )
                )
            }
        }
    }

    private fun loginByGoogle() {
        scope.launch {
            loginByGoogleUseCase.execute()
                .onSuccess {
                    onFinished()
                }
                .onError { appError ->
                    stopActionState(appError)
                }
        }
    }

    override fun onPrivacyPolicyClick() {
        if (isActionStateLoading()) {
            return
        }
        val state = startActionState()

        val validPrivacyPolicyUrl = state?.validPrivacyPolicyUrl
        if (validPrivacyPolicyUrl != null) {
            externalLauncher.openUrl(validPrivacyPolicyUrl)
            stopActionState()
        }
    }

    override fun onTermsOfServiceClick() {
        if (isActionStateLoading()) {
            return
        }
        val state = startActionState()

        val validTermsOfServiceUrl = state?.validTermsOfServiceUrl
        if (validTermsOfServiceUrl != null) {
            externalLauncher.openUrl(validTermsOfServiceUrl)
            stopActionState()
        }
    }

    private fun startActionState(): LoginWelcomeScreenState.Content? {
        val current = _state.value as? LoginWelcomeScreenState.Content ?: return null
        val newContentState = current.copy(
            actionError = null,
            actionLoading = true
        )
        _state.value = newContentState
        return newContentState
    }

    private fun stopActionState(
        actionError: AppError? = null
    ): LoginWelcomeScreenState.Content? {
        val current = _state.value as? LoginWelcomeScreenState.Content ?: return null
        val newContentState = current.copy(
            actionError = actionError,
            actionLoading = false
        )
        _state.value = newContentState
        return newContentState
    }

    private fun isActionStateLoading(): Boolean {
        val current = _state.value as? LoginWelcomeScreenState.Content ?: return false
        return current.actionLoading
    }
}