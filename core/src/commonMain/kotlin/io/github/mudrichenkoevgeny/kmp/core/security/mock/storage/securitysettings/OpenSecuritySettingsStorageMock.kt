package io.github.mudrichenkoevgeny.kmp.core.security.mock.storage.securitysettings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.security.storage.securitysettings.OpenSecuritySettingsStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.securitysettings.OpenSecuritySettings

/**
 * Mock implementation of [OpenSecuritySettingsStorage] for testing purposes.
 */
@InternalApi
public class OpenSecuritySettingsStorageMock : OpenSecuritySettingsStorage {
    public var stored: OpenSecuritySettings? = null

    public override suspend fun getOpenSecuritySettings(): OpenSecuritySettings? = stored

    public override suspend fun updateOpenSecuritySettings(securitySettings: OpenSecuritySettings) {
        stored = securitySettings
    }

    public override suspend fun clearOpenSecuritySettings() {
        stored = null
    }
}
