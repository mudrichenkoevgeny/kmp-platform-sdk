package io.github.mudrichenkoevgeny.kmp.core.settings.mock.storage.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.settings.storage.globalsettings.OpenGlobalSettingsStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.domain.model.globalsettings.OpenGlobalSettings

/**
 * Mock implementation of [OpenGlobalSettingsStorage] for testing purposes.
 */
@InternalApi
public class OpenGlobalSettingsStorageMock : OpenGlobalSettingsStorage {
    public var stored: OpenGlobalSettings? = null

    public override suspend fun getOpenGlobalSettings(): OpenGlobalSettings? = stored

    public override suspend fun updateOpenGlobalSettings(globalSettings: OpenGlobalSettings) {
        stored = globalSettings
    }

    public override suspend fun clearOpenGlobalSettings() {
        stored = null
    }
}
