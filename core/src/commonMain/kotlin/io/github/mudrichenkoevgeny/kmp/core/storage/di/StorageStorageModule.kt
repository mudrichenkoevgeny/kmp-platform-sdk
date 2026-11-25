package io.github.mudrichenkoevgeny.kmp.core.storage.di

import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.kmp.core.storage.storage.management.EncryptedManagementStorageSettingsStorage
import io.github.mudrichenkoevgeny.kmp.core.storage.storage.management.ManagementStorageSettingsStorage
import io.github.mudrichenkoevgeny.kmp.core.storage.storage.open.EncryptedOpenStorageSettingsStorage
import io.github.mudrichenkoevgeny.kmp.core.storage.storage.open.OpenStorageSettingsStorage

/**
 * Internal storage wiring for `core/storage`.
 *
 * Binds [EncryptedSettings] to concrete [OpenStorageSettingsStorage] and [ManagementStorageSettingsStorage] implementations.
 */
internal class StorageStorageModule(encryptedSettings: EncryptedSettings) {

    /** Encrypted implementation of [OpenStorageSettingsStorage]. */
    val openStorageSettingsStorage: OpenStorageSettingsStorage by lazy {
        EncryptedOpenStorageSettingsStorage(encryptedSettings)
    }

    /** Encrypted implementation of [ManagementStorageSettingsStorage]. */
    val managementStorageSettingsStorage: ManagementStorageSettingsStorage by lazy {
        EncryptedManagementStorageSettingsStorage(encryptedSettings)
    }
}
