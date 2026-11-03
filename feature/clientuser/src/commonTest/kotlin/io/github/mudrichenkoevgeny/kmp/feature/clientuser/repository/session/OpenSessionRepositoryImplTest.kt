package io.github.mudrichenkoevgeny.kmp.feature.clientuser.repository.session

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.api.session.SessionApiMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.auth.AuthStorageMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.user.UserStorageMock
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.DeletedSessionsPayload
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.common.domain.model.listing.createTestPagedResult
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.network.model.session.createTestUserSessionPrivatePayload
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.network.model.session.createTestUserSessionSummaryPayload
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

@InternalApi
class OpenSessionRepositoryImplTest {

    @Test
    fun `getSessions should return success from api`() = runTest {
        val payload = createTestUserSessionSummaryPayload()
        val paged = createTestPagedResult(listOf(payload))
        val api = SessionApiMock().apply {
            getSessionsResult = AppResult.Success(paged)
        }
        val storage = UserStorageMock()
        val authStorage = AuthStorageMock()
        val repository = OpenSessionRepositoryImpl(api, storage, authStorage)

        val result = repository.getSessions()

        assertIs<AppResult.Success<PagedResult<UserSessionSummary>>>(result)
        assertEquals(1, result.data.items.size)
    }

    @Test
    fun `getSession should return success from api`() = runTest {
        val payload = createTestUserSessionPrivatePayload()
        val api = SessionApiMock().apply {
            getSessionResult = AppResult.Success(payload)
        }
        val storage = UserStorageMock()
        val authStorage = AuthStorageMock()
        val repository = OpenSessionRepositoryImpl(api, storage, authStorage)

        val result = repository.getSession(UserSessionId.generate())

        assertIs<AppResult.Success<UserSessionPrivate>>(result)
        assertEquals(payload.id, result.data.id.value.toHexDashString())
    }

    @Test
    fun `logout should clear storage on success`() = runTest {
        val api = SessionApiMock().apply {
            logoutResult = AppResult.Success(Unit)
        }
        val storage = UserStorageMock()
        val authStorage = AuthStorageMock()
        val repository = OpenSessionRepositoryImpl(api, storage, authStorage)

        val result = repository.logout()

        assertIs<AppResult.Success<Unit>>(result)
        assertTrue(storage.isCleared)
    }

    @Test
    fun `deleteSession should return success from api`() = runTest {
        val sessionId = UserSessionId.generate()
        val api = SessionApiMock().apply {
            deleteSessionResult = AppResult.Success(Unit)
        }
        val storage = UserStorageMock()
        val authStorage = AuthStorageMock()
        val repository = OpenSessionRepositoryImpl(api, storage, authStorage)

        val result = repository.deleteSession(sessionId)

        assertIs<AppResult.Success<Unit>>(result)
    }

    @Test
    fun `deleteAllOtherSessions should return success from api`() = runTest {
        val api = SessionApiMock().apply {
            deleteAllOtherSessionsResult = AppResult.Success(DeletedSessionsPayload(emptyList()))
        }
        val storage = UserStorageMock()
        val authStorage = AuthStorageMock()
        val repository = OpenSessionRepositoryImpl(api, storage, authStorage)

        val result = repository.deleteAllOtherSessions()

        assertIs<AppResult.Success<Unit>>(result)
    }
}
