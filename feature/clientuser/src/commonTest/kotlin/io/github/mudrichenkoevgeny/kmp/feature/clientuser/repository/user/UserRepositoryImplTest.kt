package io.github.mudrichenkoevgeny.kmp.feature.clientuser.repository.user

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.network.model.websocket.socketFrameMock
import io.github.mudrichenkoevgeny.kmp.core.common.mock.network.websocket.service.WebSocketServiceMock
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.mock.network.api.user.OpenUserApiMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.user.userDetailsPayloadMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.auth.AuthStorageMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.user.UserStorageMock
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.token.AccessToken
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.token.RefreshToken
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserDetails
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.user.toUserDetails
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.contract.UserWebSocketEventTypes
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.user.UserDetailsPayload
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.encodeToJsonElement
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertSame
import kotlin.time.Instant

@InternalApi
class UserRepositoryImplTest {

    private val userStorage = UserStorageMock()
    private val authStorage = AuthStorageMock()
    private val userApi = OpenUserApiMock()
    private val webSocketService = WebSocketServiceMock()

    private fun createRepository(scope: TestScope): OpenUserRepositoryImpl {
        val repo = OpenUserRepositoryImpl(
            userStorage = userStorage,
            authStorage = authStorage,
            openUserApi = userApi,
            webSocketService = webSocketService,
            repositoryScope = scope.backgroundScope
        )
        return repo
    }

    @Test
    fun `currentUser reflects storage flow`() = runTest {
        val repo = createRepository(this)
        assertSame(userStorage.observeCurrentUser(), repo.currentUser)
    }

    @Test
    fun `refreshCurrentUser fetches from api and updates storage`() = runTest {
        val wire = userDetailsPayloadMock()
        userApi.getUserResult = AppResult.Success(wire)
        val repo = createRepository(this)

        val result = repo.refreshCurrentUser()

        assertIs<AppResult.Success<UserDetails>>(result)
        val expected = wire.toUserDetails()
        assertEquals(expected, result.data)
        assertEquals(expected, userStorage.getCurrentUser())
    }

    @Test
    fun `scheduleUserDeletion fetches from api and updates storage`() = runTest {
        val wire = userDetailsPayloadMock()
        userApi.scheduleUserDeletionResult = AppResult.Success(wire)
        val repo = createRepository(this)

        val result = repo.scheduleUserDeletion()

        assertIs<AppResult.Success<UserDetails>>(result)
        val expected = wire.toUserDetails()
        assertEquals(expected, result.data)
        assertEquals(expected, userStorage.getCurrentUser())
    }

    @Test
    fun `restoreUser fetches from api and updates storage`() = runTest {
        val wire = userDetailsPayloadMock()
        userApi.restoreUserResult = AppResult.Success(wire)
        val repo = createRepository(this)

        val result = repo.restoreUser()

        assertIs<AppResult.Success<UserDetails>>(result)
        val expected = wire.toUserDetails()
        assertEquals(expected, result.data)
        assertEquals(expected, userStorage.getCurrentUser())
    }

    @Test
    fun `USER_UPDATED web socket event updates storage`() = runTest {
        createRepository(this)
        runCurrent()

        val pushed = userDetailsPayloadMock()
        val frame = socketFrameMock(
            type = UserWebSocketEventTypes.USER_UPDATED,
            payload = FoundationJson.encodeToJsonElement<UserDetailsPayload>(pushed)
        )

        webSocketService.emit(frame)
        runCurrent()
        advanceUntilIdle()

        val expected = pushed.toUserDetails()
        val actual = userStorage.getCurrentUser()

        assertEquals(expected, actual)
    }

    @Test
    fun `SESSION_DELETED web socket event clears storage and auth`() = runTest {
        createRepository(this)
        runCurrent()

        userStorage.updateCurrentUser(userDetailsPayloadMock().toUserDetails())

        authStorage.updateTokens(
            accessToken = AccessToken("at"),
            refreshToken = RefreshToken("rt"),
            expiresAt = Instant.fromEpochMilliseconds(0)
        )

        val frame = socketFrameMock(type = UserWebSocketEventTypes.SESSION_DELETED)

        webSocketService.emit(frame)
        runCurrent()
        advanceUntilIdle()

        assertEquals(null, userStorage.getCurrentUser())
        assertEquals(null, authStorage.getAccessToken())
        assertEquals(null, authStorage.getRefreshToken())
    }
}