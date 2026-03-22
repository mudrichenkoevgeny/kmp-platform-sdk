package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.welcome

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.settings.AvailableAuthProviders

/**
 * UI state for the login welcome screen: initial load, recoverable init failure, or interactive content.
 */
sealed interface LoginWelcomeScreenState {
    /** Providers and legal URLs are being loaded. */
    data object Loading : LoginWelcomeScreenState

    /**
     * Ready to show auth provider buttons, optional legal footer, and inline action errors / loading overlay.
     *
     * @param availableAuthProviders primary (full-width) and secondary (grid) provider lists from the backend/settings.
     * @param privacyPolicyUrl optional URL for the privacy policy link; blank values hide the link.
     * @param termsOfServiceUrl optional URL for the terms link; blank values hide the link.
     * @param actionError error from OAuth or opening legal URLs; shown inline when non-null.
     * @param actionLoading when true, shows a blocking overlay while an async action runs.
     */
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

    /**
     * Failed to load providers or global settings; the user can retry initialization.
     *
     * @param error error to display via [AppError] localization.
     */
    data class InitializationError(
        val error: AppError
    ) : LoginWelcomeScreenState
}