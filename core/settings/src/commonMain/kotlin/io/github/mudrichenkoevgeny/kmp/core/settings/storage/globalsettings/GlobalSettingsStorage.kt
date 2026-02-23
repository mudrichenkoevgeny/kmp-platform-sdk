package io.github.mudrichenkoevgeny.kmp.core.settings.storage.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.settings.model.globalsettings.GlobalSettings

interface GlobalSettingsStorage {
    suspend fun getGlobalSettings(): GlobalSettings?
    suspend fun updateGlobalSettings(globalSettings: GlobalSettings)
    suspend fun clearGlobalSettings()
}