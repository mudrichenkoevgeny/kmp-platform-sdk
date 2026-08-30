package io.github.mudrichenkoevgeny.kmp.feature.user.storage.user

import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.core.common.mapper.pagedresult.mapItems
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole
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
import kotlin.math.ceil

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

    private suspend fun getAllUserIdentifiersInternal(): List<UserIdentifier> {
        val data = encryptedSettings.get(KEY_USER_IDENTIFIERS) ?: return emptyList()
        val pagedPayload = json.decodeFromString<PagedResult<UserIdentifierPayload>>(data)
        return pagedPayload.items.map { it.toUserIdentifier() }
    }

    override suspend fun getUserIdentifiersList(
        pageNumber: Int?,
        pageSize: Int?,
        sortBy: UserSortValues.UserIdentifierSortBy?,
        sortOrder: SortOrder?,
        userIds: List<String>?,
        userAuthProviders: List<UserAuthProvider>?,
        identifiers: List<String>?
    ): PagedResult<UserIdentifier> {
        val allItems = getAllUserIdentifiersInternal()
        if (allItems.isEmpty()) {
            return PagedResult.empty()
        }

        val filteredItems = allItems.filter { item ->
            val matchesUserIds = userIds == null || userIds.contains(item.userId.value.toHexDashString())
            val matchesProvider = userAuthProviders == null || item.userAuthProvider in userAuthProviders
            val matchesValue = identifiers == null || identifiers.any { pattern ->
                item.identifier.contains(pattern, ignoreCase = true)
            }
            matchesUserIds && matchesProvider && matchesValue
        }.let { list ->
            when (sortBy) {
                UserSortValues.UserIdentifierSortBy.CREATED_AT -> {
                    if (sortOrder == SortOrder.DESC) list.sortedByDescending { it.createdAt }
                    else list.sortedBy { it.createdAt }
                }
                UserSortValues.UserIdentifierSortBy.UPDATED_AT -> {
                    if (sortOrder == SortOrder.DESC) list.sortedByDescending { it.updatedAt }
                    else list.sortedBy { it.updatedAt }
                }
                null -> list
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

    override fun observeUserIdentifiersList(
        pageNumber: Int?,
        pageSize: Int?,
        sortBy: UserSortValues.UserIdentifierSortBy?,
        sortOrder: SortOrder?,
        userIds: List<String>?,
        userAuthProviders: List<UserAuthProvider>?,
        identifiers: List<String>?
    ): Flow<PagedResult<UserIdentifier>> {
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

    private suspend fun getAllUserSessionsInternal(): List<UserSession> {
        val data = encryptedSettings.get(KEY_USER_SESSIONS) ?: return emptyList()
        val pagedPayload = json.decodeFromString<PagedResult<UserSessionPayload>>(data)
        return pagedPayload.items.map { it.toUserSession() }
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
    ): PagedResult<UserSession> {
        val allItems = getAllUserSessionsInternal()
        if (allItems.isEmpty()) {
            return PagedResult.empty()
        }

        val filteredItems = allItems.filter { item ->
            val matchesUserIds = userIds == null || userIds.contains(item.userId.value.toHexDashString())
            val matchesUserRoles = userRoles == null || item.userRole in userRoles
            val matchesIdentifiers = identifiers == null || identifiers.any { pattern ->
                item.identifier.contains(pattern, ignoreCase = true)
            }
            val matchesIdentifierIds = identifierIds == null || identifierIds.contains(item.identifierId.value.toHexDashString())
            val matchesProviders = userAuthProviders == null || item.identifierAuthProvider in userAuthProviders
            val matchesClientTypes = clientTypes == null || item.deviceInfo.clientType in clientTypes
            val matchesUserAgents = userAgents == null || userAgents.any { pattern ->
                item.userAgent?.contains(pattern, ignoreCase = true) == true
            }
            val matchesIpAddresses = ipAddresses == null || ipAddresses.any { pattern ->
                item.ipAddress?.contains(pattern, ignoreCase = true) == true
            }
            val matchesLanguages = languages == null || languages.any { pattern ->
                item.deviceInfo.language?.contains(pattern, ignoreCase = true) == true
            }
            val matchesDeviceIds = deviceIds == null || item.deviceInfo.deviceId?.value?.toHexDashString() in deviceIds
            val matchesDeviceNames = deviceNames == null || deviceNames.any { pattern ->
                item.deviceInfo.deviceName?.contains(pattern, ignoreCase = true) == true
            }
            val matchesAppVersions = appVersions == null || appVersions.any { pattern ->
                item.deviceInfo.appVersion?.contains(pattern, ignoreCase = true) == true
            }
            val matchesOsVersions = operationSystemVersions == null || operationSystemVersions.any { pattern ->
                item.deviceInfo.operationSystemVersion?.contains(pattern, ignoreCase = true) == true
            }

            matchesUserIds && matchesUserRoles && matchesIdentifiers && matchesIdentifierIds &&
                    matchesProviders && matchesClientTypes && matchesUserAgents && matchesIpAddresses &&
                    matchesLanguages && matchesDeviceIds && matchesDeviceNames && matchesAppVersions &&
                    matchesOsVersions
        }.let { list ->
            when (sortBy) {
                UserSortValues.UserSessionSortBy.LAST_ACCESSED_AT -> {
                    if (sortOrder == SortOrder.DESC) list.sortedByDescending { it.lastAccessedAt }
                    else list.sortedBy { it.lastAccessedAt }
                }
                UserSortValues.UserSessionSortBy.LAST_REAUTHENTICATED_AT -> {
                    if (sortOrder == SortOrder.DESC) list.sortedByDescending { it.lastReauthenticatedAt }
                    else list.sortedBy { it.lastReauthenticatedAt }
                }
                UserSortValues.UserSessionSortBy.EXPIRES_AT -> {
                    if (sortOrder == SortOrder.DESC) list.sortedByDescending { it.expiresAt }
                    else list.sortedBy { it.expiresAt }
                }
                UserSortValues.UserSessionSortBy.CREATED_AT -> {
                    if (sortOrder == SortOrder.DESC) list.sortedByDescending { it.createdAt }
                    else list.sortedBy { it.createdAt }
                }
                UserSortValues.UserSessionSortBy.UPDATED_AT -> {
                    if (sortOrder == SortOrder.DESC) list.sortedByDescending { it.updatedAt }
                    else list.sortedBy { it.updatedAt }
                }
                null -> list
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
    ): Flow<PagedResult<UserSession>> {
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