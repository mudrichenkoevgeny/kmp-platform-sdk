package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.auth

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

    /** Updates max active sessions field. */
    fun onMaxActiveSessionsChanged(value: String)

    /** Updates access token expiration seconds field. */
    fun onAccessTokenExpirationSecondsChanged(value: String)

    /** Updates refresh token expiration seconds field. */
    fun onRefreshTokenExpirationSecondsChanged(value: String)

    /** Updates account deletion delay seconds field. */
    fun onAccountDeletionDelaySecondsChanged(value: String)

    /** Toggles registration enabled. */
    fun onRegistrationEnabledToggled(enabled: Boolean)

    /** Saves updated auth settings to the backend. */
    fun onSaveClick()

    /** Navigates back. */
    fun onBackClick()
}
