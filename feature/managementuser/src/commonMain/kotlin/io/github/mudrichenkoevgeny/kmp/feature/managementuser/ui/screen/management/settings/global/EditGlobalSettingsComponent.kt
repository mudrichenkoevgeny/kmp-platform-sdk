package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.global

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.SettingsDialogChild
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.SettingsDialogConfig

/**
 * Decompose controller for editing global platform settings.
 */
interface EditGlobalSettingsComponent {
    /** Observable UI state. */
    val state: Value<EditGlobalSettingsScreenState>

    /** Decompose child slot for settings confirmation dialogs. */
    val dialogSlot: Value<ChildSlot<SettingsDialogConfig, SettingsDialogChild>>

    /** Slot navigator for settings dialogs. */
    val dialogSlotNavigation: SlotNavigation<SettingsDialogConfig>

    /** Retries loading settings. */
    fun onRetry()

    /** Updates privacy policy URL field. */
    fun onPrivacyPolicyUrlChanged(value: String)

    /** Updates terms of service URL field. */
    fun onTermsOfServiceUrlChanged(value: String)

    /** Updates contact support email field. */
    fun onContactSupportEmailChanged(value: String)

    /** Updates min Android version field. */
    fun onMinVersionAndroidChanged(value: String)

    /** Updates min iOS version field. */
    fun onMinVersionIosChanged(value: String)

    /** Updates min Web version field. */
    fun onMinVersionWebChanged(value: String)

    /** Updates min Desktop version field. */
    fun onMinVersionDesktopChanged(value: String)

    /** Toggles tracing enabled. */
    fun onTracingEnabledToggled(enabled: Boolean)

    /** Toggles metrics enabled. */
    fun onMetricsEnabledToggled(enabled: Boolean)

    /** Toggles verbose logging enabled. */
    fun onVerboseLoggingEnabledToggled(enabled: Boolean)

    /** Saves global settings to the backend. */
    fun onSaveClick()

    /** Shows reset confirmation dialog. */
    fun onResetClick()

    /** Confirms resetting global settings to defaults. */
    fun onConfirmReset()

    /** Dismisses active confirmation dialogs. */
    fun onDismissDialog()

    /** Navigates back. */
    fun onBackClick()
}
