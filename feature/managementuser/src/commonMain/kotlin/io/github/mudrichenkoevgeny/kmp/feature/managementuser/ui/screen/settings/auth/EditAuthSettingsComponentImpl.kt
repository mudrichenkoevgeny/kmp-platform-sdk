package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.auth

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings.GetManagementAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings.SaveRemoteAuthSettingsUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.AvailableAuthProviders
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.ManagementAuthSettings
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import kotlinx.coroutines.launch

/**
 * Default implementation of [EditAuthSettingsComponent].
 */
class EditAuthSettingsComponentImpl(
    componentContext: ComponentContext,
    private val getManagementAuthSettingsUseCase: GetManagementAuthSettingsUseCase,
    private val saveRemoteAuthSettingsUseCase: SaveRemoteAuthSettingsUseCase,
    private val onBack: () -> Unit
) : EditAuthSettingsComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()
    private val _state = MutableValue<EditAuthSettingsScreenState>(EditAuthSettingsScreenState.Loading)
    override val state: Value<EditAuthSettingsScreenState> = _state

    init {
        loadSettings()
    }

    override fun onRetry() {
        loadSettings()
    }

    private fun loadSettings() {
        scope.launch {
            _state.value = EditAuthSettingsScreenState.Loading
            val result = getManagementAuthSettingsUseCase()
            val nextState = when (result) {
                is AppResult.Success -> {
                    val settings = result.data
                    val enabled = (settings.availableAuthProviders.primary + settings.availableAuthProviders.secondary).toSet()
                    EditAuthSettingsScreenState.Content(
                        enabledProviders = enabled,
                        maxTotalIdentifiers = settings.maxTotalIdentifiers.toString(),
                        maxEmailIdentifiers = settings.maxEmailIdentifiers.toString(),
                        maxPhoneIdentifiers = settings.maxPhoneIdentifiers.toString(),
                        maxIdentifiersPerExternalProvider = settings.maxIdentifiersPerExternalProvider.toString(),
                        maxActiveSessions = settings.maxActiveSessions.toString(),
                        accessTokenExpirationSeconds = settings.accessTokenExpirationSeconds.toString(),
                        refreshTokenExpirationSeconds = settings.refreshTokenExpirationSeconds.toString(),
                        accountDeletionDelaySeconds = settings.accountDeletionDelaySeconds.toString(),
                        isRegistrationEnabled = settings.isRegistrationEnabled
                    )
                }
                is AppResult.Error -> EditAuthSettingsScreenState.Error(result.error)
            }
            _state.value = nextState
        }
    }

    override fun onProviderToggled(provider: UserAuthProvider, enabled: Boolean) {
        val current = _state.value as? EditAuthSettingsScreenState.Content ?: return
        val newProviders = if (enabled) {
            current.enabledProviders + provider
        } else {
            current.enabledProviders - provider
        }
        _state.value = current.copy(enabledProviders = newProviders, saveError = null)
    }

    override fun onMaxTotalIdentifiersChanged(value: String) {
        updateContent { copy(maxTotalIdentifiers = value, saveError = null) }
    }

    override fun onMaxEmailIdentifiersChanged(value: String) {
        updateContent { copy(maxEmailIdentifiers = value, saveError = null) }
    }

    override fun onMaxPhoneIdentifiersChanged(value: String) {
        updateContent { copy(maxPhoneIdentifiers = value, saveError = null) }
    }

    override fun onMaxIdentifiersPerExternalProviderChanged(value: String) {
        updateContent { copy(maxIdentifiersPerExternalProvider = value, saveError = null) }
    }

    override fun onMaxActiveSessionsChanged(value: String) {
        updateContent { copy(maxActiveSessions = value, saveError = null) }
    }

    override fun onAccessTokenExpirationSecondsChanged(value: String) {
        updateContent { copy(accessTokenExpirationSeconds = value, saveError = null) }
    }

    override fun onRefreshTokenExpirationSecondsChanged(value: String) {
        updateContent { copy(refreshTokenExpirationSeconds = value, saveError = null) }
    }

    override fun onAccountDeletionDelaySecondsChanged(value: String) {
        updateContent { copy(accountDeletionDelaySeconds = value, saveError = null) }
    }

    override fun onRegistrationEnabledToggled(enabled: Boolean) {
        updateContent { copy(isRegistrationEnabled = enabled, saveError = null) }
    }

    private inline fun updateContent(transform: EditAuthSettingsScreenState.Content.() -> EditAuthSettingsScreenState.Content) {
        val current = _state.value as? EditAuthSettingsScreenState.Content ?: return
        _state.value = current.transform()
    }

    override fun onSaveClick() {
        val current = _state.value as? EditAuthSettingsScreenState.Content ?: return
        scope.launch {
            _state.value = current.copy(isSaving = true, saveError = null)

            val primaryProviders = listOf(UserAuthProvider.EMAIL, UserAuthProvider.PHONE).filter { it in current.enabledProviders }
            val secondaryProviders = listOf(UserAuthProvider.GOOGLE, UserAuthProvider.APPLE).filter { it in current.enabledProviders }

            val settings = ManagementAuthSettings(
                availableAuthProviders = AvailableAuthProviders(
                    primary = primaryProviders,
                    secondary = secondaryProviders
                ),
                maxTotalIdentifiers = current.maxTotalIdentifiers.toIntOrNull() ?: 10,
                maxEmailIdentifiers = current.maxEmailIdentifiers.toIntOrNull() ?: 5,
                maxPhoneIdentifiers = current.maxPhoneIdentifiers.toIntOrNull() ?: 5,
                maxIdentifiersPerExternalProvider = current.maxIdentifiersPerExternalProvider.toIntOrNull() ?: 2,
                maxActiveSessions = current.maxActiveSessions.toIntOrNull() ?: 3,
                accessTokenExpirationSeconds = current.accessTokenExpirationSeconds.toIntOrNull() ?: 3600,
                refreshTokenExpirationSeconds = current.refreshTokenExpirationSeconds.toIntOrNull() ?: 86400,
                accountDeletionDelaySeconds = current.accountDeletionDelaySeconds.toIntOrNull() ?: 604800,
                isRegistrationEnabled = current.isRegistrationEnabled
            )

            val saveResult = saveRemoteAuthSettingsUseCase(settings)
            val nextState = when (saveResult) {
                is AppResult.Success -> {
                    onBack()
                    current.copy(isSaving = false)
                }
                is AppResult.Error -> current.copy(isSaving = false, saveError = saveResult.error)
            }
            _state.value = nextState
        }
    }

    override fun onBackClick() {
        onBack()
    }
}
