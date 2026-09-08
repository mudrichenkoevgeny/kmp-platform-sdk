package io.github.mudrichenkoevgeny.kmp.core.settings.mock.storage.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.settings.storage.globalsettings.OpenGlobalSettingsStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.domain.model.globalsettings.OpenGlobalSettings

@InternalApi
class OpenGlobalSettingsStorageMock : OpenGlobalSettingsStorage {
    var stored: OpenGlobalSettings? = null

    override suspend fun getOpenGlobalSettings(): OpenGlobalSettings? = stored

    override suspend fun updateOpenGlobalSettings(globalSettings: OpenGlobalSettings) {
        stored = globalSettings
    }

    override suspend fun clearOpenGlobalSettings() {
        stored = null
    }
}
