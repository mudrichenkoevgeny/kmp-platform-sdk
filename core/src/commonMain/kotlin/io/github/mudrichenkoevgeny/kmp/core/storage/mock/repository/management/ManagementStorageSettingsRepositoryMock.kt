package io.github.mudrichenkoevgeny.kmp.core.storage.mock.repository.management

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.repository.management.ManagementStorageSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.storagesettings.ManagementStorageSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.mapper.storagesettings.toManagementStorageSettings
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.storage.network.model.storagesettings.createTestManagementStorageSettingsPayload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Mock implementation of [ManagementStorageSettingsRepository] for testing.
 */
@InternalApi
public class ManagementStorageSettingsRepositoryMock(
    public var initialSettings: ManagementStorageSettings =
        createTestManagementStorageSettingsPayload().toManagementStorageSettings()
) : ManagementStorageSettingsRepository {

    private val settingsStateFlow = MutableStateFlow<ManagementStorageSettings?>(initialSettings)

    public override suspend fun getManagementStorageSettings(): AppResult<ManagementStorageSettings> =
        AppResult.Success(
            settingsStateFlow.value ?: initialSettings
        )

    public override suspend fun saveRemoteManagementStorageSettings(
        storageSettings: ManagementStorageSettings
    ): AppResult<ManagementStorageSettings> {
        settingsStateFlow.value = storageSettings
        return AppResult.Success(storageSettings)
    }

    public override suspend fun resetRemoteManagementStorageSettings(): AppResult<ManagementStorageSettings> {
        val defaultSettings = createTestManagementStorageSettingsPayload().toManagementStorageSettings()
        settingsStateFlow.value = defaultSettings
        return AppResult.Success(defaultSettings)
    }

    public override suspend fun refreshManagementStorageSettings(): AppResult<ManagementStorageSettings> =
        AppResult.Success(
            settingsStateFlow.value ?: initialSettings
        )

    public override suspend fun updateManagementStorageSettings(storageSettings: ManagementStorageSettings) {
        settingsStateFlow.value = storageSettings
    }

    public override fun observeManagementStorageSettings(): Flow<ManagementStorageSettings?> =
        settingsStateFlow.asStateFlow()
}
