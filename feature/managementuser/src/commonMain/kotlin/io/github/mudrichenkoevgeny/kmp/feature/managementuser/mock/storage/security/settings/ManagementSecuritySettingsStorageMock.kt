package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.storage.security.settings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.storage.security.settings.ManagementSecuritySettingsStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.securitysettings.ManagementSecuritySettings

@InternalApi
class ManagementSecuritySettingsStorageMock : ManagementSecuritySettingsStorage {
    var stored: ManagementSecuritySettings? = null

    override suspend fun getManagementSecuritySettings(): ManagementSecuritySettings? = stored

    override suspend fun updateManagementSecuritySettings(managementSecuritySettings: ManagementSecuritySettings) {
        stored = managementSecuritySettings
    }

    override suspend fun clearManagementSecuritySettings() {
        stored = null
    }
}
