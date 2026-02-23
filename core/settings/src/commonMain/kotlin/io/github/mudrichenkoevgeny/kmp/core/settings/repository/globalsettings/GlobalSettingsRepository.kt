package io.github.mudrichenkoevgeny.kmp.core.settings.repository.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.settings.model.globalsettings.GlobalSettings
import kotlinx.coroutines.flow.Flow

interface GlobalSettingsRepository {
    suspend fun getGlobalSettings(): AppResult<GlobalSettings>
    suspend fun refreshGlobalSettings(): AppResult<GlobalSettings>
    suspend fun updateGlobalSettings(globalSettings: GlobalSettings)
    fun observeGlobalSettings(): Flow<GlobalSettings?>
}