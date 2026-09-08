package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.storage.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.storage.auth.settings.ManagementAuthSettingsStorage
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.ManagementAuthSettings

@InternalApi
class ManagementAuthSettingsStorageMock : ManagementAuthSettingsStorage {
    var stored: ManagementAuthSettings? = null

    override suspend fun getManagementAuthSettings(): ManagementAuthSettings? = stored

    override suspend fun updateManagementAuthSettings(managementAuthSettings: ManagementAuthSettings) {
        stored = managementAuthSettings
    }

    override suspend fun clearManagementAuthSettings() {
        stored = null
    }
}
