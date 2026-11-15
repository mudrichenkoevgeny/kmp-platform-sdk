package io.github.mudrichenkoevgeny.kmp.core.storage.mock.storage.open

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.storage.storage.open.OpenStorageSettingsStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.storagesettings.OpenStorageSettings

/**
 * Mock implementation of [OpenStorageSettingsStorage] for testing.
 */
@InternalApi
public class OpenStorageSettingsStorageMock : OpenStorageSettingsStorage {
    public var stored: OpenStorageSettings? = null

    public override suspend fun getOpenStorageSettings(): OpenStorageSettings? = stored

    public override suspend fun updateOpenStorageSettings(openStorageSettings: OpenStorageSettings) {
        stored = openStorageSettings
    }

    public override suspend fun clearOpenStorageSettings() {
        stored = null
    }
}
