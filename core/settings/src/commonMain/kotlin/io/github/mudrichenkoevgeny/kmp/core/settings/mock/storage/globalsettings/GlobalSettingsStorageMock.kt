package io.github.mudrichenkoevgeny.kmp.core.settings.mock.storage.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.settings.storage.globalsettings.GlobalSettingsStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.domain.model.globalsettings.GlobalSettings

@InternalApi
class GlobalSettingsStorageMock : GlobalSettingsStorage {
    var stored: GlobalSettings? = null

    override suspend fun getGlobalSettings(): GlobalSettings? = stored

    override suspend fun updateGlobalSettings(globalSettings: GlobalSettings) {
        stored = globalSettings
    }

    override suspend fun clearGlobalSettings() {
        stored = null
    }
}