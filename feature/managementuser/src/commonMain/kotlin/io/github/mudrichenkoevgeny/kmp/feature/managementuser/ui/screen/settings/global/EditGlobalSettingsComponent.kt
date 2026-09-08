package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.global

import com.arkivanov.decompose.value.Value

/**
 * Decompose controller for editing global platform settings.
 */
interface EditGlobalSettingsComponent {
    /** Observable UI state. */
    val state: Value<EditGlobalSettingsScreenState>

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

    /** Navigates back. */
    fun onBackClick()
}
