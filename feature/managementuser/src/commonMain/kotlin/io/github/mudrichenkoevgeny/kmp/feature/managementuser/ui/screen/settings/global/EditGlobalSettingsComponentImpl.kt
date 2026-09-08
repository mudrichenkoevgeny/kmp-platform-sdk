package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.global

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.globalsettings.GetManagementGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.globalsettings.SaveRemoteGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.domain.model.globalsettings.ManagementGlobalSettings
import kotlinx.coroutines.launch

/**
 * Default implementation of [EditGlobalSettingsComponent].
 */
class EditGlobalSettingsComponentImpl(
    componentContext: ComponentContext,
    private val getManagementGlobalSettingsUseCase: GetManagementGlobalSettingsUseCase,
    private val saveRemoteGlobalSettingsUseCase: SaveRemoteGlobalSettingsUseCase,
    private val onBack: () -> Unit
) : EditGlobalSettingsComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()
    private val _state = MutableValue<EditGlobalSettingsScreenState>(EditGlobalSettingsScreenState.Loading)
    override val state: Value<EditGlobalSettingsScreenState> = _state

    init {
        loadSettings()
    }

    override fun onRetry() {
        loadSettings()
    }

    private fun loadSettings() {
        scope.launch {
            _state.value = EditGlobalSettingsScreenState.Loading
            val result = getManagementGlobalSettingsUseCase()
            val nextState = when (result) {
                is AppResult.Success -> {
                    val settings = result.data
                    EditGlobalSettingsScreenState.Content(
                        privacyPolicyUrl = settings.privacyPolicyUrl ?: "",
                        termsOfServiceUrl = settings.termsOfServiceUrl ?: "",
                        contactSupportEmail = settings.contactSupportEmail ?: "",
                        minVersionAndroid = settings.minSupportedAppVersions[ClientType.ANDROID] ?: "",
                        minVersionIos = settings.minSupportedAppVersions[ClientType.IOS] ?: "",
                        minVersionWeb = settings.minSupportedAppVersions[ClientType.WEB] ?: "",
                        minVersionDesktop = settings.minSupportedAppVersions[ClientType.DESKTOP] ?: "",
                        isTracingEnabled = settings.isTracingEnabled,
                        isMetricsEnabled = settings.isMetricsEnabled,
                        isVerboseLoggingEnabled = settings.isVerboseLoggingEnabled
                    )
                }
                is AppResult.Error -> EditGlobalSettingsScreenState.Error(result.error)
            }
            _state.value = nextState
        }
    }

    override fun onPrivacyPolicyUrlChanged(value: String) {
        updateContent { copy(privacyPolicyUrl = value, saveError = null) }
    }

    override fun onTermsOfServiceUrlChanged(value: String) {
        updateContent { copy(termsOfServiceUrl = value, saveError = null) }
    }

    override fun onContactSupportEmailChanged(value: String) {
        updateContent { copy(contactSupportEmail = value, saveError = null) }
    }

    override fun onMinVersionAndroidChanged(value: String) {
        updateContent { copy(minVersionAndroid = value, saveError = null) }
    }

    override fun onMinVersionIosChanged(value: String) {
        updateContent { copy(minVersionIos = value, saveError = null) }
    }

    override fun onMinVersionWebChanged(value: String) {
        updateContent { copy(minVersionWeb = value, saveError = null) }
    }

    override fun onMinVersionDesktopChanged(value: String) {
        updateContent { copy(minVersionDesktop = value, saveError = null) }
    }

    override fun onTracingEnabledToggled(enabled: Boolean) {
        updateContent { copy(isTracingEnabled = enabled, saveError = null) }
    }

    override fun onMetricsEnabledToggled(enabled: Boolean) {
        updateContent { copy(isMetricsEnabled = enabled, saveError = null) }
    }

    override fun onVerboseLoggingEnabledToggled(enabled: Boolean) {
        updateContent { copy(isVerboseLoggingEnabled = enabled, saveError = null) }
    }

    private inline fun updateContent(transform: EditGlobalSettingsScreenState.Content.() -> EditGlobalSettingsScreenState.Content) {
        val current = _state.value as? EditGlobalSettingsScreenState.Content ?: return
        _state.value = current.transform()
    }

    override fun onSaveClick() {
        val current = _state.value as? EditGlobalSettingsScreenState.Content ?: return
        scope.launch {
            _state.value = current.copy(isSaving = true, saveError = null)

            val minVersions = buildMap {
                if (current.minVersionAndroid.isNotBlank()) put(ClientType.ANDROID, current.minVersionAndroid)
                if (current.minVersionIos.isNotBlank()) put(ClientType.IOS, current.minVersionIos)
                if (current.minVersionWeb.isNotBlank()) put(ClientType.WEB, current.minVersionWeb)
                if (current.minVersionDesktop.isNotBlank()) put(ClientType.DESKTOP, current.minVersionDesktop)
            }

            val settings = ManagementGlobalSettings(
                privacyPolicyUrl = current.privacyPolicyUrl.ifBlank { null },
                termsOfServiceUrl = current.termsOfServiceUrl.ifBlank { null },
                contactSupportEmail = current.contactSupportEmail.ifBlank { null },
                maintenanceUntilEpochMillis = null,
                minSupportedAppVersions = minVersions,
                isTracingEnabled = current.isTracingEnabled,
                isMetricsEnabled = current.isMetricsEnabled,
                isVerboseLoggingEnabled = current.isVerboseLoggingEnabled
            )

            val saveResult = saveRemoteGlobalSettingsUseCase(settings)
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
