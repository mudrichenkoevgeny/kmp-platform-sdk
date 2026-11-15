package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.global

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError

/**
 * UI state for editing global platform settings.
 */
public sealed interface EditGlobalSettingsScreenState {
    /** Initial loading state. */
    public object Loading : EditGlobalSettingsScreenState

    /** Loading error state. */
    public data class Error(public val error: AppError) : EditGlobalSettingsScreenState

    /** Form content state. */
    public data class Content(
        public val privacyPolicyUrl: String,
        public val termsOfServiceUrl: String,
        public val contactSupportEmail: String,
        public val minVersionAndroid: String = "",
        public val minVersionIos: String = "",
        public val minVersionWeb: String = "",
        public val minVersionDesktop: String = "",
        public val isTracingEnabled: Boolean = true,
        public val isMetricsEnabled: Boolean = true,
        public val isVerboseLoggingEnabled: Boolean = false,
        public val isSaving: Boolean = false,
        public val saveError: AppError? = null
    ) : EditGlobalSettingsScreenState
}
