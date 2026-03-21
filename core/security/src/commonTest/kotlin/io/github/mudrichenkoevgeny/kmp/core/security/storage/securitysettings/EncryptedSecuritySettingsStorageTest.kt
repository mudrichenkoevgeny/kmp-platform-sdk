package io.github.mudrichenkoevgeny.kmp.core.security.storage.securitysettings

import io.github.mudrichenkoevgeny.kmp.core.common.mock.storage.MockEncryptedSettings
import io.github.mudrichenkoevgeny.kmp.core.security.model.securitysettings.SecuritySettings
import io.github.mudrichenkoevgeny.shared.foundation.core.security.passwordpolicy.model.PasswordPolicy
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class EncryptedSecuritySettingsStorageTest {

    @Test
    fun `getSecuritySettings returns null when not persisted`() = runTest {
        val storage = EncryptedSecuritySettingsStorage(MockEncryptedSettings())

        assertNull(storage.getSecuritySettings())
    }

    @Test
    fun `updateSecuritySettings persists and getSecuritySettings returns same values`() = runTest {
        val storage = EncryptedSecuritySettingsStorage(MockEncryptedSettings())
        val settings = SecuritySettings(
            passwordPolicy = PasswordPolicy(
                minLength = 12,
                requireLetter = true,
                requireUpperCase = true,
                requireDigit = true
            )
        )

        storage.updateSecuritySettings(settings)

        assertEquals(settings, storage.getSecuritySettings())
    }

    @Test
    fun `clearSecuritySettings removes snapshot`() = runTest {
        val storage = EncryptedSecuritySettingsStorage(MockEncryptedSettings())
        val settings = SecuritySettings(passwordPolicy = PasswordPolicy(minLength = 4))

        storage.updateSecuritySettings(settings)
        storage.clearSecuritySettings()

        assertNull(storage.getSecuritySettings())
    }
}
