package io.github.mudrichenkoevgeny.kmp.feature.user.storage.user

import io.github.mudrichenkoevgeny.kmp.core.common.mapper.pagedresult.mapItems
import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserDetails
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.identifier.toUserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.identifier.toUserIdentifierPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.session.toUserSession
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.session.toUserSessionPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.user.toUserDetails
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.user.toUserDetailsPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.UserSessionPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.user.UserDetailsPayload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * [UserStorage] backed by [EncryptedSettings], using [FoundationJson] to encode [UserDetails], identifier, and session lists.
 *
 * @param encryptedSettings Encrypted key-value store supplied by the host.
 */
class EncryptedUserStorage(
    private val encryptedSettings: EncryptedSettings
) : UserStorage {

    private val json = FoundationJson

    override suspend fun getCurrentUser(): UserDetails? {
        val data = encryptedSettings.get(KEY_CURRENT_USER)
            ?: return null

        return json.decodeFromString<UserDetailsPayload>(data).toUserDetails()
    }

    override fun observeCurrentUser(): Flow<UserDetails?> {
        return encryptedSettings.observe(KEY_CURRENT_USER).map { data ->
            if (data == null) {
                return@map null
            }

            json.decodeFromString<UserDetailsPayload>(data).toUserDetails()
        }
    }

    override suspend fun updateCurrentUser(currentUser: UserDetails) {
        val userDetailsPayload = currentUser.toUserDetailsPayload()
        val data = json.encodeToString(userDetailsPayload)

        encryptedSettings.put(KEY_CURRENT_USER, data)
    }

    override suspend fun getUserIdentifiersList(): PagedResult<UserIdentifier> {
        val data = encryptedSettings.get(KEY_USER_IDENTIFIERS)
            ?: return PagedResult.empty()

        val pagedPayload = json.decodeFromString<PagedResult<UserIdentifierPayload>>(data)

        return pagedPayload.mapItems { userIdentifierPayload ->
            userIdentifierPayload.toUserIdentifier()
        }
    }

    override fun observeUserIdentifiersList(): Flow<PagedResult<UserIdentifier>> {
        return encryptedSettings.observe(KEY_USER_IDENTIFIERS).map { data ->
            if (data == null) {
                return@map PagedResult.empty()
            }

            val pagedPayload = json.decodeFromString<PagedResult<UserIdentifierPayload>>(data)

            pagedPayload.mapItems { userIdentifierPayload ->
                userIdentifierPayload.toUserIdentifier()
            }
        }
    }

    override suspend fun updateUserIdentifiersList(userIdentifiersList: PagedResult<UserIdentifier>) {
        val pagedPayload = userIdentifiersList.mapItems { userIdentifier ->
            userIdentifier.toUserIdentifierPayload()
        }

        updateUserIdentifiersPayloadList(pagedPayload)
    }

    override suspend fun updateUserIdentifiersPayloadList(
        userIdentifiersList: PagedResult<UserIdentifierPayload>
    ) {
        val data = json.encodeToString(userIdentifiersList)
        encryptedSettings.put(KEY_USER_IDENTIFIERS, data)
    }

    override suspend fun addUserIdentifier(userIdentifier: UserIdentifier) {
        val currentPaged = getUserIdentifiersList()
        val updatedItems = currentPaged.items + userIdentifier

        updateUserIdentifiersList(
            currentPaged.copy(
                items = updatedItems,
                totalCount = currentPaged.totalCount + 1
            )
        )
    }

    override suspend fun removeUserIdentifier(identifierId: UserIdentifierId) {
        val currentPaged = getUserIdentifiersList()
        val updatedItems = currentPaged.items.filter { userIdentifier ->
            userIdentifier.id != identifierId
        }

        if (updatedItems.size == currentPaged.items.size) return

        updateUserIdentifiersList(
            currentPaged.copy(
                items = updatedItems,
                totalCount = (currentPaged.totalCount - 1).coerceAtLeast(0)
            )
        )
    }

    override suspend fun getUserSessionsList(): PagedResult<UserSession> {
        val data = encryptedSettings.get(KEY_USER_SESSIONS)
            ?: return PagedResult.empty()

        val pagedPayload = json.decodeFromString<PagedResult<UserSessionPayload>>(data)

        return pagedPayload.mapItems { userSessionPayload ->
            userSessionPayload.toUserSession()
        }
    }

    override fun observeUserSessionsList(): Flow<PagedResult<UserSession>> {
        return encryptedSettings.observe(KEY_USER_SESSIONS).map { data ->
            if (data == null) {
                return@map PagedResult.empty()
            }

            val pagedPayload = json.decodeFromString<PagedResult<UserSessionPayload>>(data)

            pagedPayload.mapItems { userSessionPayload ->
                userSessionPayload.toUserSession()
            }
        }
    }

    override suspend fun updateUserSessionsList(userSessionsList: PagedResult<UserSession>) {
        val pagedPayload = userSessionsList.mapItems { userSession ->
            userSession.toUserSessionPayload()
        }
        updateUserSessionsPayloadList(pagedPayload)
    }

    override suspend fun updateUserSessionsPayloadList(userSessionsList: PagedResult<UserSessionPayload>) {
        val data = json.encodeToString(userSessionsList)
        encryptedSettings.put(KEY_USER_SESSIONS, data)
    }

    override suspend fun addUserSession(userSession: UserSession) {
        val currentPaged = getUserSessionsList()
        val updatedItems = currentPaged.items + userSession

        updateUserSessionsList(
            currentPaged.copy(
                items = updatedItems,
                totalCount = currentPaged.totalCount + 1
            )
        )
    }

    override suspend fun removeUserSession(sessionId: UserSessionId) {
        val currentPaged = getUserSessionsList()
        val updatedItems = currentPaged.items.filter { userSession ->
            userSession.id != sessionId
        }

        if (updatedItems.size == currentPaged.items.size) return

        updateUserSessionsList(
            currentPaged.copy(
                items = updatedItems,
                totalCount = (currentPaged.totalCount - 1).coerceAtLeast(0)
            )
        )
    }

    override suspend fun removeUserSessions(sessionIds: List<UserSessionId>) {
        val currentPaged = getUserSessionsList()

        val idsToRemove = sessionIds.toSet()

        val updatedItems = currentPaged.items.filter { userSession ->
            userSession.id !in idsToRemove
        }
        if (updatedItems.size == currentPaged.items.size) return

        val removedCount = currentPaged.items.size - updatedItems.size

        updateUserSessionsList(
            currentPaged.copy(
                items = updatedItems,
                totalCount = (currentPaged.totalCount - removedCount).coerceAtLeast(0)
            )
        )
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
