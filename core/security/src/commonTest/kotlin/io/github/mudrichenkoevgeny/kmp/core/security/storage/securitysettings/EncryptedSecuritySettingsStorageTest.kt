package io.github.mudrichenkoevgeny.kmp.core.security.storage.securitysettings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.storage.EncryptedSettingsMock
import io.github.mudrichenkoevgeny.kmp.core.security.mock.domain.model.securitySettingsMock
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.passwordpolicy.PasswordPolicy
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.securitysettings.SecuritySettings
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@InternalApi
class EncryptedSecuritySettingsStorageTest {

    @Test
    fun `getSecuritySettings returns null when not persisted`() = runTest {
        val storage = EncryptedSecuritySettingsStorage(EncryptedSettingsMock())

        assertNull(storage.getSecuritySettings())
    }

    @Test
    fun `updateSecuritySettings persists and getSecuritySettings returns same values`() = runTest {
        val storage = EncryptedSecuritySettingsStorage(EncryptedSettingsMock())
        val settings = securitySettingsMock()

        storage.updateSecuritySettings(settings)

        assertEquals(settings, storage.getSecuritySettings())
    }

    @Test
    fun `clearSecuritySettings removes snapshot`() = runTest {
        val storage = EncryptedSecuritySettingsStorage(EncryptedSettingsMock())
        val settings = securitySettingsMock()

        storage.updateSecuritySettings(settings)
        storage.clearSecuritySettings()

        assertNull(storage.getSecuritySettings())
    }
}
