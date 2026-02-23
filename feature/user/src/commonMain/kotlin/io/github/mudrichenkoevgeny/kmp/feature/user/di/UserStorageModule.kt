package io.github.mudrichenkoevgeny.kmp.feature.user.di

import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.user.EncryptedUserStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.user.UserStorage

internal class UserStorageModule(encryptedSettings: EncryptedSettings) {
    val userStorage: UserStorage by lazy { EncryptedUserStorage(encryptedSettings) }
}