package io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.user

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.domain.model.listing.pagedResultMock
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.user.UserStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserDetails
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.identifier.toUserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.session.toUserSession
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.UserSessionPayload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

@InternalApi
class UserStorageMock : UserStorage {

    private val currentUserFlow = MutableStateFlow<UserDetails?>(null)
    private val identifiersFlow = MutableStateFlow(pagedResultMock<UserIdentifier>())
    private val sessionsFlow = MutableStateFlow(pagedResultMock<UserSession>())

    override suspend fun getCurrentUser(): UserDetails? = currentUserFlow.value

    override fun observeCurrentUser(): Flow<UserDetails?> = currentUserFlow

    override suspend fun updateCurrentUser(currentUser: UserDetails) {
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
    ): PagedResult<UserIdentifier> = identifiersFlow.value

    override fun observeUserIdentifiersList(
        pageNumber: Int?,
        pageSize: Int?,
        sortBy: UserSortValues.UserIdentifierSortBy?,
        sortOrder: SortOrder?,
        userIds: List<String>?,
        userAuthProviders: List<UserAuthProvider>?,
        identifiers: List<String>?
    ): Flow<PagedResult<UserIdentifier>> = identifiersFlow

    override suspend fun updateUserIdentifiersList(userIdentifiersList: PagedResult<UserIdentifier>) {
        identifiersFlow.value = userIdentifiersList
    }

    override suspend fun updateUserIdentifiersPayloadList(userIdentifiersList: PagedResult<UserIdentifierPayload>) {
        val mapped = userIdentifiersList.items.map { it.toUserIdentifier() }
        identifiersFlow.value = pagedResultMock(
            items = mapped,
            totalCount = userIdentifiersList.totalCount,
            pageNumber = userIdentifiersList.pageNumber,
            pageSize = userIdentifiersList.pageSize,
            totalPages = userIdentifiersList.totalPages
        )
    }

    override suspend fun addUserIdentifier(userIdentifier: UserIdentifier) {
        identifiersFlow.update { current ->
            current.copy(
                items = current.items + userIdentifier,
                totalCount = current.totalCount + 1
            )
        }
    }

    override suspend fun removeUserIdentifier(identifierId: UserIdentifierId) {
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
    ): PagedResult<UserSession> = sessionsFlow.value

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
    ): Flow<PagedResult<UserSession>> = sessionsFlow

    override suspend fun updateUserSessionsList(userSessionsList: PagedResult<UserSession>) {
        sessionsFlow.value = userSessionsList
    }

    override suspend fun updateUserSessionsPayloadList(userSessionsList: PagedResult<UserSessionPayload>) {
        val mapped = userSessionsList.items.map { it.toUserSession() }
        sessionsFlow.value = pagedResultMock(
            items = mapped,
            totalCount = userSessionsList.totalCount,
            pageNumber = userSessionsList.pageNumber,
            pageSize = userSessionsList.pageSize,
            totalPages = userSessionsList.totalPages
        )
    }

    override suspend fun addUserSession(userSession: UserSession) {
        sessionsFlow.update { current ->
            current.copy(
                items = current.items + userSession,
                totalCount = current.totalCount + 1
            )
        }
    }

    override suspend fun removeUserSession(sessionId: UserSessionId) {
        removeUserSessions(listOf(sessionId))
    }

    override suspend fun removeUserSessions(sessionIds: List<UserSessionId>) {
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
    }
}