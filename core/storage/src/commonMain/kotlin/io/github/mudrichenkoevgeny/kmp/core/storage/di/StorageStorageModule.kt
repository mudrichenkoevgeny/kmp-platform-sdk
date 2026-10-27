package io.github.mudrichenkoevgeny.kmp.core.storage.di

import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.kmp.core.storage.storage.storagesettings.EncryptedOpenStorageSettingsStorage
import io.github.mudrichenkoevgeny.kmp.core.storage.storage.storagesettings.OpenStorageSettingsStorage

/**
 * Internal storage wiring for `core/storage`.
 *
 * Binds [EncryptedSettings] to a concrete [OpenStorageSettingsStorage] implementation used by the
 * repository layer.
 */
internal class StorageStorageModule(encryptedSettings: EncryptedSettings) {
    /**
     * Encrypted implementation of [OpenStorageSettingsStorage].
     */
    val openStorageSettingsStorage: OpenStorageSettingsStorage by lazy {
        EncryptedOpenStorageSettingsStorage(
            encryptedSettings
        )
    }
}
