package io.github.mudrichenkoevgeny.kmp.core.storage.mock.repository.management

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.repository.management.ManagementStorageSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.storagesettings.ManagementStorageSettings
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.storage.network.model.storagesettings.createTestManagementStorageSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.mapper.storagesettings.toManagementStorageSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@InternalApi
class ManagementStorageSettingsRepositoryMock(
    var initialSettings: ManagementStorageSettings = createTestManagementStorageSettingsPayload().toManagementStorageSettings()
) : ManagementStorageSettingsRepository {

    private val _settings = MutableStateFlow<ManagementStorageSettings?>(initialSettings)

    override suspend fun getManagementStorageSettings(): AppResult<ManagementStorageSettings> {
        return AppResult.Success(_settings.value ?: initialSettings)
    }

    override suspend fun saveRemoteManagementStorageSettings(storageSettings: ManagementStorageSettings): AppResult<ManagementStorageSettings> {
        _settings.value = storageSettings
        return AppResult.Success(storageSettings)
    }

    override suspend fun resetRemoteManagementStorageSettings(): AppResult<ManagementStorageSettings> {
        val defaultSettings = createTestManagementStorageSettingsPayload().toManagementStorageSettings()
        _settings.value = defaultSettings
        return AppResult.Success(defaultSettings)
    }

    override suspend fun refreshManagementStorageSettings(): AppResult<ManagementStorageSettings> {
        return AppResult.Success(_settings.value ?: initialSettings)
    }

    override suspend fun updateManagementStorageSettings(storageSettings: ManagementStorageSettings) {
        _settings.value = storageSettings
    }

    override fun observeManagementStorageSettings(): Flow<ManagementStorageSettings?> = _settings.asStateFlow()
}
