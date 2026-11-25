package io.github.mudrichenkoevgeny.kmp.core.storage.storage.storagesettings.open

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.storage.EncryptedSettingsMock
import io.github.mudrichenkoevgeny.kmp.core.storage.storage.open.EncryptedOpenStorageSettingsStorage
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.storage.domain.model.storagesettings.createTestOpenStorageSettings
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@InternalApi
class EncryptedOpenStorageSettingsStorageTest {

    @Test
    fun getOpenStorageSettings_returnsNullWhenNotPersisted() = runTest {
        val storage = EncryptedOpenStorageSettingsStorage(EncryptedSettingsMock())

        assertNull(storage.getOpenStorageSettings())
    }

    @Test
    fun updateOpenStorageSettings_persistsAndGetOpenStorageSettingsReturnsSameValues() = runTest {
        val storage = EncryptedOpenStorageSettingsStorage(EncryptedSettingsMock())
        val settings = createTestOpenStorageSettings()

        storage.updateOpenStorageSettings(settings)

        assertEquals(settings, storage.getOpenStorageSettings())
    }

    @Test
    fun clearOpenStorageSettings_removesSnapshot() = runTest {
        val storage = EncryptedOpenStorageSettingsStorage(EncryptedSettingsMock())
        val settings = createTestOpenStorageSettings()

        storage.updateOpenStorageSettings(settings)
        storage.clearOpenStorageSettings()

        assertNull(storage.getOpenStorageSettings())
    }
}
