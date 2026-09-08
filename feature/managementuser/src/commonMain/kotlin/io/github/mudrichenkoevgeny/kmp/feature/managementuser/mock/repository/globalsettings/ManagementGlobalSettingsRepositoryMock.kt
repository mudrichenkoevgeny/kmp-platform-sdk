package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.settings.mock.domain.model.globalsettings.managementGlobalSettingsMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.globalsettings.ManagementGlobalSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.domain.model.globalsettings.ManagementGlobalSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@InternalApi
class ManagementGlobalSettingsRepositoryMock(
    initialSettings: ManagementGlobalSettings? = managementGlobalSettingsMock()
) : ManagementGlobalSettingsRepository {

    private val _settings = MutableStateFlow(initialSettings)

    var getManagementGlobalSettingsResult: AppResult<ManagementGlobalSettings>? = null
    var refreshManagementGlobalSettingsResult: AppResult<ManagementGlobalSettings>? = null
    var saveRemoteGlobalSettingsResult: AppResult<Unit>? = null

    override suspend fun getManagementGlobalSettings(): AppResult<ManagementGlobalSettings> {
        val customResult = getManagementGlobalSettingsResult
        if (customResult != null) return customResult
        val current = _settings.value
        return if (current != null) {
            AppResult.Success(current)
        } else {
            val fresh = managementGlobalSettingsMock()
            _settings.value = fresh
            AppResult.Success(fresh)
        }
    }

    override suspend fun saveRemoteManagementGlobalSettings(globalSettings: ManagementGlobalSettings): AppResult<Unit> {
        val customResult = saveRemoteGlobalSettingsResult
        if (customResult != null) return customResult
        _settings.value = globalSettings
        return AppResult.Success(Unit)
    }

    override suspend fun refreshManagementGlobalSettings(): AppResult<ManagementGlobalSettings> {
        val customResult = refreshManagementGlobalSettingsResult
        if (customResult != null) return customResult
        return getManagementGlobalSettings()
    }

    override suspend fun updateManagementGlobalSettings(globalSettings: ManagementGlobalSettings) {
        _settings.value = globalSettings
    }

    override fun observeManagementGlobalSettings(): Flow<ManagementGlobalSettings?> = _settings.asStateFlow()
}
