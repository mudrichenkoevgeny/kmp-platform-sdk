package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.session

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.domain.model.listing.pagedResultMock
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.session.ManagementSessionApiMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.session.userSessionPayloadMock
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.session.toUserSession
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.uuid.Uuid

@InternalApi
class ManagementSessionRepositoryImplTest {

    private lateinit var api: ManagementSessionApiMock
    private lateinit var repo: ManagementSessionRepositoryImpl

    @BeforeTest
    fun setUp() {
        api = ManagementSessionApiMock()
        repo = ManagementSessionRepositoryImpl(api)
    }

    @Test
    fun `getSessions forwards request and maps paged result`() = runTest {
        val payload = userSessionPayloadMock()
        val wire = pagedResultMock(items = listOf(payload), totalCount = 1)
        api.getSessionsResult = AppResult.Success(wire)

        val result = repo.getSessions(
            pageNumber = 1,
            pageSize = 20,
            sortBy = null,
            sortOrder = null,
            userIds = null,
            userRoles = null,
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
        assertEquals(1, success.data.items.size)
        assertEquals(payload.toUserSession(), success.data.items.first())
    }

    @Test
    fun `getSession forwards request and maps result`() = runTest {
        val payload = userSessionPayloadMock()
        api.getSessionResult = AppResult.Success(payload)

        val result = repo.getSession("session-1")

        val success = assertIs<AppResult.Success<UserSession>>(result)
        assertEquals(payload.toUserSession(), success.data)
    }

    @Test
    fun `deleteSession forwards request`() = runTest {
        api.deleteSessionResult = AppResult.Success(Unit)
        val userId = UserId(Uuid.random())

        val result = repo.deleteSession(userId, "session-1")

        assertIs<AppResult.Success<Unit>>(result)
    }

    @Test
    fun `deleteAllUserSessions forwards request`() = runTest {
        api.deleteAllUserSessionsResult = AppResult.Success(Unit)
        val userId = UserId(Uuid.random())

        val result = repo.deleteAllUserSessions(userId)

        assertIs<AppResult.Success<Unit>>(result)
    }

    @Test
    fun `repository propagates api errors`() = runTest {
        api.getSessionResult = AppResult.Error(CommonError.Unknown())

        val result = repo.getSession("session-1")

        assertIs<AppResult.Error>(result)
    }
}