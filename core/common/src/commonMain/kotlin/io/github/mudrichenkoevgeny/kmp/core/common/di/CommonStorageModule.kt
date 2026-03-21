package io.github.mudrichenkoevgeny.kmp.core.common.di

import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.kmp.core.common.storage.common.CommonStorage
import io.github.mudrichenkoevgeny.kmp.core.common.storage.common.EncryptedCommonStorage

/**
 * Internal storage wiring for `core/common`.
 *
 * Converts [EncryptedSettings] into a concrete [CommonStorage] implementation
 * used by higher-level repositories.
 */
internal class CommonStorageModule(encryptedSettings: EncryptedSettings) {
    /**
     * Encrypted implementation of [CommonStorage].
     */
    val commonStorage: CommonStorage by lazy {
        EncryptedCommonStorage(encryptedSettings)
    }
}