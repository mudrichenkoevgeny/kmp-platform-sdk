package io.github.mudrichenkoevgeny.kmp.core.settings.di

import io.github.mudrichenkoevgeny.kmp.core.settings.storage.globalsettings.EncryptedGlobalSettingsStorage
import io.github.mudrichenkoevgeny.kmp.core.settings.storage.globalsettings.GlobalSettingsStorage
import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings

internal class SettingsStorageModule(encryptedSettings: EncryptedSettings) {
    val globalSettingsStorage: GlobalSettingsStorage by lazy {
        EncryptedGlobalSettingsStorage(
            encryptedSettings
        )
    }
}