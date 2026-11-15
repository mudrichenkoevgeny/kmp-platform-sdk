package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.storage.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.storage.auth.settings.ManagementAuthSettingsStorage
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.ManagementAuthSettings

/**
 * Mock implementation of [ManagementAuthSettingsStorage] for testing.
 */
@InternalApi
public class ManagementAuthSettingsStorageMock : ManagementAuthSettingsStorage {
    public var stored: ManagementAuthSettings? = null

    public override suspend fun getManagementAuthSettings(): ManagementAuthSettings? = stored

    public override suspend fun updateManagementAuthSettings(managementAuthSettings: ManagementAuthSettings) {
        stored = managementAuthSettings
    }

    public override suspend fun clearManagementAuthSettings() {
        stored = null
    }
}
