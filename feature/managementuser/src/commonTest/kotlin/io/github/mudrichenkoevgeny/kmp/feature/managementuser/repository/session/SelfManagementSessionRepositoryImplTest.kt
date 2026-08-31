package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.session

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.domain.model.listing.pagedResultMock
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.session.SelfManagementSessionApiMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.session.userSessionPayloadMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.user.UserStorageMock
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.session.toUserSession
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.uuid.Uuid

@InternalApi
class SelfManagementSessionRepositoryImplTest {

    private lateinit var api: SelfManagementSessionApiMock
    private lateinit var storage: UserStorageMock
    private lateinit var repo: SelfManagementSessionRepositoryImpl

    @BeforeTest
    fun setUp() {
        api = SelfManagementSessionApiMock()
        storage = UserStorageMock()
        repo = SelfManagementSessionRepositoryImpl(api, storage)
    }

    @Test
    fun `getSessions fetches from network and updates cache`() = runTest {
        val payload = userSessionPayloadMock()
        val wire = pagedResultMock(items = listOf(payload), totalCount = 1)
        api.getSessionsResult = AppResult.Success(wire)

        val result = repo.getSessions(
            pageNumber = 1, pageSize = 20, sortBy = null, sortOrder = null,
            identifiers = null, identifierIds = null, userAuthProviders = null,
            clientTypes = null, userAgents = null, ipAddresses = null,
            languages = null, deviceIds = null, deviceNames = null,
            appVersions = null, operationSystemVersions = null
        )

        val success = assertIs<AppResult.Success<PagedResult<UserSession>>>(result)
        assertEquals(payload.toUserSession(), success.data.items.first())
        assertEquals(1, storage.getUserSessionsList().items.size)
    }

    @Test
    fun `getSessions returns cache when network fails`() = runTest {
        val session = userSessionPayloadMock().toUserSession()
        storage.updateUserSessionsList(pagedResultMock(items = listOf(session)))
        api.getSessionsResult = AppResult.Error(CommonError.Unknown())

        val result = repo.getSessions(
            pageNumber = 1,
            pageSize = 20,
            sortBy = null,
            sortOrder = null,
            identifiers = null,
            identifierIds = null,
            userAuthProviders = null,
            clientTypes = null,
            userAgents = null,
            ipAddresses = null,
            languages = null,
            deviceIds = null,
            deviceNames = null,
            appVersions = null,
            operationSystemVersions = null
        )

        val success = assertIs<AppResult.Success<PagedResult<UserSession>>>(result)
        assertEquals(session, success.data.items.first())
    }

    @Test
    fun `logout clears storage on success`() = runTest {
        api.logoutResult = AppResult.Success(Unit)
        storage.addUserSession(userSessionPayloadMock().toUserSession())

        val result = repo.logout()

        assertIs<AppResult.Success<Unit>>(result)
        assertEquals(0, storage.getUserSessionsList().items.size)
    }

    @Test
    fun `deleteSession removes from storage on success`() = runTest {
        val sessionId = UserSessionId(Uuid.random())
        api.deleteSessionResult = AppResult.Success(Unit)

        val result = repo.deleteSession(sessionId)

        assertIs<AppResult.Success<Unit>>(result)
    }

    @Test
    fun `getSession fetches from network and updates cache`() = runTest {
        val payload = userSessionPayloadMock()
        api.getSessionResult = AppResult.Success(payload)
        val sessionId = UserSessionId(Uuid.random())

        val result = repo.getSession(sessionId)

        val success = assertIs<AppResult.Success<UserSession>>(result)
        assertEquals(payload.toUserSession(), success.data)

        val cached = repo.getSession(sessionId)
        assertEquals(success.data, (cached as AppResult.Success).data)
    }

    @Test
    fun `deleteAllOtherSessions removes IDs from storage`() = runTest {
        val id1 = UserSessionId(Uuid.random())
        val deletedIds = listOf(id1.value.toHexDashString())
        api.deleteAllOtherSessionsResult = AppResult.Success(
            io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.DeletedSessionsPayload(deletedIds)
        )
        
        storage.addUserSession(userSessionPayloadMock().toUserSession().copy(id = id1))
        assertEquals(1, storage.getUserSessionsList().items.size)

        val result = repo.deleteAllOtherSessions()

        assertIs<AppResult.Success<Unit>>(result)
        assertEquals(0, storage.getUserSessionsList().items.size)
    }
}