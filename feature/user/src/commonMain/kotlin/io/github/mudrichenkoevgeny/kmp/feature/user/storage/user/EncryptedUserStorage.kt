package io.github.mudrichenkoevgeny.kmp.feature.user.storage.user

import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.user.UserPrivatePayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.user.toUserPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.user.toUserPrivatePayload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Encrypted local storage implementation for user snapshot, identifiers, and active sessions.
 *
 * @param encryptedSettings Core encrypted settings key-value abstraction.
 */
class EncryptedUserStorage(
    private val encryptedSettings: EncryptedSettings
) : UserStorage {

    companion object {
        const val KEY_CURRENT_USER = "key_current_user"
    }

    override suspend fun getCurrentUser(): UserPrivate? {
        val json = encryptedSettings.get(KEY_CURRENT_USER) ?: return null
        return try {
            FoundationJson.decodeFromString<UserPrivatePayload>(json).toUserPrivate()
        } catch (_: Exception) {
            encryptedSettings.remove(KEY_CURRENT_USER)
            null
        }
    }

    override fun observeCurrentUser(): Flow<UserPrivate?> {
        return encryptedSettings.observe(KEY_CURRENT_USER).map { json ->
            if (json == null) {
                null
            } else {
                try {
                    FoundationJson.decodeFromString<UserPrivatePayload>(json)
                        .toUserPrivate()
                } catch (_: Exception) {
                    encryptedSettings.remove(KEY_CURRENT_USER)
                    null
                }
            }
        }
    }

    override suspend fun updateCurrentUser(currentUser: UserPrivate) {
        val payload = currentUser.toUserPrivatePayload()
        val json = FoundationJson.encodeToString(payload)
        encryptedSettings.put(KEY_CURRENT_USER, json)
    }

    override suspend fun clear() {
        encryptedSettings.remove(KEY_CURRENT_USER)
    }
}
