package io.github.mudrichenkoevgeny.kmp.core.storage.mock.repository.open

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.repository.open.OpenStorageSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.storagesettings.OpenStorageSettings
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.storage.domain.model.storagesettings.createTestOpenStorageSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@InternalApi
class OpenStorageSettingsRepositoryMock(
    var initialSettings: OpenStorageSettings = createTestOpenStorageSettings()
) : OpenStorageSettingsRepository {

    private val _settings = MutableStateFlow<OpenStorageSettings?>(initialSettings)

    override suspend fun getOpenStorageSettings(): AppResult<OpenStorageSettings> {
        return AppResult.Success(_settings.value ?: initialSettings)
    }

    override suspend fun refreshOpenStorageSettings(): AppResult<OpenStorageSettings> {
        return AppResult.Success(_settings.value ?: initialSettings)
    }

    override suspend fun updateOpenStorageSettings(openStorageSettings: OpenStorageSettings) {
        _settings.value = openStorageSettings
    }

    override fun observeOpenStorageSettings(): Flow<OpenStorageSettings?> = _settings.asStateFlow()
}
