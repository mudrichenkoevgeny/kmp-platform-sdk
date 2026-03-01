package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.welcome

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.settings.AvailableAuthProviders

sealed interface LoginWelcomeScreenState {
    data object Loading : LoginWelcomeScreenState

    data class Content(
        val availableAuthProviders: AvailableAuthProviders,
        val privacyPolicyUrl: String? = null,
        val termsOfServiceUrl: String? = null,
        val actionError: AppError? = null,
        val actionLoading: Boolean = false
    ) : LoginWelcomeScreenState {
        val validPrivacyPolicyUrl: String? get() = privacyPolicyUrl?.takeIf { it.isNotBlank() }
        val validTermsOfServiceUrl: String? get() = termsOfServiceUrl?.takeIf { it.isNotBlank() }

        val hasPrivacyPolicy: Boolean get() = validPrivacyPolicyUrl != null
        val hasTermsOfService: Boolean get() = validTermsOfServiceUrl != null
    }

    data class InitializationError(
        val error: AppError
    ) : LoginWelcomeScreenState
}