package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.global

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError

/**
 * UI state for editing global platform settings.
 */
sealed interface EditGlobalSettingsScreenState {
    /** Initial loading state. */
    object Loading : EditGlobalSettingsScreenState

    /** Loading error state. */
    data class Error(val error: AppError) : EditGlobalSettingsScreenState

    /** Form content state. */
    data class Content(
        val privacyPolicyUrl: String,
        val termsOfServiceUrl: String,
        val contactSupportEmail: String,
        val minVersionAndroid: String = "",
        val minVersionIos: String = "",
        val minVersionWeb: String = "",
        val minVersionDesktop: String = "",
        val isTracingEnabled: Boolean = true,
        val isMetricsEnabled: Boolean = true,
        val isVerboseLoggingEnabled: Boolean = false,
        val isSaving: Boolean = false,
        val saveError: AppError? = null
    ) : EditGlobalSettingsScreenState
}
