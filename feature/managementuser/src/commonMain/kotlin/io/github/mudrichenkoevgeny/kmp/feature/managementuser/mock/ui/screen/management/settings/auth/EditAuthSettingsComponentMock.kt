package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.settings.auth

import com.arkivanov.decompose.Child
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.auth.EditAuthSettingsComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.auth.EditAuthSettingsScreenState
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.dialog.SettingsDialogChild
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.dialog.SettingsDialogConfig
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider

/**
 * Mock implementation of [EditAuthSettingsComponent] for testing.
 */
@InternalApi
public class EditAuthSettingsComponentMock(
    initialState: EditAuthSettingsScreenState = EditAuthSettingsScreenState.Content(
        enabledProviders = setOf(UserAuthProvider.EMAIL, UserAuthProvider.GOOGLE),
        maxTotalIdentifiers = "10",
        maxEmailIdentifiers = "5",
        maxPhoneIdentifiers = "5",
        maxIdentifiersPerExternalProvider = "2",
        userNicknameMinLength = "3",
        userNicknameMaxLength = "30",
        userDisplayNameMinLength = "1",
        userDisplayNameMaxLength = "50",
        isUserNicknameChangeAllowed = true,
        maxActiveSessionsForOpenUser = "3",
        maxActiveSessionsForManagementUser = "5",
        accessTokenExpirationSeconds = "3600",
        refreshTokenExpirationSeconds = "86400",
        accountDeletionGracePeriodSeconds = "604800",
        accountDeletionCheckIntervalSeconds = "86400",
        isRegistrationEnabled = true
    )
) : EditAuthSettingsComponent {
    public override val state: Value<EditAuthSettingsScreenState> = MutableValue(initialState)

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
    public override fun onProviderToggled(
        provider: UserAuthProvider,
        enabled: Boolean
    ): Unit = Unit

    public override fun onMaxTotalIdentifiersChanged(value: String): Unit = Unit
    public override fun onMaxEmailIdentifiersChanged(value: String): Unit = Unit
    public override fun onMaxPhoneIdentifiersChanged(value: String): Unit = Unit
    public override fun onMaxIdentifiersPerExternalProviderChanged(value: String): Unit = Unit
    public override fun onUserNicknameMinLengthChanged(value: String): Unit = Unit
    public override fun onUserNicknameMaxLengthChanged(value: String): Unit = Unit
    public override fun onUserDisplayNameMinLengthChanged(value: String): Unit = Unit
    public override fun onUserDisplayNameMaxLengthChanged(value: String): Unit = Unit
    public override fun onIsUserNicknameChangeAllowedToggled(enabled: Boolean): Unit = Unit
    public override fun onMaxActiveSessionsForOpenUserChanged(value: String): Unit = Unit
    public override fun onMaxActiveSessionsForManagementUserChanged(value: String): Unit = Unit
    public override fun onAccessTokenExpirationSecondsChanged(value: String): Unit = Unit
    public override fun onRefreshTokenExpirationSecondsChanged(value: String): Unit = Unit
    public override fun onAccountDeletionGracePeriodSecondsChanged(value: String): Unit = Unit
    public override fun onAccountDeletionCheckIntervalSecondsChanged(value: String): Unit = Unit
    public override fun onRegistrationEnabledToggled(enabled: Boolean): Unit = Unit
    public override fun onOpenEmailBlacklistEnabledToggled(enabled: Boolean): Unit = Unit
    public override fun onOpenEmailBlacklistChanged(value: String): Unit = Unit
    public override fun onOpenEmailWhitelistEnabledToggled(enabled: Boolean): Unit = Unit
    public override fun onOpenEmailWhitelistChanged(value: String): Unit = Unit
    public override fun onManagementEmailBlacklistEnabledToggled(enabled: Boolean): Unit = Unit
    public override fun onManagementEmailBlacklistChanged(value: String): Unit = Unit
    public override fun onManagementEmailWhitelistEnabledToggled(enabled: Boolean): Unit = Unit
    public override fun onManagementEmailWhitelistChanged(value: String): Unit = Unit
    public override fun onSaveClick(): Unit = Unit
    public override fun onResetClick(): Unit = Unit
    public override fun onConfirmReset(): Unit = Unit
    public override fun onDismissDialog(): Unit = Unit
    public override fun onBackClick(): Unit = Unit
}
