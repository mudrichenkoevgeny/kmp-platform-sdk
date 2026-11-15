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

/**
 * Mock implementation of [EditGlobalSettingsComponent] for testing.
 */
@InternalApi
public class EditGlobalSettingsComponentMock(
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
    public override val state: Value<EditGlobalSettingsScreenState> = MutableValue(initialState)

    public override val dialogSlotNavigation: SlotNavigation<SettingsDialogConfig> =
        SlotNavigation<SettingsDialogConfig>()
    private val _dialogSlot = MutableValue<ChildSlot<SettingsDialogConfig, SettingsDialogChild>>(ChildSlot())
    public override val dialogSlot: Value<ChildSlot<SettingsDialogConfig, SettingsDialogChild>> = _dialogSlot

    /** Sets the active dialog configuration. */
    public fun setDialog(config: SettingsDialogConfig?) {
        val child = when (config) {
            is SettingsDialogConfig.ConfirmReset -> SettingsDialogChild.ConfirmReset
            null -> null
        }
        _dialogSlot.value = ChildSlot(
            child = if (config != null && child != null) {
                Child.Created(
                    configuration = config,
                    instance = child
                )
            } else {
                null
            }
        )
    }

    public override fun onRetry(): Unit = Unit
    public override fun onPrivacyPolicyUrlChanged(value: String): Unit = Unit
    public override fun onTermsOfServiceUrlChanged(value: String): Unit = Unit
    public override fun onContactSupportEmailChanged(value: String): Unit = Unit
    public override fun onMinVersionAndroidChanged(value: String): Unit = Unit
    public override fun onMinVersionIosChanged(value: String): Unit = Unit
    public override fun onMinVersionWebChanged(value: String): Unit = Unit
    public override fun onMinVersionDesktopChanged(value: String): Unit = Unit
    public override fun onTracingEnabledToggled(enabled: Boolean): Unit = Unit
    public override fun onMetricsEnabledToggled(enabled: Boolean): Unit = Unit
    public override fun onVerboseLoggingEnabledToggled(enabled: Boolean): Unit = Unit
    public override fun onSaveClick(): Unit = Unit
    public override fun onResetClick(): Unit = Unit
    public override fun onConfirmReset(): Unit = Unit
    public override fun onDismissDialog(): Unit = Unit
    public override fun onBackClick(): Unit = Unit
}
