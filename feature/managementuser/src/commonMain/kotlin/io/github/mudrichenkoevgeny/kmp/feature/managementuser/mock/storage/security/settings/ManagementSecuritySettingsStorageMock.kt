package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.storage.security.settings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.storage.security.settings.ManagementSecuritySettingsStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.securitysettings.ManagementSecuritySettings

/**
 * Mock implementation of [ManagementSecuritySettingsStorage] for testing.
 */
@InternalApi
public class ManagementSecuritySettingsStorageMock : ManagementSecuritySettingsStorage {
    public var stored: ManagementSecuritySettings? = null

    public override suspend fun getManagementSecuritySettings(): ManagementSecuritySettings? = stored

    public override suspend fun updateManagementSecuritySettings(managementSecuritySettings: ManagementSecuritySettings) {
        stored = managementSecuritySettings
    }

    public override suspend fun clearManagementSecuritySettings() {
        stored = null
    }
}
