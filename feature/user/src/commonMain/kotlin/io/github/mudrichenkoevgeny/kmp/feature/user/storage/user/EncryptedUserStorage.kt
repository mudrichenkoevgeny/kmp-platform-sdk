package io.github.mudrichenkoevgeny.kmp.feature.user.storage.user

import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.kmp.feature.user.model.session.UserSession
import io.github.mudrichenkoevgeny.kmp.feature.user.model.user.CurrentUser
import io.github.mudrichenkoevgeny.kmp.feature.user.model.useridentifier.UserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EncryptedUserStorage(
    private val encryptedSettings: EncryptedSettings
) : UserStorage {

    private val json = FoundationJson

    override suspend fun getCurrentUser(): CurrentUser? {
        val data = encryptedSettings.get(KEY_CURRENT_USER) ?: return null
        return json.decodeFromString(data)
    }

    override fun observeCurrentUser(): Flow<CurrentUser?> {
        return encryptedSettings.observe(KEY_CURRENT_USER).map { data ->
            if (data == null) {
                null
            } else {
                json.decodeFromString<CurrentUser>(data)
            }
        }
    }

    override suspend fun updateCurrentUser(currentUser: CurrentUser) {
        val data = json.encodeToString(currentUser)
        encryptedSettings.put(KEY_CURRENT_USER, data)
    }

    override suspend fun getUserIdentifiersList(): List<UserIdentifier> {
        val data = encryptedSettings.get(KEY_USER_IDENTIFIERS) ?: return emptyList()
        return json.decodeFromString(data)
    }

    override suspend fun updateUserIdentifiersList(userIdentifiersList: List<UserIdentifier>) {
        val data = json.encodeToString(userIdentifiersList)
        encryptedSettings.put(KEY_USER_IDENTIFIERS, data)
    }

    override suspend fun getUserSessionsList(): List<UserSession> {
        val data = encryptedSettings.get(KEY_USER_SESSIONS) ?: return emptyList()
        return json.decodeFromString(data)
    }

    override suspend fun updateUserSessionsList(userSessionsList: List<UserSession>) {
        val data = json.encodeToString(userSessionsList)
        encryptedSettings.put(KEY_USER_SESSIONS, data)
    }

    override suspend fun clear() {
        encryptedSettings.remove(KEY_CURRENT_USER)
        encryptedSettings.remove(KEY_USER_IDENTIFIERS)
        encryptedSettings.remove(KEY_USER_SESSIONS)
    }

    companion object {
        private const val KEY_CURRENT_USER = "current_user"
        private const val KEY_USER_IDENTIFIERS = "user_identifiers_list"
        private const val KEY_USER_SESSIONS = "user_sessions_list"
    }
}