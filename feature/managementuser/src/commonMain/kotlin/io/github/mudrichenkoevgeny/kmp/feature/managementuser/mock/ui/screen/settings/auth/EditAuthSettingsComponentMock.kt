package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.settings.auth

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.auth.EditAuthSettingsComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.auth.EditAuthSettingsScreenState
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider

@InternalApi
class EditAuthSettingsComponentMock(
    initialState: EditAuthSettingsScreenState = EditAuthSettingsScreenState.Content(
        enabledProviders = setOf(UserAuthProvider.EMAIL, UserAuthProvider.GOOGLE),
        maxTotalIdentifiers = "10",
        maxEmailIdentifiers = "5",
        maxPhoneIdentifiers = "5",
        maxIdentifiersPerExternalProvider = "2",
        maxActiveSessionsForOpenUser = "3",
        maxActiveSessionsForManagementUser = "5",
        accessTokenExpirationSeconds = "3600",
        refreshTokenExpirationSeconds = "86400",
        accountDeletionGracePeriodSeconds = "604800",
        accountDeletionCheckIntervalSeconds = "86400",
        isRegistrationEnabled = true
    )
) : EditAuthSettingsComponent {
    override val state: Value<EditAuthSettingsScreenState> = MutableValue(initialState)

    override fun onRetry() {}
    override fun onProviderToggled(provider: UserAuthProvider, enabled: Boolean) {}
    override fun onMaxTotalIdentifiersChanged(value: String) {}
    override fun onMaxEmailIdentifiersChanged(value: String) {}
    override fun onMaxPhoneIdentifiersChanged(value: String) {}
    override fun onMaxIdentifiersPerExternalProviderChanged(value: String) {}
    override fun onMaxActiveSessionsForOpenUserChanged(value: String) {}
    override fun onMaxActiveSessionsForManagementUserChanged(value: String) {}
    override fun onAccessTokenExpirationSecondsChanged(value: String) {}
    override fun onRefreshTokenExpirationSecondsChanged(value: String) {}
    override fun onAccountDeletionGracePeriodSecondsChanged(value: String) {}
    override fun onAccountDeletionCheckIntervalSecondsChanged(value: String) {}
    override fun onRegistrationEnabledToggled(enabled: Boolean) {}
    override fun onOpenEmailBlacklistEnabledToggled(enabled: Boolean) {}
    override fun onOpenEmailBlacklistChanged(value: String) {}
    override fun onOpenEmailWhitelistEnabledToggled(enabled: Boolean) {}
    override fun onOpenEmailWhitelistChanged(value: String) {}
    override fun onManagementEmailBlacklistEnabledToggled(enabled: Boolean) {}
    override fun onManagementEmailBlacklistChanged(value: String) {}
    override fun onManagementEmailWhitelistEnabledToggled(enabled: Boolean) {}
    override fun onManagementEmailWhitelistChanged(value: String) {}
    override fun onSaveClick() {}
    override fun onResetClick() {}
    override fun onBackClick() {}
}
