package io.github.mudrichenkoevgeny.kmp.core.security.storage.securitysettings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.storage.EncryptedSettingsMock
import io.github.mudrichenkoevgeny.kmp.core.security.mock.domain.model.openSecuritySettingsMock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@InternalApi
class EncryptedOpenSecuritySettingsStorageTest {

    @Test
    fun getOpenSecuritySettings_returnsNullWhenNotPersisted() = runTest {
        val storage = EncryptedOpenSecuritySettingsStorage(EncryptedSettingsMock())

        assertNull(storage.getOpenSecuritySettings())
    }

    @Test
    fun updateOpenSecuritySettings_persistsAndGetOpenSecuritySettingsReturnsSameValues() = runTest {
        val storage = EncryptedOpenSecuritySettingsStorage(EncryptedSettingsMock())
        val settings = openSecuritySettingsMock()

        storage.updateOpenSecuritySettings(settings)

        assertEquals(settings, storage.getOpenSecuritySettings())
    }

    @Test
    fun clearOpenSecuritySettings_removesSnapshot() = runTest {
        val storage = EncryptedOpenSecuritySettingsStorage(EncryptedSettingsMock())
        val settings = openSecuritySettingsMock()

        storage.updateOpenSecuritySettings(settings)
        storage.clearOpenSecuritySettings()

        assertNull(storage.getOpenSecuritySettings())
    }
}
