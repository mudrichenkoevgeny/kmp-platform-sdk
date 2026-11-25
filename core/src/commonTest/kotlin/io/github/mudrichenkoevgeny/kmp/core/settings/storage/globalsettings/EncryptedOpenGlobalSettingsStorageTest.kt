package io.github.mudrichenkoevgeny.kmp.core.settings.storage.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.storage.EncryptedSettingsMock
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.settings.domain.model.globalsettings.createTestOpenGlobalSettings
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@InternalApi
class EncryptedOpenGlobalSettingsStorageTest {

    @Test
    fun getOpenGlobalSettings_returnsNullWhenNotPersisted() = runTest {
        val storage = EncryptedOpenGlobalSettingsStorage(EncryptedSettingsMock())

        assertNull(storage.getOpenGlobalSettings())
    }

    @Test
    fun updateOpenGlobalSettings_persistsAndGetOpenGlobalSettingsReturnsSameValues() = runTest {
        val storage = EncryptedOpenGlobalSettingsStorage(EncryptedSettingsMock())
        val settings = createTestOpenGlobalSettings()

        storage.updateOpenGlobalSettings(settings)

        assertEquals(settings, storage.getOpenGlobalSettings())
    }

    @Test
    fun clearOpenGlobalSettings_removesSnapshot() = runTest {
        val storage = EncryptedOpenGlobalSettingsStorage(EncryptedSettingsMock())
        val settings = createTestOpenGlobalSettings()

        storage.updateOpenGlobalSettings(settings)
        storage.clearOpenGlobalSettings()

        assertNull(storage.getOpenGlobalSettings())
    }
}
