package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.session

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.session.SelfManagementSessionApiMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.auth.AuthStorageMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.user.UserStorageMock
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.session.toUserSessionPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.session.toUserSessionSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.user.session.list.SelfSessionListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.DeletedSessionsPayload
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.common.domain.model.listing.createTestPagedResult
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.network.model.session.createTestUserSessionPrivatePayload
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.network.model.session.createTestUserSessionSummaryPayload
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
    private lateinit var authStorage: AuthStorageMock
    private lateinit var repo: SelfManagementSessionRepositoryImpl

    @BeforeTest
    fun setUp() {
        api = SelfManagementSessionApiMock()
        storage = UserStorageMock()
        authStorage = AuthStorageMock()
        repo = SelfManagementSessionRepositoryImpl(api, storage, authStorage)
    }

    @Test
    fun `getSessions fetches from network`() = runTest {
        val payload = createTestUserSessionSummaryPayload()
        val wire = createTestPagedResult(items = listOf(payload), totalCount = 1)
        api.getSessionsResult = AppResult.Success(wire)

        val result = repo.getSessions(
            params = SelfSessionListParams(
                pageNumber = 1,
                pageSize = 20
            )
        )

        val success = assertIs<AppResult.Success<PagedResult<UserSessionSummary>>>(result)
        assertEquals(payload.toUserSessionSummary(), success.data.items.first())
    }

    @Test
    fun `logout clears storage on success`() = runTest {
        api.logoutResult = AppResult.Success(Unit)

        val result = repo.logout()

        assertIs<AppResult.Success<Unit>>(result)
        assertEquals(true, storage.isCleared)
    }

    @Test
    fun `deleteSession returns success on success`() = runTest {
        val sessionId = UserSessionId(Uuid.random())
        api.deleteSessionResult = AppResult.Success(Unit)

        val result = repo.deleteSession(sessionId)

        assertIs<AppResult.Success<Unit>>(result)
    }

    @Test
    fun `getSession fetches from network`() = runTest {
        val payload = createTestUserSessionPrivatePayload()
        api.getSessionResult = AppResult.Success(payload)
        val sessionId = UserSessionId(Uuid.random())

        val result = repo.getSession(sessionId)

        val success = assertIs<AppResult.Success<UserSessionPrivate>>(result)
        assertEquals(payload.toUserSessionPrivate(), success.data)
    }

    @Test
    fun `deleteAllOtherSessions returns success`() = runTest {
        api.deleteAllOtherSessionsResult = AppResult.Success(DeletedSessionsPayload(emptyList()))

        val result = repo.deleteAllOtherSessions()

        assertIs<AppResult.Success<Unit>>(result)
    }
}
