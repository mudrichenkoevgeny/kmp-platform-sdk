package io.github.mudrichenkoevgeny.kmp.feature.user.di

import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.user.EncryptedUserStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.user.UserStorage

/**
 * Wiring for encrypted user-scoped storage backed by [EncryptedSettings].
 *
 * @param encryptedSettings Platform encrypted settings used to build [UserStorage].
 */
class UserStorageModule(encryptedSettings: EncryptedSettings) {
    /**
     * Encrypted implementation of [UserStorage].
     */
    val userStorage: UserStorage by lazy { EncryptedUserStorage(encryptedSettings) }
}