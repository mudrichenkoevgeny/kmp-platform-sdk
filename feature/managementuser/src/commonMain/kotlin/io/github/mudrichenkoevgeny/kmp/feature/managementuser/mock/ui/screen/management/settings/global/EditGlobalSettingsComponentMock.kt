package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.settings.global

import com.arkivanov.decompose.Child
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.SettingsDialogChild
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.SettingsDialogConfig
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.global.EditGlobalSettingsComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.global.EditGlobalSettingsScreenState

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

    override val dialogSlotNavigation = SlotNavigation<SettingsDialogConfig>()
    private val _dialogSlot = MutableValue<ChildSlot<SettingsDialogConfig, SettingsDialogChild>>(ChildSlot())
    override val dialogSlot: Value<ChildSlot<SettingsDialogConfig, SettingsDialogChild>> = _dialogSlot

    fun setDialog(config: SettingsDialogConfig?) {
        val child = when (config) {
            is SettingsDialogConfig.ConfirmReset -> SettingsDialogChild.ConfirmReset
            null -> null
        }
        _dialogSlot.value = ChildSlot(
            child = if (config != null && child != null) Child.Created(configuration = config, instance = child) else null
        )
    }

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
    override fun onResetClick() {}
    override fun onConfirmReset() {}
    override fun onDismissDialog() {}
    override fun onBackClick() {}
}
