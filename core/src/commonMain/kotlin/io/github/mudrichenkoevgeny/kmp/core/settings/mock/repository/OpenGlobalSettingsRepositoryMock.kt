package io.github.mudrichenkoevgeny.kmp.core.settings.mock.repository

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.settings.repository.OpenGlobalSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.domain.model.globalsettings.OpenGlobalSettings
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.settings.domain.model.globalsettings.createTestOpenGlobalSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Mock implementation of [OpenGlobalSettingsRepository] for testing purposes.
 */
@InternalApi
public class OpenGlobalSettingsRepositoryMock(
    initialSettings: OpenGlobalSettings? = createTestOpenGlobalSettings()
) : OpenGlobalSettingsRepository {

    private val settingsStateFlow = MutableStateFlow(initialSettings)

    public var refreshCallCount: Int = 0
        private set

    public var getGlobalSettingsResult: AppResult<OpenGlobalSettings>? = null
    public var refreshGlobalSettingsResult: AppResult<OpenGlobalSettings>? = null

    override suspend fun getOpenGlobalSettings(): AppResult<OpenGlobalSettings> {
        val customResult = getGlobalSettingsResult
        if (customResult != null) {
            return customResult
        }
        val current = settingsStateFlow.value
        return if (current != null) {
            AppResult.Success(current)
        } else {
            val fresh = createTestOpenGlobalSettings()
            settingsStateFlow.value = fresh
            AppResult.Success(fresh)
        }
    }

    override suspend fun refreshOpenGlobalSettings(): AppResult<OpenGlobalSettings> {
        refreshCallCount++
        val customResult = refreshGlobalSettingsResult
        if (customResult != null) {
            return customResult
        }
        val fresh = createTestOpenGlobalSettings()
        settingsStateFlow.value = fresh
        return AppResult.Success(fresh)
    }

    override suspend fun updateOpenGlobalSettings(globalSettings: OpenGlobalSettings) {
        settingsStateFlow.value = globalSettings
    }

    override fun observeOpenGlobalSettings(): Flow<OpenGlobalSettings?> = settingsStateFlow.asStateFlow()
}
