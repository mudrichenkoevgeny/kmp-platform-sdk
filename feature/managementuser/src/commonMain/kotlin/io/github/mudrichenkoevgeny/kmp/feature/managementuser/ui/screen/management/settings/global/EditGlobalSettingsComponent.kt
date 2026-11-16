package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.global

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.dialog.SettingsDialogChild
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.dialog.SettingsDialogConfig

/**
 * Decompose controller for editing global platform settings.
 */
public interface EditGlobalSettingsComponent {
    /** Observable UI state. */
    public val state: Value<EditGlobalSettingsScreenState>

    /** Decompose child slot for settings confirmation dialogs. */
    public val dialogSlot: Value<ChildSlot<SettingsDialogConfig, SettingsDialogChild>>

    /** Slot navigator for settings dialogs. */
    public val dialogSlotNavigation: SlotNavigation<SettingsDialogConfig>

    /** Retries loading settings. */
    public fun onRetry()

    /** Updates privacy policy URL field. */
    public fun onPrivacyPolicyUrlChanged(value: String)

    /** Updates terms of service URL field. */
    public fun onTermsOfServiceUrlChanged(value: String)

    /** Updates contact support email field. */
    public fun onContactSupportEmailChanged(value: String)

    /** Updates min Android version field. */
    public fun onMinVersionAndroidChanged(value: String)

    /** Updates min iOS version field. */
    public fun onMinVersionIosChanged(value: String)

    /** Updates min Web version field. */
    public fun onMinVersionWebChanged(value: String)

    /** Updates min Desktop version field. */
    public fun onMinVersionDesktopChanged(value: String)

    /** Toggles tracing enabled. */
    public fun onTracingEnabledToggled(enabled: Boolean)

    /** Toggles metrics enabled. */
    public fun onMetricsEnabledToggled(enabled: Boolean)

    /** Toggles verbose logging enabled. */
    public fun onVerboseLoggingEnabledToggled(enabled: Boolean)

    /** Saves global settings to the backend. */
    public fun onSaveClick()

    /** Shows reset confirmation dialog. */
    public fun onResetClick()

    /** Confirms resetting global settings to defaults. */
    public fun onConfirmReset()

    /** Dismisses active confirmation dialogs. */
    public fun onDismissDialog()

    /** Navigates back. */
    public fun onBackClick()
}
