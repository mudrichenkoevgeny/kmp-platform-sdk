package io.github.mudrichenkoevgeny.kmp.core.storage.mock.storage.management

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.storage.storage.management.ManagementStorageSettingsStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.storagesettings.ManagementStorageSettings

/**
 * Mock implementation of [ManagementStorageSettingsStorage] for testing.
 */
@InternalApi
public class ManagementStorageSettingsStorageMock(
    public var settings: ManagementStorageSettings? = null
) : ManagementStorageSettingsStorage {

    public override suspend fun getManagementStorageSettings(): ManagementStorageSettings? = settings

    public override suspend fun updateManagementStorageSettings(managementStorageSettings: ManagementStorageSettings) {
        settings = managementStorageSettings
    }

    public override suspend fun clearManagementStorageSettings() {
        settings = null
    }
}
