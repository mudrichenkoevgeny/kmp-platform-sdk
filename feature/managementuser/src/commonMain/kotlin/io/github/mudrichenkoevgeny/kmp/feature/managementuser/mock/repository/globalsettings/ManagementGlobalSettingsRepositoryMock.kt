package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.globalsettings.ManagementGlobalSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.domain.model.globalsettings.ManagementGlobalSettings
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.settings.domain.model.globalsettings.createTestManagementGlobalSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Mock implementation of [ManagementGlobalSettingsRepository] for testing.
 */
@InternalApi
public class ManagementGlobalSettingsRepositoryMock(
    initialSettings: ManagementGlobalSettings? = createTestManagementGlobalSettings()
) : ManagementGlobalSettingsRepository {

    private val settingsStateFlow = MutableStateFlow(initialSettings)

    public var getManagementGlobalSettingsResult: AppResult<ManagementGlobalSettings>? = null
    public var refreshManagementGlobalSettingsResult: AppResult<ManagementGlobalSettings>? = null
    public var saveRemoteGlobalSettingsResult: AppResult<ManagementGlobalSettings>? = null
    public var resetRemoteGlobalSettingsResult: AppResult<ManagementGlobalSettings>? = null

    public override suspend fun getManagementGlobalSettings(): AppResult<ManagementGlobalSettings> {
        val customResult = getManagementGlobalSettingsResult
        if (customResult != null) {
            return customResult
        }
        val current = settingsStateFlow.value
        return if (current != null) {
            AppResult.Success(current)
        } else {
            val fresh = createTestManagementGlobalSettings()
            settingsStateFlow.value = fresh
            AppResult.Success(fresh)
        }
    }

    public override suspend fun saveRemoteManagementGlobalSettings(
        globalSettings: ManagementGlobalSettings
    ): AppResult<ManagementGlobalSettings> {
        val customResult = saveRemoteGlobalSettingsResult
        if (customResult != null) {
            return customResult
        }
        settingsStateFlow.value = globalSettings
        return AppResult.Success(globalSettings)
    }

    public override suspend fun refreshManagementGlobalSettings(): AppResult<ManagementGlobalSettings> {
        val customResult = refreshManagementGlobalSettingsResult
        if (customResult != null) {
            return customResult
        }
        return getManagementGlobalSettings()
    }

    public override suspend fun updateManagementGlobalSettings(globalSettings: ManagementGlobalSettings) {
        settingsStateFlow.value = globalSettings
    }

    public override suspend fun resetRemoteManagementGlobalSettings(): AppResult<ManagementGlobalSettings> {
        val customResult = resetRemoteGlobalSettingsResult
        if (customResult != null) {
            return customResult
        }
        return getManagementGlobalSettings()
    }

    public override fun observeManagementGlobalSettings(): Flow<ManagementGlobalSettings?> =
        settingsStateFlow.asStateFlow()
}
