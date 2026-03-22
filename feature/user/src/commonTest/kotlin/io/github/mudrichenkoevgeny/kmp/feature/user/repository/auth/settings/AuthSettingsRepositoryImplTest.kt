package io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mapper.auth.settings.toAuthSettings
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.auth.MockAuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.settings.AuthSettings
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.settings.AuthSettingsApi
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.wireAuthSettingsResponse
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.model.websocket.SocketFrame
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.contract.UserWebSocketEventTypes
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.auth.settings.AuthSettingsResponse
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.auth.settings.AvailableAuthProvidersResponse
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.JsonElement
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

private const val FRAME_ID = "123e4567-e89b-12d3-a456-426614174000"
private const val SOCKET_FRAME_TIMESTAMP_MS = 0L
private const val NOT_RETRYABLE = false

private class FakeWebSocketService : WebSocketService {
    private val events = Channel<SocketFrame>(Channel.UNLIMITED)

    override fun observeEvents() = events.receiveAsFlow()

    suspend fun emit(frame: SocketFrame) {
        events.send(frame)
    }

    override fun connect() = Unit

    override fun disconnect() = Unit

    override fun restart() = Unit

    override fun updateWebSocketMessageHandlers(webSocketMessageHandlers: List<WebSocketMessageHandler>) = Unit

    override suspend fun sendEvent(
        type: String,
        payload: JsonElement?,
        metadata: Map<String, String>
    ) = Unit

    override suspend fun sendPing(metadata: Map<String, String>) = Unit
}

private class FakeAuthSettingsApi : AuthSettingsApi {
    var result: AppResult<AuthSettingsResponse> = AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE))
    var callCount = 0

    override suspend fun getAuthSettings(): AppResult<AuthSettingsResponse> {
        callCount++
        return result
    }
}

class AuthSettingsRepositoryImplTest {

    @Test
    fun getAuthSettings_returnsMemoryWithoutApi_whenPreloadedFromStorage() = runTest {
        val persisted = wireAuthSettingsResponse().toAuthSettings()
        val storage = MockAuthStorage()
        storage.updateAuthSettings(persisted)
        val api = FakeAuthSettingsApi().apply {
            result = AppResult.Success(wireAuthSettingsResponse())
        }

        val repo = AuthSettingsRepositoryImpl(
            authSettingsApi = api,
            authStorage = storage,
            webSocketService = FakeWebSocketService(),
            repositoryScope = backgroundScope
        )
        advanceUntilIdle()

        val settingsResult = repo.getAuthSettings()
        assertIs<AppResult.Success<AuthSettings>>(settingsResult)
        assertEquals(persisted, settingsResult.data)
        assertEquals(0, api.callCount)
    }

    @Test
    fun getAuthSettings_fetchesFromApi_whenNothingInStorage() = runTest {
        val storage = MockAuthStorage()
        val wire = wireAuthSettingsResponse()
        val api = FakeAuthSettingsApi().apply { result = AppResult.Success(wire) }

        val repo = AuthSettingsRepositoryImpl(
            authSettingsApi = api,
            authStorage = storage,
            webSocketService = FakeWebSocketService(),
            repositoryScope = backgroundScope
        )
        advanceUntilIdle()

        val settingsResult = repo.getAuthSettings()
        assertIs<AppResult.Success<AuthSettings>>(settingsResult)
        assertEquals(wire.toAuthSettings(), settingsResult.data)
        assertEquals(wire.toAuthSettings(), storage.getAuthSettings())
        assertEquals(1, api.callCount)
    }

    @Test
    fun refreshAuthSettings_alwaysCallsApi() = runTest {
        val storage = MockAuthStorage()
        storage.updateAuthSettings(wireAuthSettingsResponse().toAuthSettings())
        val fresh = wireAuthSettingsResponse()
        val api = FakeAuthSettingsApi().apply { result = AppResult.Success(fresh) }

        val repo = AuthSettingsRepositoryImpl(
            authSettingsApi = api,
            authStorage = storage,
            webSocketService = FakeWebSocketService(),
            repositoryScope = backgroundScope
        )
        advanceUntilIdle()

        val refreshed = repo.refreshAuthSettings()
        assertIs<AppResult.Success<AuthSettings>>(refreshed)
        assertEquals(fresh.toAuthSettings(), refreshed.data)
        assertEquals(fresh.toAuthSettings(), storage.getAuthSettings())
        assertTrue(api.callCount >= 1)
    }

    @Test
    fun authSettingsUpdatedWebSocketEvent_persistsAndUpdatesObservableState() = runTest {
        val storage = MockAuthStorage()
        val initialWire = wireAuthSettingsResponse()
        val api = FakeAuthSettingsApi().apply { result = AppResult.Success(initialWire) }
        val sockets = FakeWebSocketService()

        val repo = AuthSettingsRepositoryImpl(
            authSettingsApi = api,
            authStorage = storage,
            webSocketService = sockets,
            repositoryScope = backgroundScope
        )
        advanceUntilIdle()

        assertIs<AppResult.Success<AuthSettings>>(repo.getAuthSettings())
        assertEquals(initialWire.toAuthSettings(), storage.getAuthSettings())

        val pushed = AuthSettingsResponse(
            availableAuthProviders = AvailableAuthProvidersResponse(
                primary = listOf(
                    UserAuthProvider.EMAIL.serialName,
                    UserAuthProvider.GOOGLE.serialName
                ),
                secondary = emptyList()
            )
        )
        val frame = SocketFrame(
            id = FRAME_ID,
            type = UserWebSocketEventTypes.AUTH_SETTINGS_UPDATED,
            payload = FoundationJson.encodeToJsonElement(AuthSettingsResponse.serializer(), pushed),
            metadata = emptyMap(),
            timestamp = SOCKET_FRAME_TIMESTAMP_MS
        )
        sockets.emit(frame)
        runCurrent()
        advanceUntilIdle()

        assertEquals(pushed.toAuthSettings(), storage.getAuthSettings())
        val after = repo.getAuthSettings()
        assertIs<AppResult.Success<AuthSettings>>(after)
        assertEquals(pushed.toAuthSettings(), after.data)
    }
}
