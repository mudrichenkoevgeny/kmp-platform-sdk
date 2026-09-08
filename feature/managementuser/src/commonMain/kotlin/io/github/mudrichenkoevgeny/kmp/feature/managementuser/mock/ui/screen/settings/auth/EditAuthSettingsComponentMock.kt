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
        enabledProviders = setOf(UserAuthProvider.EMAIL, UserAuthProvider.PHONE, UserAuthProvider.GOOGLE),
        maxTotalIdentifiers = "10",
        maxEmailIdentifiers = "5",
        maxPhoneIdentifiers = "5",
        maxIdentifiersPerExternalProvider = "2",
        maxActiveSessions = "3",
        accessTokenExpirationSeconds = "3600",
        refreshTokenExpirationSeconds = "86400",
        accountDeletionDelaySeconds = "604800",
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
    override fun onMaxActiveSessionsChanged(value: String) {}
    override fun onAccessTokenExpirationSecondsChanged(value: String) {}
    override fun onRefreshTokenExpirationSecondsChanged(value: String) {}
    override fun onAccountDeletionDelaySecondsChanged(value: String) {}
    override fun onRegistrationEnabledToggled(enabled: Boolean) {}
    override fun onSaveClick() {}
    override fun onBackClick() {}
}
