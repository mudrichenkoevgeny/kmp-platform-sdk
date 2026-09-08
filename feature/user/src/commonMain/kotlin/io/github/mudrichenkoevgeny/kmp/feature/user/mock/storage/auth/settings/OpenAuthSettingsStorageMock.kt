package io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.settings.OpenAuthSettingsStorage
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.OpenAuthSettings

@InternalApi
class OpenAuthSettingsStorageMock : OpenAuthSettingsStorage {
    var stored: OpenAuthSettings? = null

    override suspend fun getOpenAuthSettings(): OpenAuthSettings? = stored

    override suspend fun updateOpenAuthSettings(openAuthSettings: OpenAuthSettings) {
        stored = openAuthSettings
    }

    override suspend fun clearOpenAuthSettings() {
        stored = null
    }
}
