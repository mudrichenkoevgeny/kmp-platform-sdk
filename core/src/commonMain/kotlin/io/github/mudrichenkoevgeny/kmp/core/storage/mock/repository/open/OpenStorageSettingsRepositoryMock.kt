package io.github.mudrichenkoevgeny.kmp.core.storage.mock.repository.open

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.repository.open.OpenStorageSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.storagesettings.OpenStorageSettings
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.storage.domain.model.storagesettings.createTestOpenStorageSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Mock implementation of [OpenStorageSettingsRepository] for testing.
 */
@InternalApi
public class OpenStorageSettingsRepositoryMock(
    public var initialSettings: OpenStorageSettings = createTestOpenStorageSettings()
) : OpenStorageSettingsRepository {

    private val settingsStateFlow = MutableStateFlow<OpenStorageSettings?>(initialSettings)

    public override suspend fun getOpenStorageSettings(): AppResult<OpenStorageSettings> = AppResult.Success(
        settingsStateFlow.value ?: initialSettings
    )

    public override suspend fun refreshOpenStorageSettings(): AppResult<OpenStorageSettings> = AppResult.Success(
        settingsStateFlow.value ?: initialSettings
    )

    public override suspend fun updateOpenStorageSettings(openStorageSettings: OpenStorageSettings) {
        settingsStateFlow.value = openStorageSettings
    }

    public override fun observeOpenStorageSettings(): Flow<OpenStorageSettings?> = settingsStateFlow.asStateFlow()
}
