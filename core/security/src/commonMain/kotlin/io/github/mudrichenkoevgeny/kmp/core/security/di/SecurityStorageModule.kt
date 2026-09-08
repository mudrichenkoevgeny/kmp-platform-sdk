package io.github.mudrichenkoevgeny.kmp.core.security.di

import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.kmp.core.security.storage.securitysettings.EncryptedOpenSecuritySettingsStorage
import io.github.mudrichenkoevgeny.kmp.core.security.storage.securitysettings.OpenSecuritySettingsStorage

/**
 * Internal storage wiring for `core/security`.
 *
 * Binds [EncryptedSettings] to a concrete [OpenSecuritySettingsStorage] implementation used by the
 * repository layer.
 */
internal class SecurityStorageModule(encryptedSettings: EncryptedSettings) {
    /**
     * Encrypted implementation of [OpenSecuritySettingsStorage].
     */
    val openSecuritySettingsStorage: OpenSecuritySettingsStorage by lazy {
        EncryptedOpenSecuritySettingsStorage(
            encryptedSettings
        )
    }
}