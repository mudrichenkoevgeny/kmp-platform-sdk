package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.auth

import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider

/**
 * Decompose controller for editing authentication settings.
 */
interface EditAuthSettingsComponent {
    /** Observable UI state. */
    val state: Value<EditAuthSettingsScreenState>

    /** Retries loading settings. */
    fun onRetry()

    /** Toggles provider availability. */
    fun onProviderToggled(provider: UserAuthProvider, enabled: Boolean)

    /** Updates max total identifiers field. */
    fun onMaxTotalIdentifiersChanged(value: String)

    /** Updates max email identifiers field. */
    fun onMaxEmailIdentifiersChanged(value: String)

    /** Updates max phone identifiers field. */
    fun onMaxPhoneIdentifiersChanged(value: String)

    /** Updates max external provider identifiers field. */
    fun onMaxIdentifiersPerExternalProviderChanged(value: String)

    /** Updates max active sessions for open user field. */
    fun onMaxActiveSessionsForOpenUserChanged(value: String)

    /** Updates max active sessions for management user field. */
    fun onMaxActiveSessionsForManagementUserChanged(value: String)

    /** Updates access token expiration seconds field. */
    fun onAccessTokenExpirationSecondsChanged(value: String)

    /** Updates refresh token expiration seconds field. */
    fun onRefreshTokenExpirationSecondsChanged(value: String)

    /** Updates account deletion grace period seconds field. */
    fun onAccountDeletionGracePeriodSecondsChanged(value: String)

    /** Updates account deletion check interval seconds field. */
    fun onAccountDeletionCheckIntervalSecondsChanged(value: String)

    /** Toggles registration enabled. */
    fun onRegistrationEnabledToggled(enabled: Boolean)

    /** Toggles open email blacklist. */
    fun onOpenEmailBlacklistEnabledToggled(enabled: Boolean)

    /** Updates open email blacklist value. */
    fun onOpenEmailBlacklistChanged(value: String)

    /** Toggles open email whitelist. */
    fun onOpenEmailWhitelistEnabledToggled(enabled: Boolean)

    /** Updates open email whitelist value. */
    fun onOpenEmailWhitelistChanged(value: String)

    /** Toggles management email blacklist. */
    fun onManagementEmailBlacklistEnabledToggled(enabled: Boolean)

    /** Updates management email blacklist value. */
    fun onManagementEmailBlacklistChanged(value: String)

    /** Toggles management email whitelist. */
    fun onManagementEmailWhitelistEnabledToggled(enabled: Boolean)

    /** Updates management email whitelist value. */
    fun onManagementEmailWhitelistChanged(value: String)

    /** Saves updated auth settings to the backend. */
    fun onSaveClick()

    /** Resets auth settings to defaults on the backend. */
    fun onResetClick()

    /** Navigates back. */
    fun onBackClick()
}