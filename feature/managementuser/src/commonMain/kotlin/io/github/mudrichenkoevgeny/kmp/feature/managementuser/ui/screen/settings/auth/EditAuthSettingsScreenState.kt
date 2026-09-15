package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.auth

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider

/**
 * UI state for editing authentication settings.
 */
sealed interface EditAuthSettingsScreenState {
    /** Initial loading state. */
    object Loading : EditAuthSettingsScreenState

    /** Loading error state. */
    data class Error(val error: AppError) : EditAuthSettingsScreenState

    /** Form content state. */
    data class Content(
        val enabledProviders: Set<UserAuthProvider>,
        val maxTotalIdentifiers: String,
        val maxEmailIdentifiers: String,
        val maxPhoneIdentifiers: String,
        val maxIdentifiersPerExternalProvider: String,
        val maxActiveSessionsForOpenUser: String,
        val maxActiveSessionsForManagementUser: String,
        val accessTokenExpirationSeconds: String,
        val refreshTokenExpirationSeconds: String,
        val accountDeletionGracePeriodSeconds: String,
        val accountDeletionCheckIntervalSeconds: String,
        val isRegistrationEnabled: Boolean = true,
        val openEmailBlacklistEnabled: Boolean = false,
        val openEmailBlacklist: String = "",
        val openEmailWhitelistEnabled: Boolean = false,
        val openEmailWhitelist: String = "",
        val managementEmailBlacklistEnabled: Boolean = false,
        val managementEmailBlacklist: String = "",
        val managementEmailWhitelistEnabled: Boolean = false,
        val managementEmailWhitelist: String = "",
        val isSaving: Boolean = false,
        val saveError: AppError? = null
    ) : EditAuthSettingsScreenState
}
