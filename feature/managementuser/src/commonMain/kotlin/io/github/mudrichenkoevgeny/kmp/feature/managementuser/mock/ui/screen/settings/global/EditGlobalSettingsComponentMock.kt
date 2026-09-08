package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.settings.global

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.global.EditGlobalSettingsComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.global.EditGlobalSettingsScreenState

@InternalApi
class EditGlobalSettingsComponentMock(
    initialState: EditGlobalSettingsScreenState = EditGlobalSettingsScreenState.Content(
        privacyPolicyUrl = "https://example.com/privacy",
        termsOfServiceUrl = "https://example.com/terms",
        contactSupportEmail = "support@example.com",
        minVersionAndroid = "1.0.0",
        minVersionIos = "1.0.0",
        minVersionWeb = "1.0.0",
        minVersionDesktop = "1.0.0",
        isTracingEnabled = true,
        isMetricsEnabled = true,
        isVerboseLoggingEnabled = false
    )
) : EditGlobalSettingsComponent {
    override val state: Value<EditGlobalSettingsScreenState> = MutableValue(initialState)

    override fun onRetry() {}
    override fun onPrivacyPolicyUrlChanged(value: String) {}
    override fun onTermsOfServiceUrlChanged(value: String) {}
    override fun onContactSupportEmailChanged(value: String) {}
    override fun onMinVersionAndroidChanged(value: String) {}
    override fun onMinVersionIosChanged(value: String) {}
    override fun onMinVersionWebChanged(value: String) {}
    override fun onMinVersionDesktopChanged(value: String) {}
    override fun onTracingEnabledToggled(enabled: Boolean) {}
    override fun onMetricsEnabledToggled(enabled: Boolean) {}
    override fun onVerboseLoggingEnabledToggled(enabled: Boolean) {}
    override fun onSaveClick() {}
    override fun onBackClick() {}
}
