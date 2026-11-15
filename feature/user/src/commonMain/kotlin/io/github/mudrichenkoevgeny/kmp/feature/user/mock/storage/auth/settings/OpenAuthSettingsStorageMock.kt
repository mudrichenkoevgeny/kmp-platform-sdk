package io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.settings.OpenAuthSettingsStorage
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.OpenAuthSettings

/**
 * Mock implementation of [OpenAuthSettingsStorage] for testing.
 */
@InternalApi
public class OpenAuthSettingsStorageMock : OpenAuthSettingsStorage {
    public var stored: OpenAuthSettings? = null

    public override suspend fun getOpenAuthSettings(): OpenAuthSettings? = stored

    public override suspend fun updateOpenAuthSettings(openAuthSettings: OpenAuthSettings) {
        stored = openAuthSettings
    }

    public override suspend fun clearOpenAuthSettings() {
        stored = null
    }
}
