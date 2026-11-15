package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.storage.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.storage.globalsettings.ManagementGlobalSettingsStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.domain.model.globalsettings.ManagementGlobalSettings

/**
 * Mock implementation of [ManagementGlobalSettingsStorage] for testing.
 */
@InternalApi
public class ManagementGlobalSettingsStorageMock : ManagementGlobalSettingsStorage {
    public var stored: ManagementGlobalSettings? = null

    public override suspend fun getManagementGlobalSettings(): ManagementGlobalSettings? = stored

    public override suspend fun updateManagementGlobalSettings(managementGlobalSettings: ManagementGlobalSettings) {
        stored = managementGlobalSettings
    }

    public override suspend fun clearManagementGlobalSettings() {
        stored = null
    }
}
