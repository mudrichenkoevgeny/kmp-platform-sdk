package io.github.mudrichenkoevgeny.kmp.core.security.mock.storage.securitysettings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.security.storage.securitysettings.OpenSecuritySettingsStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.securitysettings.OpenSecuritySettings

@InternalApi
class OpenSecuritySettingsStorageMock : OpenSecuritySettingsStorage {
    var stored: OpenSecuritySettings? = null

    override suspend fun getOpenSecuritySettings(): OpenSecuritySettings? = stored

    override suspend fun updateOpenSecuritySettings(securitySettings: OpenSecuritySettings) {
        stored = securitySettings
    }

    override suspend fun clearOpenSecuritySettings() {
        stored = null
    }
}
