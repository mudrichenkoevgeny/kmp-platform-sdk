package io.github.mudrichenkoevgeny.kmp.core.settings.di

import io.github.mudrichenkoevgeny.kmp.core.settings.storage.globalsettings.EncryptedGlobalSettingsStorage
import io.github.mudrichenkoevgeny.kmp.core.settings.storage.globalsettings.GlobalSettingsStorage
import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings

/**
 * Internal storage wiring for `core/settings`.
 *
 * Binds [EncryptedSettings] to a concrete [GlobalSettingsStorage] implementation used by the
 * repository layer.
 */
internal class SettingsStorageModule(encryptedSettings: EncryptedSettings) {
    /**
     * Encrypted implementation of [GlobalSettingsStorage].
     */
    val globalSettingsStorage: GlobalSettingsStorage by lazy {
        EncryptedGlobalSettingsStorage(
            encryptedSettings
        )
    }
}