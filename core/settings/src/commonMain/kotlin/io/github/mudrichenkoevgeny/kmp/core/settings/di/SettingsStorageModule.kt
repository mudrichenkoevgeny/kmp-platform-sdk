package io.github.mudrichenkoevgeny.kmp.core.settings.di

import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.kmp.core.settings.storage.globalsettings.EncryptedOpenGlobalSettingsStorage
import io.github.mudrichenkoevgeny.kmp.core.settings.storage.globalsettings.OpenGlobalSettingsStorage

/**
 * Internal storage wiring for `core/settings`.
 *
 * Binds [EncryptedSettings] to a concrete [OpenGlobalSettingsStorage] implementation used by the
 * repository layer.
 */
internal class SettingsStorageModule(encryptedSettings: EncryptedSettings) {
    /**
     * Encrypted implementation of [OpenGlobalSettingsStorage].
     */
    val openGlobalSettingsStorage: OpenGlobalSettingsStorage by lazy {
        EncryptedOpenGlobalSettingsStorage(
            encryptedSettings
        )
    }
}