package io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.user

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.domain.model.listing.pagedResultMock
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.user.UserStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.identifier.toUserIdentifierSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.session.toUserSessionSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierSummaryPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.UserSessionSummaryPayload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

@InternalApi
class UserStorageMock : UserStorage {

    private val currentUserFlow = MutableStateFlow<UserPrivate?>(null)
    private val identifiersFlow = MutableStateFlow(pagedResultMock<UserIdentifierSummary>())
    private val sessionsFlow = MutableStateFlow(pagedResultMock<UserSessionSummary>())

    var isCleared = false
        private set

    var lastUpdatedIdentifiersPayload: PagedResult<UserIdentifierSummaryPayload>? = null
    var lastRemovedIdentifierId: UserIdentifierId? = null
    var lastUpdatedSessionsPayload: PagedResult<UserSessionSummaryPayload>? = null
    var lastAddedSession: UserSessionSummary? = null
    var lastRemovedSessionId: UserSessionId? = null
    var lastRemovedSessionsList: List<UserSessionId>? = null

    override suspend fun getCurrentUser(): UserPrivate? = currentUserFlow.value

    override fun observeCurrentUser(): Flow<UserPrivate?> = currentUserFlow

    override suspend fun updateCurrentUser(currentUser: UserPrivate) {
        currentUserFlow.value = currentUser
    }

    override suspend fun getUserIdentifiersList(
        pageNumber: Int?,
        pageSize: Int?,
        sortBy: UserSortValues.UserIdentifierSortBy?,
        sortOrder: SortOrder?,
        userIds: List<String>?,
        userAuthProviders: List<UserAuthProvider>?,
        identifiers: List<String>?
    ): PagedResult<UserIdentifierSummary> = identifiersFlow.value

    override fun observeUserIdentifiersList(
        pageNumber: Int?,
        pageSize: Int?,
        sortBy: UserSortValues.UserIdentifierSortBy?,
        sortOrder: SortOrder?,
        userIds: List<String>?,
        userAuthProviders: List<UserAuthProvider>?,
        identifiers: List<String>?
    ): Flow<PagedResult<UserIdentifierSummary>> = identifiersFlow

    override suspend fun updateUserIdentifiersList(userIdentifiersList: PagedResult<UserIdentifierSummary>) {
        identifiersFlow.value = userIdentifiersList
    }

    override suspend fun updateUserIdentifiersPayloadList(userIdentifiersList: PagedResult<UserIdentifierSummaryPayload>) {
        lastUpdatedIdentifiersPayload = userIdentifiersList
        val mapped = userIdentifiersList.items.map { it.toUserIdentifierSummary() }
        identifiersFlow.value = pagedResultMock(
            items = mapped,
            totalCount = userIdentifiersList.totalCount,
            pageNumber = userIdentifiersList.pageNumber,
            pageSize = userIdentifiersList.pageSize,
            totalPages = userIdentifiersList.totalPages
        )
    }

    override suspend fun addUserIdentifier(userIdentifier: UserIdentifierSummary) {
        identifiersFlow.update { current ->
            current.copy(
                items = current.items + userIdentifier,
                totalCount = current.totalCount + 1
            )
        }
    }

    override suspend fun removeUserIdentifier(identifierId: UserIdentifierId) {
        lastRemovedIdentifierId = identifierId
        identifiersFlow.update { current ->
            val newItems = current.items.filterNot { it.id == identifierId }
            val removedCount = current.items.size - newItems.size
            current.copy(
                items = newItems,
                totalCount = (current.totalCount - removedCount).coerceAtLeast(0)
            )
        }
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
    ): PagedResult<UserSessionSummary> = sessionsFlow.value

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
    ): Flow<PagedResult<UserSessionSummary>> = sessionsFlow

    override suspend fun updateUserSessionsList(userSessionsList: PagedResult<UserSessionSummary>) {
        sessionsFlow.value = userSessionsList
    }

    override suspend fun updateUserSessionsPayloadList(userSessionsList: PagedResult<UserSessionSummaryPayload>) {
        lastUpdatedSessionsPayload = userSessionsList
        val mapped = userSessionsList.items.map { it.toUserSessionSummary() }
        sessionsFlow.value = pagedResultMock(
            items = mapped,
            totalCount = userSessionsList.totalCount,
            pageNumber = userSessionsList.pageNumber,
            pageSize = userSessionsList.pageSize,
            totalPages = userSessionsList.totalPages
        )
    }

    override suspend fun addUserSession(userSession: UserSessionSummary) {
        lastAddedSession = userSession
        sessionsFlow.update { current ->
            current.copy(
                items = current.items + userSession,
                totalCount = current.totalCount + 1
            )
        }
    }

    override suspend fun removeUserSession(sessionId: UserSessionId) {
        lastRemovedSessionId = sessionId
        removeUserSessions(listOf(sessionId))
    }

    override suspend fun removeUserSessions(sessionIds: List<UserSessionId>) {
        lastRemovedSessionsList = sessionIds
        sessionsFlow.update { current ->
            val newItems = current.items.filterNot { it.id in sessionIds }
            val removedCount = current.items.size - newItems.size
            current.copy(
                items = newItems,
                totalCount = (current.totalCount - removedCount).coerceAtLeast(0)
            )
        }
    }

    override suspend fun clear() {
        currentUserFlow.value = null
        identifiersFlow.value = pagedResultMock()
        sessionsFlow.value = pagedResultMock()
        isCleared = true
    }
}