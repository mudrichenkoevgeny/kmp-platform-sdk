package io.github.mudrichenkoevgeny.kmp.core.security.mock.storage.securitysettings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.security.storage.securitysettings.SecuritySettingsStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.securitysettings.SecuritySettings

@InternalApi
class SecuritySettingsStorageMock : SecuritySettingsStorage {
    var stored: SecuritySettings? = null

    override suspend fun getSecuritySettings(): SecuritySettings? = stored

    override suspend fun updateSecuritySettings(securitySettings: SecuritySettings) {
        stored = securitySettings
    }

    override suspend fun clearSecuritySettings() {
        stored = null
    }
}