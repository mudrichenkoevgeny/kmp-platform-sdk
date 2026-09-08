package io.github.mudrichenkoevgeny.kmp.core.settings.mock.repository

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.settings.mock.domain.model.globalsettings.openGlobalSettingsMock
import io.github.mudrichenkoevgeny.kmp.core.settings.repository.OpenGlobalSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.domain.model.globalsettings.OpenGlobalSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@InternalApi
class OpenGlobalSettingsRepositoryMock(
    initialSettings: OpenGlobalSettings? = openGlobalSettingsMock()
) : OpenGlobalSettingsRepository {

    private val _settings = MutableStateFlow(initialSettings)

    var getGlobalSettingsResult: AppResult<OpenGlobalSettings>? = null
    var refreshGlobalSettingsResult: AppResult<OpenGlobalSettings>? = null

    override suspend fun getOpenGlobalSettings(): AppResult<OpenGlobalSettings> {
        val customResult = getGlobalSettingsResult
        if (customResult != null) return customResult
        val current = _settings.value
        return if (current != null) {
            AppResult.Success(current)
        } else {
            val fresh = openGlobalSettingsMock()
            _settings.value = fresh
            AppResult.Success(fresh)
        }
    }

    override suspend fun refreshOpenGlobalSettings(): AppResult<OpenGlobalSettings> {
        val customResult = refreshGlobalSettingsResult
        if (customResult != null) return customResult
        val fresh = openGlobalSettingsMock()
        _settings.value = fresh
        return AppResult.Success(fresh)
    }

    override suspend fun updateOpenGlobalSettings(globalSettings: OpenGlobalSettings) {
        _settings.value = globalSettings
    }

    override fun observeOpenGlobalSettings(): Flow<OpenGlobalSettings?> = _settings.asStateFlow()
}
