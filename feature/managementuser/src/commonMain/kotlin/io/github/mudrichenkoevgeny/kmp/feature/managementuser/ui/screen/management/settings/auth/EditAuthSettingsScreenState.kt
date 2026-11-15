package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.auth

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider

/**
 * UI state for editing authentication settings.
 */
public sealed interface EditAuthSettingsScreenState {
    /** Initial loading state. */
    public object Loading : EditAuthSettingsScreenState

    /** Loading error state. */
    public data class Error(public val error: AppError) : EditAuthSettingsScreenState

    /** Form content state. */
    public data class Content(
        public val enabledProviders: Set<UserAuthProvider>,
        public val maxTotalIdentifiers: String,
        public val maxEmailIdentifiers: String,
        public val maxPhoneIdentifiers: String,
        public val maxIdentifiersPerExternalProvider: String,
        public val userNicknameMinLength: String,
        public val userNicknameMaxLength: String,
        public val userDisplayNameMinLength: String,
        public val userDisplayNameMaxLength: String,
        public val isUserNicknameChangeAllowed: Boolean,
        public val maxActiveSessionsForOpenUser: String,
        public val maxActiveSessionsForManagementUser: String,
        public val accessTokenExpirationSeconds: String,
        public val refreshTokenExpirationSeconds: String,
        public val accountDeletionGracePeriodSeconds: String,
        public val accountDeletionCheckIntervalSeconds: String,
        public val isRegistrationEnabled: Boolean = true,
        public val openEmailBlacklistEnabled: Boolean = false,
        public val openEmailBlacklist: String = "",
        public val openEmailWhitelistEnabled: Boolean = false,
        public val openEmailWhitelist: String = "",
        public val managementEmailBlacklistEnabled: Boolean = false,
        public val managementEmailBlacklist: String = "",
        public val managementEmailWhitelistEnabled: Boolean = false,
        public val managementEmailWhitelist: String = "",
        public val isSaving: Boolean = false,
        public val saveError: AppError? = null
    ) : EditAuthSettingsScreenState
}
