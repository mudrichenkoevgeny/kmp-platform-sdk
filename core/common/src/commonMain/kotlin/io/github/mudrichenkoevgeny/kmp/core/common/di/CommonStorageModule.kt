package io.github.mudrichenkoevgeny.kmp.core.common.di

import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.kmp.core.common.storage.common.CommonStorage
import io.github.mudrichenkoevgeny.kmp.core.common.storage.common.EncryptedCommonStorage

internal class CommonStorageModule(encryptedSettings: EncryptedSettings) {
    val commonStorage: CommonStorage by lazy {
        EncryptedCommonStorage(encryptedSettings)
    }
}