package io.github.mudrichenkoevgeny.kmp.core.storage.mock.storage.storagesettings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.storage.storage.storagesettings.OpenStorageSettingsStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.storagesettings.OpenStorageSettings

@InternalApi
class OpenStorageSettingsStorageMock : OpenStorageSettingsStorage {
    var stored: OpenStorageSettings? = null

    override suspend fun getOpenStorageSettings(): OpenStorageSettings? = stored

    override suspend fun updateOpenStorageSettings(storageSettings: OpenStorageSettings) {
        stored = storageSettings
    }

    override suspend fun clearOpenStorageSettings() {
        stored = null
    }
}
