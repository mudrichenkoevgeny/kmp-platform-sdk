package io.github.mudrichenkoevgeny.kmp.core.storage.mock.repository

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.repository.settings.OpenStorageSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.storagesettings.OpenStorageSettings
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.storage.domain.model.storagesettings.createTestOpenStorageSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@InternalApi
class OpenStorageSettingsRepositoryMock(
    initialSettings: OpenStorageSettings? = createTestOpenStorageSettings()
) : OpenStorageSettingsRepository {

    private val _settings = MutableStateFlow(initialSettings)

    var getStorageSettingsResult: AppResult<OpenStorageSettings>? = null
    var refreshStorageSettingsResult: AppResult<OpenStorageSettings>? = null

    override suspend fun getOpenStorageSettings(): AppResult<OpenStorageSettings> {
        val customResult = getStorageSettingsResult
        if (customResult != null) {
            return customResult
        }
        val current = _settings.value
        return if (current != null) {
            AppResult.Success(current)
        } else {
            val fresh = createTestOpenStorageSettings()
            _settings.value = fresh
            AppResult.Success(fresh)
        }
    }

    override suspend fun refreshOpenStorageSettings(): AppResult<OpenStorageSettings> {
        val customResult = refreshStorageSettingsResult
        if (customResult != null) {
            return customResult
        }
        val fresh = createTestOpenStorageSettings()
        _settings.value = fresh
        return AppResult.Success(fresh)
    }

    override suspend fun updateOpenStorageSettings(storageSettings: OpenStorageSettings) {
        _settings.value = storageSettings
    }

    override fun observeOpenStorageSettings(): Flow<OpenStorageSettings?> = _settings.asStateFlow()
}
