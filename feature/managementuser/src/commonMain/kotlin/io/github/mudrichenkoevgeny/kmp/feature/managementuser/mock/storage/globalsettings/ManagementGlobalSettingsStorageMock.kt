package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.storage.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.storage.globalsettings.ManagementGlobalSettingsStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.domain.model.globalsettings.ManagementGlobalSettings

@InternalApi
class ManagementGlobalSettingsStorageMock : ManagementGlobalSettingsStorage {
    var stored: ManagementGlobalSettings? = null

    override suspend fun getManagementGlobalSettings(): ManagementGlobalSettings? = stored

    override suspend fun updateManagementGlobalSettings(managementGlobalSettings: ManagementGlobalSettings) {
        stored = managementGlobalSettings
    }

    override suspend fun clearManagementGlobalSettings() {
        stored = null
    }
}
