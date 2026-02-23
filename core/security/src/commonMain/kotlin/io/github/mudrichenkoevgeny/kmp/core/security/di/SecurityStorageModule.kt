package io.github.mudrichenkoevgeny.kmp.core.security.di

import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.kmp.core.security.storage.securitysettings.EncryptedSecuritySettingsStorage
import io.github.mudrichenkoevgeny.kmp.core.security.storage.securitysettings.SecuritySettingsStorage

internal class SecurityStorageModule(encryptedSettings: EncryptedSettings) {
    val securitySettingsStorage: SecuritySettingsStorage by lazy {
        EncryptedSecuritySettingsStorage(
            encryptedSettings
        )
    }
}