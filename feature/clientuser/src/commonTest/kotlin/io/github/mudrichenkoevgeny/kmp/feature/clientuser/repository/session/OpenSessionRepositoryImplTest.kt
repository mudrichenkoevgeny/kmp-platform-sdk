package io.github.mudrichenkoevgeny.kmp.feature.clientuser.repository.session

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.session.userSessionMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.api.session.SessionApiMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.session.userSessionPayloadMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.user.UserStorageMock
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.DeletedSessionsPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.UserSessionPayload
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
        val api = SessionApiMock().apply {
            getSessionsResult = AppResult.Success(paged)
        }
        val storage = UserStorageMock()
        val repository = OpenSessionRepositoryImpl(api, storage)

        val result = repository.getSessions()

        assertIs<AppResult.Success<PagedResult<UserSession>>>(result)
        assertEquals(paged, storage.lastUpdatedSessionsPayload)
    }

    @Test
    fun `getSession should add to storage on success`() = runTest {
        val payload = userSessionPayloadMock()
        val api = SessionApiMock().apply {
            getSessionResult = AppResult.Success(payload)
        }
        val storage = UserStorageMock()
        val repository = OpenSessionRepositoryImpl(api, storage)

        val result = repository.getSession(UserSessionId.generate())

        assertIs<AppResult.Success<UserSession>>(result)
        assertEquals(payload.id, storage.lastAddedSession?.id?.value?.toHexDashString())
    }

    @Test
    fun `logout should clear storage on success`() = runTest {
        val api = SessionApiMock().apply {
            logoutResult = AppResult.Success(Unit)
        }
        val storage = UserStorageMock()
        val repository = OpenSessionRepositoryImpl(api, storage)

        val result = repository.logout()

        assertIs<AppResult.Success<Unit>>(result)
        assertTrue(storage.isCleared)
    }

    @Test
    fun `deleteSession should remove from storage on success`() = runTest {
        val sessionId = UserSessionId.generate()
        val api = SessionApiMock().apply {
            deleteSessionResult = AppResult.Success(Unit)
        }
        val storage = UserStorageMock()
        val repository = OpenSessionRepositoryImpl(api, storage)

        val result = repository.deleteSession(sessionId)

        assertIs<AppResult.Success<Unit>>(result)
        assertEquals(sessionId, storage.lastRemovedSessionId)
    }

    @Test
    fun `deleteAllOtherSessions should remove deleted IDs from storage`() = runTest {
        val id1 = UserSessionId.generate()
        val deletedIds = listOf(id1.value.toHexDashString())
        
        val api = SessionApiMock().apply {
            deleteAllOtherSessionsResult = AppResult.Success(DeletedSessionsPayload(deletedIds))
        }
        val storage = UserStorageMock()
        storage.addUserSession(userSessionMock().copy(id = id1))
        val repository = OpenSessionRepositoryImpl(api, storage)

        val result = repository.deleteAllOtherSessions()

        assertIs<AppResult.Success<Unit>>(result)
        assertEquals(listOf(id1), storage.lastRemovedSessionsList)
    }
}
