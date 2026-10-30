package io.github.mudrichenkoevgeny.kmp.core.storage.storage.storagesettings.management

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.storage.EncryptedSettingsMock
import io.github.mudrichenkoevgeny.kmp.core.storage.storage.management.EncryptedManagementStorageSettingsStorage
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.storage.domain.model.storagesettings.createTestManagementStorageSettings
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@InternalApi
class EncryptedManagementStorageSettingsStorageTest {

    @Test
    fun getManagementStorageSettings_returnsNullWhenNotPersisted() = runTest {
        val storage = EncryptedManagementStorageSettingsStorage(EncryptedSettingsMock())

        assertNull(storage.getManagementStorageSettings())
    }

    @Test
    fun updateManagementStorageSettings_persistsAndGetReturnsSameValues() = runTest {
        val storage = EncryptedManagementStorageSettingsStorage(EncryptedSettingsMock())
        val settings = createTestManagementStorageSettings()

        storage.updateManagementStorageSettings(settings)

        assertEquals(settings, storage.getManagementStorageSettings())
    }

    @Test
    fun clearManagementStorageSettings_removesSnapshot() = runTest {
        val storage = EncryptedManagementStorageSettingsStorage(EncryptedSettingsMock())
        val settings = createTestManagementStorageSettings()

        storage.updateManagementStorageSettings(settings)
        storage.clearManagementStorageSettings()

        assertNull(storage.getManagementStorageSettings())
    }
}
