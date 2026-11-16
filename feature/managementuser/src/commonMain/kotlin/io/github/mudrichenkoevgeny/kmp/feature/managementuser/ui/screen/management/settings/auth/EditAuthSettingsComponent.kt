package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.auth

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.dialog.SettingsDialogChild
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.dialog.SettingsDialogConfig
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider

/**
 * Decompose controller for editing authentication settings.
 */
public interface EditAuthSettingsComponent {
    /** Observable UI state. */
    public val state: Value<EditAuthSettingsScreenState>

    /** Decompose child slot for settings confirmation dialogs. */
    public val dialogSlot: Value<ChildSlot<SettingsDialogConfig, SettingsDialogChild>>

    /** Slot navigator for settings dialogs. */
    public val dialogSlotNavigation: SlotNavigation<SettingsDialogConfig>

    /** Retries loading settings. */
    public fun onRetry()

    /** Toggles provider availability. */
    public fun onProviderToggled(
        provider: UserAuthProvider,
        enabled: Boolean
    )

    /** Updates max total identifiers field. */
    public fun onMaxTotalIdentifiersChanged(value: String)

    /** Updates max email identifiers field. */
    public fun onMaxEmailIdentifiersChanged(value: String)

    /** Updates max phone identifiers field. */
    public fun onMaxPhoneIdentifiersChanged(value: String)

    /** Updates max external provider identifiers field. */
    public fun onMaxIdentifiersPerExternalProviderChanged(value: String)

    /** Updates user nickname min length field. */
    public fun onUserNicknameMinLengthChanged(value: String)

    /** Updates user nickname max length field. */
    public fun onUserNicknameMaxLengthChanged(value: String)

    /** Updates user display name min length field. */
    public fun onUserDisplayNameMinLengthChanged(value: String)

    /** Updates user display name max length field. */
    public fun onUserDisplayNameMaxLengthChanged(value: String)

    /** Toggles whether user nickname change is allowed. */
    public fun onIsUserNicknameChangeAllowedToggled(enabled: Boolean)

    /** Updates max active sessions for open user field. */
    public fun onMaxActiveSessionsForOpenUserChanged(value: String)

    /** Updates max active sessions for management user field. */
    public fun onMaxActiveSessionsForManagementUserChanged(value: String)

    /** Updates access token expiration seconds field. */
    public fun onAccessTokenExpirationSecondsChanged(value: String)

    /** Updates refresh token expiration seconds field. */
    public fun onRefreshTokenExpirationSecondsChanged(value: String)

    /** Updates account deletion grace period seconds field. */
    public fun onAccountDeletionGracePeriodSecondsChanged(value: String)

    /** Updates account deletion check interval seconds field. */
    public fun onAccountDeletionCheckIntervalSecondsChanged(value: String)

    /** Toggles registration enabled. */
    public fun onRegistrationEnabledToggled(enabled: Boolean)

    /** Toggles open email blacklist. */
    public fun onOpenEmailBlacklistEnabledToggled(enabled: Boolean)

    /** Updates open email blacklist value. */
    public fun onOpenEmailBlacklistChanged(value: String)

    /** Toggles open email whitelist. */
    public fun onOpenEmailWhitelistEnabledToggled(enabled: Boolean)

    /** Updates open email whitelist value. */
    public fun onOpenEmailWhitelistChanged(value: String)

    /** Toggles management email blacklist. */
    public fun onManagementEmailBlacklistEnabledToggled(enabled: Boolean)

    /** Updates management email blacklist value. */
    public fun onManagementEmailBlacklistChanged(value: String)

    /** Toggles management email whitelist. */
    public fun onManagementEmailWhitelistEnabledToggled(enabled: Boolean)

    /** Updates management email whitelist value. */
    public fun onManagementEmailWhitelistChanged(value: String)

    /** Saves updated auth settings to the backend. */
    public fun onSaveClick()

    /** Shows reset confirmation dialog. */
    public fun onResetClick()

    /** Confirms resetting auth settings to defaults. */
    public fun onConfirmReset()

    /** Dismisses active confirmation dialogs. */
    public fun onDismissDialog()

    /** Navigates back. */
    public fun onBackClick()
}
