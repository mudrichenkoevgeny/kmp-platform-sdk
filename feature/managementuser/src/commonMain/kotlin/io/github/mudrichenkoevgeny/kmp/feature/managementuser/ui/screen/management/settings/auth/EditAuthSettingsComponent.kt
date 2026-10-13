package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.auth

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.SettingsDialogChild
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.SettingsDialogConfig
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider

/**
 * Decompose controller for editing authentication settings.
 */
interface EditAuthSettingsComponent {
    /** Observable UI state. */
    val state: Value<EditAuthSettingsScreenState>

    /** Decompose child slot for settings confirmation dialogs. */
    val dialogSlot: Value<ChildSlot<SettingsDialogConfig, SettingsDialogChild>>

    /** Slot navigator for settings dialogs. */
    val dialogSlotNavigation: SlotNavigation<SettingsDialogConfig>

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

    /** Shows reset confirmation dialog. */
    fun onResetClick()

    /** Confirms resetting auth settings to defaults. */
    fun onConfirmReset()

    /** Dismisses active confirmation dialogs. */
    fun onDismissDialog()

    /** Navigates back. */
    fun onBackClick()
}
