package io.github.mudrichenkoevgeny.kmp.feature.user.storage.user

import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.core.common.mapper.pagedresult.mapItems
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.identifier.toUserIdentifierSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.identifier.toUserIdentifierSummaryPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.session.toUserSessionSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.session.toUserSessionSummaryPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.user.toUserPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.user.toUserPrivatePayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierSummaryPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.UserSessionSummaryPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.user.UserPrivatePayload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.math.ceil

/**
 * [UserStorage] backed by [EncryptedSettings], using [FoundationJson] to encode [UserPrivate], identifier, and session lists.
 *
 * @param encryptedSettings Encrypted key-value store supplied by the host.
 */
class EncryptedUserStorage(
    private val encryptedSettings: EncryptedSettings
) : UserStorage {

    private val json = FoundationJson

    override suspend fun getCurrentUser(): UserPrivate? {
        val data = encryptedSettings.get(KEY_CURRENT_USER)
            ?: return null

        return try {
            json.decodeFromString<UserPrivatePayload>(data).toUserPrivate()
        } catch (_: Exception) {
            encryptedSettings.remove(KEY_CURRENT_USER)
            null
        }
    }

    override fun observeCurrentUser(): Flow<UserPrivate?> {
        return encryptedSettings.observe(KEY_CURRENT_USER).map { data ->
            if (data == null) {
                return@map null
            }

            try {
                json.decodeFromString<UserPrivatePayload>(data).toUserPrivate()
            } catch (_: Exception) {
                encryptedSettings.remove(KEY_CURRENT_USER)
                null
            }
        }
    }

    override suspend fun updateCurrentUser(currentUser: UserPrivate) {
        val userPrivatePayload = currentUser.toUserPrivatePayload()
        val data = json.encodeToString(userPrivatePayload)

        encryptedSettings.put(KEY_CURRENT_USER, data)
    }

    private suspend fun getAllUserIdentifiersInternal(): List<UserIdentifierSummary> {
        val data = encryptedSettings.get(KEY_USER_IDENTIFIERS) ?: return emptyList()
        val pagedPayload = try {
            json.decodeFromString<PagedResult<UserIdentifierSummaryPayload>>(data)
        } catch (_: Exception) {
            encryptedSettings.remove(KEY_USER_IDENTIFIERS)
            return emptyList()
        }
        return pagedPayload.items.map { it.toUserIdentifierSummary() }
    }

    override suspend fun getUserIdentifiersList(
        pageNumber: Int?,
        pageSize: Int?,
        sortBy: UserSortValues.UserIdentifierSortBy?,
        sortOrder: SortOrder?,
        userIds: List<String>?,
        userAuthProviders: List<UserAuthProvider>?,
        identifiers: List<String>?
    ): PagedResult<UserIdentifierSummary> {
        val allItems = getAllUserIdentifiersInternal()
        if (allItems.isEmpty()) {
            return PagedResult.empty()
        }

        val filteredItems = allItems.filter { item ->
            val matchesProvider = userAuthProviders == null || item.userAuthProvider in userAuthProviders
            val matchesValue = identifiers == null || identifiers.any { pattern ->
                item.identifier.contains(pattern, ignoreCase = true) || item.displayName.contains(pattern, ignoreCase = true)
            }
            matchesProvider && matchesValue
        }

        val totalCount = filteredItems.size
        val requestedPage = pageNumber ?: 1
        val requestedSize = pageSize ?: totalCount

        if (requestedSize <= 0) {
            return PagedResult(
                items = emptyList(),
                totalCount = totalCount.toLong(),
                pageNumber = requestedPage,
                pageSize = requestedSize,
                totalPages = 0L
            )
        }

        val totalPages = ceil(totalCount.toDouble() / requestedSize).toLong()
        val startIndex = ((requestedPage - 1) * requestedSize).coerceAtMost(totalCount)
        val endIndex = (startIndex + requestedSize).coerceAtMost(totalCount)

        val paginatedItems = if (startIndex < totalCount) {
            filteredItems.subList(startIndex, endIndex)
        } else {
            emptyList()
        }

        return PagedResult(
            items = paginatedItems,
            totalCount = totalCount.toLong(),
            pageNumber = requestedPage,
            pageSize = requestedSize,
            totalPages = totalPages
        )
    }

    override fun observeUserIdentifiersList(
        pageNumber: Int?,
        pageSize: Int?,
        sortBy: UserSortValues.UserIdentifierSortBy?,
        sortOrder: SortOrder?,
        userIds: List<String>?,
        userAuthProviders: List<UserAuthProvider>?,
        identifiers: List<String>?
    ): Flow<PagedResult<UserIdentifierSummary>> {
        return encryptedSettings.observe(KEY_USER_IDENTIFIERS).map {
            getUserIdentifiersList(
                pageNumber = pageNumber,
                pageSize = pageSize,
                sortBy = sortBy,
                sortOrder = sortOrder,
                userIds = userIds,
                userAuthProviders = userAuthProviders,
                identifiers = identifiers
            )
        }
    }

    override suspend fun updateUserIdentifiersList(userIdentifiersList: PagedResult<UserIdentifierSummary>) {
        val pagedPayload = userIdentifiersList.mapItems { userIdentifier ->
            userIdentifier.toUserIdentifierSummaryPayload()
        }

        updateUserIdentifiersPayloadList(pagedPayload)
    }

    override suspend fun updateUserIdentifiersPayloadList(
        userIdentifiersList: PagedResult<UserIdentifierSummaryPayload>
    ) {
        val data = json.encodeToString(userIdentifiersList)
        encryptedSettings.put(KEY_USER_IDENTIFIERS, data)
    }

    override suspend fun addUserIdentifier(userIdentifier: UserIdentifierSummary) {
        val allItems = getAllUserIdentifiersInternal()
        var contains = false

        val updatedItems = allItems.map { existing ->
            if (existing.id == userIdentifier.id) {
                contains = true
                userIdentifier
            } else {
                existing
            }
        }.let { list ->
            if (contains) list else list + userIdentifier
        }

        val defaultSize = 20
        val totalCount = updatedItems.size
        val totalPages = ceil(totalCount.toDouble() / defaultSize).toLong()

        updateUserIdentifiersList(
            PagedResult(
                items = updatedItems,
                totalCount = totalCount.toLong(),
                pageNumber = 1,
                pageSize = defaultSize,
                totalPages = totalPages
            )
        )
    }

    override suspend fun removeUserIdentifier(identifierId: UserIdentifierId) {
        val allItems = getAllUserIdentifiersInternal()
        val updatedItems = allItems.filter { userIdentifier ->
            userIdentifier.id != identifierId
        }

        if (updatedItems.size == allItems.size) return

        val defaultSize = 20
        val totalCount = updatedItems.size
        val totalPages = ceil(totalCount.toDouble() / defaultSize).toLong()

        updateUserIdentifiersList(
            PagedResult(
                items = updatedItems,
                totalCount = totalCount.toLong(),
                pageNumber = 1,
                pageSize = defaultSize,
                totalPages = totalPages
            )
        )
    }

    private suspend fun getAllUserSessionsInternal(): List<UserSessionSummary> {
        val data = encryptedSettings.get(KEY_USER_SESSIONS) ?: return emptyList()
        val pagedPayload = try {
            json.decodeFromString<PagedResult<UserSessionSummaryPayload>>(data)
        } catch (_: Exception) {
            encryptedSettings.remove(KEY_USER_SESSIONS)
            return emptyList()
        }
        return pagedPayload.items.map { it.toUserSessionSummary() }
    }

    override suspend fun getUserSessionsList(
        pageNumber: Int?,
        pageSize: Int?,
        sortBy: UserSortValues.UserSessionSortBy?,
        sortOrder: SortOrder?,
        userIds: List<String>?,
        userRoles: List<UserRole>?,
        identifiers: List<String>?,
        identifierIds: List<String>?,
        userAuthProviders: List<UserAuthProvider>?,
        clientTypes: List<ClientType>?,
        userAgents: List<String>?,
        ipAddresses: List<String>?,
        languages: List<String>?,
        deviceIds: List<String>?,
        deviceNames: List<String>?,
        appVersions: List<String>?,
        operationSystemVersions: List<String>?
    ): PagedResult<UserSessionSummary> {
        val allItems = getAllUserSessionsInternal()
        if (allItems.isEmpty()) {
            return PagedResult.empty()
        }

        val filteredItems = allItems.filter { item ->
            val matchesIdentifiers = identifiers == null || identifiers.any { pattern ->
                item.identifierDisplayName.contains(pattern, ignoreCase = true)
            }
            val matchesProviders = userAuthProviders == null || item.identifierAuthProvider in userAuthProviders
            val matchesClientTypes = clientTypes == null || item.clientDeviceInfo.clientType in clientTypes
            val matchesLanguages = languages == null || languages.any { pattern ->
                item.clientDeviceInfo.language?.contains(pattern, ignoreCase = true) == true
            }
            val matchesDeviceIds = deviceIds == null || item.clientDeviceInfo.deviceId?.value?.toHexDashString() in deviceIds
            val matchesDeviceNames = deviceNames == null || deviceNames.any { pattern ->
                item.clientDeviceInfo.deviceName?.contains(pattern, ignoreCase = true) == true
            }
            val matchesAppVersions = appVersions == null || appVersions.any { pattern ->
                item.clientDeviceInfo.appVersion?.contains(pattern, ignoreCase = true) == true
            }
            val matchesOsVersions = operationSystemVersions == null || operationSystemVersions.any { pattern ->
                item.clientDeviceInfo.operationSystemVersion?.contains(pattern, ignoreCase = true) == true
            }

            matchesIdentifiers && matchesProviders && matchesClientTypes &&
                    matchesLanguages && matchesDeviceIds && matchesDeviceNames && matchesAppVersions &&
                    matchesOsVersions
        }.let { list ->
            when (sortBy) {
                UserSortValues.UserSessionSortBy.LAST_ACCESSED_AT -> {
                    if (sortOrder == SortOrder.DESC) list.sortedByDescending { it.lastAccessedAt }
                    else list.sortedBy { it.lastAccessedAt }
                }
                UserSortValues.UserSessionSortBy.EXPIRES_AT -> {
                    if (sortOrder == SortOrder.DESC) list.sortedByDescending { it.expiresAt }
                    else list.sortedBy { it.expiresAt }
                }
                else -> list
            }
        }

        val totalCount = filteredItems.size
        val requestedPage = pageNumber ?: 1
        val requestedSize = pageSize ?: totalCount

        if (requestedSize <= 0) {
            return PagedResult(
                items = emptyList(),
                totalCount = totalCount.toLong(),
                pageNumber = requestedPage,
                pageSize = requestedSize,
                totalPages = 0L
            )
        }

        val totalPages = ceil(totalCount.toDouble() / requestedSize).toLong()
        val startIndex = ((requestedPage - 1) * requestedSize).coerceAtMost(totalCount)
        val endIndex = (startIndex + requestedSize).coerceAtMost(totalCount)

        val paginatedItems = if (startIndex < totalCount) {
            filteredItems.subList(startIndex, endIndex)
        } else {
            emptyList()
        }

        return PagedResult(
            items = paginatedItems,
            totalCount = totalCount.toLong(),
            pageNumber = requestedPage,
            pageSize = requestedSize,
            totalPages = totalPages
        )
    }

    override fun observeUserSessionsList(
        pageNumber: Int?,
        pageSize: Int?,
        sortBy: UserSortValues.UserSessionSortBy?,
        sortOrder: SortOrder?,
        userIds: List<String>?,
        userRoles: List<UserRole>?,
        identifiers: List<String>?,
        identifierIds: List<String>?,
        userAuthProviders: List<UserAuthProvider>?,
        clientTypes: List<ClientType>?,
        userAgents: List<String>?,
        ipAddresses: List<String>?,
        languages: List<String>?,
        deviceIds: List<String>?,
        deviceNames: List<String>?,
        appVersions: List<String>?,
        operationSystemVersions: List<String>?
    ): Flow<PagedResult<UserSessionSummary>> {
        return encryptedSettings.observe(KEY_USER_SESSIONS).map {
            getUserSessionsList(
                pageNumber = pageNumber,
                pageSize = pageSize,
                sortBy = sortBy,
                sortOrder = sortOrder,
                userIds = userIds,
                userRoles = userRoles,
                identifiers = identifiers,
                identifierIds = identifierIds,
                userAuthProviders = userAuthProviders,
                clientTypes = clientTypes,
                userAgents = userAgents,
                ipAddresses = ipAddresses,
                languages = languages,
                deviceIds = deviceIds,
                deviceNames = deviceNames,
                appVersions = appVersions,
                operationSystemVersions = operationSystemVersions
            )
        }
    }

    override suspend fun updateUserSessionsList(userSessionsList: PagedResult<UserSessionSummary>) {
        val pagedPayload = userSessionsList.mapItems { userSession ->
            userSession.toUserSessionSummaryPayload()
        }
        updateUserSessionsPayloadList(pagedPayload)
    }

    override suspend fun updateUserSessionsPayloadList(userSessionsList: PagedResult<UserSessionSummaryPayload>) {
        val data = json.encodeToString(userSessionsList)
        encryptedSettings.put(KEY_USER_SESSIONS, data)
    }

    override suspend fun addUserSession(userSession: UserSessionSummary) {
        val allItems = getAllUserSessionsInternal()
        var contains = false

        val updatedItems = allItems.map { existing ->
            if (existing.id == userSession.id) {
                contains = true
                userSession
            } else {
                existing
            }
        }.let { list ->
            if (contains) list else list + userSession
        }

        val defaultSize = 20
        val totalCount = updatedItems.size
        val totalPages = ceil(totalCount.toDouble() / defaultSize).toLong()

        updateUserSessionsList(
            PagedResult(
                items = updatedItems,
                totalCount = totalCount.toLong(),
                pageNumber = 1,
                pageSize = defaultSize,
                totalPages = totalPages
            )
        )
    }

    override suspend fun removeUserSession(sessionId: UserSessionId) {
        val allItems = getAllUserSessionsInternal()
        val updatedItems = allItems.filter { userSession ->
            userSession.id != sessionId
        }

        if (updatedItems.size == allItems.size) return

        val defaultSize = 20
        val totalCount = updatedItems.size
        val totalPages = ceil(totalCount.toDouble() / defaultSize).toLong()

        updateUserSessionsList(
            PagedResult(
                items = updatedItems,
                totalCount = totalCount.toLong(),
                pageNumber = 1,
                pageSize = defaultSize,
                totalPages = totalPages
            )
        )
    }

    override suspend fun removeUserSessions(sessionIds: List<UserSessionId>) {
        val allItems = getAllUserSessionsInternal()
        val idsToRemove = sessionIds.toSet()
        val updatedItems = allItems.filter { userSession ->
            userSession.id !in idsToRemove
        }

        if (updatedItems.size == allItems.size) return

        val defaultSize = 20
        val totalCount = updatedItems.size
        val totalPages = ceil(totalCount.toDouble() / defaultSize).toLong()

        updateUserSessionsList(
            PagedResult(
                items = updatedItems,
                totalCount = totalCount.toLong(),
                pageNumber = 1,
                pageSize = defaultSize,
                totalPages = totalPages
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