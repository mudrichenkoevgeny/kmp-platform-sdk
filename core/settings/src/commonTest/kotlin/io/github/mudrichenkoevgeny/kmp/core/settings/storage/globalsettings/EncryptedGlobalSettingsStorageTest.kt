package io.github.mudrichenkoevgeny.kmp.core.settings.storage.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.mock.storage.MockEncryptedSettings
import io.github.mudrichenkoevgeny.kmp.core.settings.model.globalsettings.GlobalSettings
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class EncryptedGlobalSettingsStorageTest {

    @Test
    fun `getGlobalSettings returns null when not persisted`() = runTest {
        val storage = EncryptedGlobalSettingsStorage(MockEncryptedSettings())

        assertNull(storage.getGlobalSettings())
    }

    @Test
    fun `updateGlobalSettings persists and getGlobalSettings returns same values`() = runTest {
        val storage = EncryptedGlobalSettingsStorage(MockEncryptedSettings())
        val settings = GlobalSettings(
            privacyPolicyUrl = "https://privacy.example",
            termsOfServiceUrl = "https://terms.example",
            contactSupportEmail = "support@example.com"
        )

        storage.updateGlobalSettings(settings)

        assertEquals(settings, storage.getGlobalSettings())
    }

    @Test
    fun `clearGlobalSettings removes snapshot`() = runTest {
        val storage = EncryptedGlobalSettingsStorage(MockEncryptedSettings())
        val settings = GlobalSettings(
            privacyPolicyUrl = "u",
            termsOfServiceUrl = null,
            contactSupportEmail = null
        )

        storage.updateGlobalSettings(settings)
        storage.clearGlobalSettings()

        assertNull(storage.getGlobalSettings())
    }
}
