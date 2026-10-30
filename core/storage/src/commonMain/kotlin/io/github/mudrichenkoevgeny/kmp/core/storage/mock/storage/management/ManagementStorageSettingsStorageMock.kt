package io.github.mudrichenkoevgeny.kmp.core.storage.mock.storage.management

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.storage.storage.management.ManagementStorageSettingsStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.storagesettings.ManagementStorageSettings

@InternalApi
class ManagementStorageSettingsStorageMock(
    var settings: ManagementStorageSettings? = null
) : ManagementStorageSettingsStorage {

    override suspend fun getManagementStorageSettings(): ManagementStorageSettings? = settings

    override suspend fun updateManagementStorageSettings(managementStorageSettings: ManagementStorageSettings) {
        settings = managementStorageSettings
    }

    override suspend fun clearManagementStorageSettings() {
        settings = null
    }
}
