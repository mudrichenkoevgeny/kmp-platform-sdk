package io.github.mudrichenkoevgeny.kmp.feature.clientuser.repository.session

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.session.userSessionMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.session.userSessionPayloadMock
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.session.SessionApi
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.user.UserStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.verifytotp.VerifyTotpPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserDetails
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.DeletedSessionsPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.UserSessionPayload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

@InternalApi
class OpenSessionRepositoryImplTest {

    private fun pagedPayload(items: List<UserSessionPayload>) = PagedResult(
        items = items,
        totalCount = items.size.toLong(),
        pageNumber = 1,
        pageSize = 20,
        totalPages = 1
    )

    @Test
    fun `getSessions should update storage on success`() = runTest {
        val payload = userSessionPayloadMock()
        val paged = pagedPayload(listOf(payload))
        val api = FakeSessionApi(sessionsResult = AppResult.Success(paged))
        val storage = FakeUserStorage()
        val repository = OpenSessionRepositoryImpl(api, storage)

        val result = repository.getSessions()

        assertIs<AppResult.Success<PagedResult<UserSession>>>(result)
        assertEquals(paged, storage.lastUpdatedSessionsPayload)
    }

    @Test
    fun `getSession should add to storage on success`() = runTest {
        val payload = userSessionPayloadMock()
        val api = FakeSessionApi(sessionResult = AppResult.Success(payload))
        val storage = FakeUserStorage()
        val repository = OpenSessionRepositoryImpl(api, storage)

        val result = repository.getSession(UserSessionId.generate())

        assertIs<AppResult.Success<UserSession>>(result)
        assertEquals(payload.id, storage.lastAddedSession?.id?.value?.toHexDashString())
    }

    @Test
    fun `logout should clear storage on success`() = runTest {
        val api = FakeSessionApi(logoutResult = AppResult.Success(Unit))
        val storage = FakeUserStorage()
        val repository = OpenSessionRepositoryImpl(api, storage)

        val result = repository.logout()

        assertIs<AppResult.Success<Unit>>(result)
        assertTrue(storage.wasCleared)
    }

    @Test
    fun `deleteSession should remove from storage on success`() = runTest {
        val sessionId = UserSessionId.generate()
        val api = FakeSessionApi(deleteResult = AppResult.Success(Unit))
        val storage = FakeUserStorage()
        val repository = OpenSessionRepositoryImpl(api, storage)

        val result = repository.deleteSession(sessionId)

        assertIs<AppResult.Success<Unit>>(result)
        assertEquals(sessionId, storage.lastRemovedSessionId)
    }

    @Test
    fun `deleteAllOtherSessions should remove deleted IDs from storage`() = runTest {
        val id1 = UserSessionId.generate()
        val deletedIds = listOf(id1.value.toHexDashString())
        
        val api = FakeSessionApi(deleteAllOtherResult = AppResult.Success(DeletedSessionsPayload(deletedIds)))
        val storage = FakeUserStorage().apply {
            sessionsInCache = listOf(userSessionMock().copy(id = id1))
        }
        val repository = OpenSessionRepositoryImpl(api, storage)

        val result = repository.deleteAllOtherSessions()

        assertIs<AppResult.Success<Unit>>(result)
        assertEquals(listOf(id1), storage.lastRemovedSessionsList)
    }

    private class FakeSessionApi(
        private val sessionsResult: AppResult<PagedResult<UserSessionPayload>> = AppResult.Success(PagedResult(emptyList(), 0, 1, 20, 0)),
        private val sessionResult: AppResult<UserSessionPayload> = AppResult.Error(CommonError.Unknown()),
        private val logoutResult: AppResult<Unit> = AppResult.Success(Unit),
        private val deleteResult: AppResult<Unit> = AppResult.Success(Unit),
        private val deleteAllOtherResult: AppResult<DeletedSessionsPayload> = AppResult.Success(DeletedSessionsPayload(emptyList()))
    ) : SessionApi {
        override suspend fun getSessions(pageNumber: Int?, pageSize: Int?, sortBy: UserSortValues.UserSessionSortBy?, sortOrder: SortOrder?, identifiers: List<String>?, identifierIds: List<String>?, userAuthProviders: List<UserAuthProvider>?, clientTypes: List<ClientType>?, userAgents: List<String>?, ipAddresses: List<String>?, languages: List<String>?, deviceIds: List<String>?, deviceNames: List<String>?, appVersions: List<String>?, operationSystemVersions: List<String>?): AppResult<PagedResult<UserSessionPayload>> = sessionsResult
        override suspend fun getSession(userSessionId: UserSessionId): AppResult<UserSessionPayload> = sessionResult
        override suspend fun logout(): AppResult<Unit> = logoutResult
        override suspend fun deleteSession(userSessionId: UserSessionId): AppResult<Unit> = deleteResult
        override suspend fun deleteAllOtherSessions(): AppResult<DeletedSessionsPayload> = deleteAllOtherResult
        override suspend fun reauthenticateSession(request: VerifyTotpPayload): AppResult<Unit> = error("N/A")
    }

    private class FakeUserStorage : UserStorage {
        var lastUpdatedSessionsPayload: PagedResult<UserSessionPayload>? = null
        var lastAddedSession: UserSession? = null
        var lastRemovedSessionId: UserSessionId? = null
        var lastRemovedSessionsList: List<UserSessionId>? = null
        var wasCleared = false
        var sessionsInCache: List<UserSession> = emptyList()

        override suspend fun getCurrentUser() = error("N/A")
        override fun observeCurrentUser() = error("N/A")
        override suspend fun updateCurrentUser(currentUser: UserDetails) = Unit
        override suspend fun getUserIdentifiersList(pageNumber: Int?, pageSize: Int?, sortBy: UserSortValues.UserIdentifierSortBy?, sortOrder: SortOrder?, userIds: List<String>?, userAuthProviders: List<UserAuthProvider>?, identifiers: List<String>?) = error("N/A")
        override fun observeUserIdentifiersList(pageNumber: Int?, pageSize: Int?, sortBy: UserSortValues.UserIdentifierSortBy?, sortOrder: SortOrder?, userIds: List<String>?, userAuthProviders: List<UserAuthProvider>?, identifiers: List<String>?) = error("N/A")
        override suspend fun updateUserIdentifiersList(userIdentifiersList: PagedResult<UserIdentifier>) = Unit
        override suspend fun updateUserIdentifiersPayloadList(userIdentifiersList: PagedResult<UserIdentifierPayload>) = Unit
        override suspend fun addUserIdentifier(userIdentifier: UserIdentifier) = Unit
        override suspend fun removeUserIdentifier(identifierId: UserIdentifierId) = Unit
        
        override suspend fun getUserSessionsList(pageNumber: Int?, pageSize: Int?, sortBy: UserSortValues.UserSessionSortBy?, sortOrder: SortOrder?, userIds: List<String>?, userRoles: List<UserRole>?, identifiers: List<String>?, identifierIds: List<String>?, userAuthProviders: List<UserAuthProvider>?, clientTypes: List<ClientType>?, userAgents: List<String>?, ipAddresses: List<String>?, languages: List<String>?, deviceIds: List<String>?, deviceNames: List<String>?, appVersions: List<String>?, operationSystemVersions: List<String>?): PagedResult<UserSession> {
            return PagedResult(
                items = sessionsInCache,
                totalCount = sessionsInCache.size.toLong(),
                pageNumber = 1,
                pageSize = 20,
                totalPages = 1
            )
        }
        override fun observeUserSessionsList(pageNumber: Int?, pageSize: Int?, sortBy: UserSortValues.UserSessionSortBy?, sortOrder: SortOrder?, userIds: List<String>?, userRoles: List<UserRole>?, identifiers: List<String>?, identifierIds: List<String>?, userAuthProviders: List<UserAuthProvider>?, clientTypes: List<ClientType>?, userAgents: List<String>?, ipAddresses: List<String>?, languages: List<String>?, deviceIds: List<String>?, deviceNames: List<String>?, appVersions: List<String>?, operationSystemVersions: List<String>?) = error("N/A")
        override suspend fun updateUserSessionsList(userSessionsList: PagedResult<UserSession>) = Unit
        override suspend fun updateUserSessionsPayloadList(userSessionsList: PagedResult<UserSessionPayload>) {
            lastUpdatedSessionsPayload = userSessionsList
        }
        override suspend fun addUserSession(userSession: UserSession) {
            lastAddedSession = userSession
        }
        override suspend fun removeUserSession(sessionId: UserSessionId) {
            lastRemovedSessionId = sessionId
        }
        override suspend fun removeUserSessions(sessionIds: List<UserSessionId>) {
            lastRemovedSessionsList = sessionIds
        }
        override suspend fun clear() {
            wasCleared = true
        }
    }
}
