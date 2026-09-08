package io.github.mudrichenkoevgeny.kmp.core.settings.storage.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.storage.EncryptedSettingsMock
import io.github.mudrichenkoevgeny.kmp.core.settings.mock.domain.model.globalsettings.openGlobalSettingsMock
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
        val settings = openGlobalSettingsMock(
            privacy = "https://privacy.example",
            terms = "https://terms.example",
            email = "support@example.com"
        )

        storage.updateOpenGlobalSettings(settings)

        assertEquals(settings, storage.getOpenGlobalSettings())
    }

    @Test
    fun clearOpenGlobalSettings_removesSnapshot() = runTest {
        val storage = EncryptedOpenGlobalSettingsStorage(EncryptedSettingsMock())
        val settings = openGlobalSettingsMock(
            privacy = "u",
            terms = null,
            email = null
        )

        storage.updateOpenGlobalSettings(settings)
        storage.clearOpenGlobalSettings()

        assertNull(storage.getOpenGlobalSettings())
    }
}
