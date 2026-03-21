package io.github.mudrichenkoevgeny.kmp.core.security.di

import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.kmp.core.security.storage.securitysettings.EncryptedSecuritySettingsStorage
import io.github.mudrichenkoevgeny.kmp.core.security.storage.securitysettings.SecuritySettingsStorage

/**
 * Internal storage wiring for `core/security`.
 *
 * Binds [EncryptedSettings] to a concrete [SecuritySettingsStorage] implementation used by the
 * repository layer.
 */
internal class SecurityStorageModule(encryptedSettings: EncryptedSettings) {
    /**
     * Encrypted implementation of [SecuritySettingsStorage].
     */
    val securitySettingsStorage: SecuritySettingsStorage by lazy {
        EncryptedSecuritySettingsStorage(
            encryptedSettings
        )
    }
}