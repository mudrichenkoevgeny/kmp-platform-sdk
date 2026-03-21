package io.github.mudrichenkoevgeny.kmp.core.settings.repository.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.settings.mapper.globalsettings.toGlobalSettings
import io.github.mudrichenkoevgeny.kmp.core.settings.model.globalsettings.GlobalSettings
import io.github.mudrichenkoevgeny.kmp.core.settings.network.api.globalsettings.GlobalSettingsApi
import io.github.mudrichenkoevgeny.kmp.core.settings.storage.globalsettings.GlobalSettingsStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.model.websocket.SocketFrame
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.contract.SettingsWebSocketEventTypes
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.response.GlobalSettingsResponse
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

private class FakeGlobalSettingsStorage : GlobalSettingsStorage {
    var stored: GlobalSettings? = null

    override suspend fun getGlobalSettings(): GlobalSettings? = stored

    override suspend fun updateGlobalSettings(globalSettings: GlobalSettings) {
        stored = globalSettings
    }

    override suspend fun clearGlobalSettings() {
        stored = null
    }
}

private class FakeGlobalSettingsApi : GlobalSettingsApi {
    var result: AppResult<GlobalSettingsResponse> = AppResult.Error(CommonError.Unknown())
    var callCount = 0

    override suspend fun getGlobalSettings(): AppResult<GlobalSettingsResponse> {
        callCount++
        return result
    }
}

private class FakeWebSocketService : WebSocketService {
    private val events = Channel<SocketFrame>(Channel.UNLIMITED)

    override fun observeEvents() = events.receiveAsFlow()

    suspend fun emit(frame: SocketFrame) {
        events.send(frame)
    }

    override fun connect() {}

    override fun disconnect() {}

    override fun restart() {}

    override fun updateWebSocketMessageHandlers(webSocketMessageHandlers: List<WebSocketMessageHandler>) {}

    override suspend fun sendEvent(
        type: String,
        payload: JsonElement?,
        metadata: Map<String, String>
    ) {
    }

    override suspend fun sendPing(metadata: Map<String, String>) {}
}

private fun wire(
    privacy: String? = "https://privacy",
    terms: String? = "https://terms",
    email: String? = "a@b.c"
) = GlobalSettingsResponse(
    privacyPolicyUrl = privacy,
    termsOfServiceUrl = terms,
    contactSupportEmail = email
)

class GlobalSettingsRepositoryImplTest {

    @Test
    fun `getGlobalSettings uses in memory cache without calling api when preloaded from storage`() =
        runTest {
            val persisted = GlobalSettings(
                privacyPolicyUrl = "p",
                termsOfServiceUrl = "t",
                contactSupportEmail = "e"
            )
            val storage = FakeGlobalSettingsStorage().apply { stored = persisted }
            val api = FakeGlobalSettingsApi().apply {
                result = AppResult.Success(wire())
            }

            val repo = GlobalSettingsRepositoryImpl(
                globalSettingsApi = api,
                globalSettingsStorage = storage,
                webSocketService = FakeWebSocketService(),
                repositoryScope = backgroundScope
            )
            advanceUntilIdle()

            val r = repo.getGlobalSettings()
            assertIs<AppResult.Success<GlobalSettings>>(r)
            assertEquals(persisted, r.data)
            assertEquals(0, api.callCount)
        }

    @Test
    fun `getGlobalSettings fetches from api when nothing in storage`() = runTest {
        val storage = FakeGlobalSettingsStorage()
        val response = wire()
        val api = FakeGlobalSettingsApi().apply {
            result = AppResult.Success(response)
        }

        val repo = GlobalSettingsRepositoryImpl(
            globalSettingsApi = api,
            globalSettingsStorage = storage,
            webSocketService = FakeWebSocketService(),
            repositoryScope = backgroundScope
        )
        advanceUntilIdle()

        val r = repo.getGlobalSettings()
        assertIs<AppResult.Success<GlobalSettings>>(r)
        assertEquals(response.toGlobalSettings(), r.data)
        assertEquals(response.toGlobalSettings(), storage.stored)
        assertEquals(1, api.callCount)
    }

    @Test
    fun `refreshGlobalSettings always calls api`() = runTest {
        val storage = FakeGlobalSettingsStorage().apply {
            stored = GlobalSettings("old-p", "old-t", "old-e")
        }
        val fresh = wire(privacy = "new-p", terms = "new-t", email = "new-e")
        val api = FakeGlobalSettingsApi().apply {
            result = AppResult.Success(fresh)
        }

        val repo = GlobalSettingsRepositoryImpl(
            globalSettingsApi = api,
            globalSettingsStorage = storage,
            webSocketService = FakeWebSocketService(),
            repositoryScope = backgroundScope
        )
        advanceUntilIdle()

        val r = repo.refreshGlobalSettings()
        assertIs<AppResult.Success<GlobalSettings>>(r)
        assertEquals(fresh.toGlobalSettings(), r.data)
        assertEquals(fresh.toGlobalSettings(), storage.stored)
        assertTrue(api.callCount >= 1)
    }

    @Test
    fun `global settings updated websocket event persists and updates flow`() = runTest {
        val storage = FakeGlobalSettingsStorage()
        val initialWire = wire(privacy = "first")
        val api = FakeGlobalSettingsApi().apply {
            result = AppResult.Success(initialWire)
        }
        val sockets = FakeWebSocketService()

        val repo = GlobalSettingsRepositoryImpl(
            globalSettingsApi = api,
            globalSettingsStorage = storage,
            webSocketService = sockets,
            repositoryScope = backgroundScope
        )
        advanceUntilIdle()

        assertIs<AppResult.Success<GlobalSettings>>(repo.getGlobalSettings())
        assertEquals(initialWire.toGlobalSettings(), storage.stored)

        val pushed = wire(privacy = "ws-p", terms = "ws-t", email = "ws@e")
        val frame = SocketFrame(
            id = FRAME_ID,
            type = SettingsWebSocketEventTypes.GLOBAL_SETTINGS_UPDATED,
            payload = FoundationJson.encodeToJsonElement(
                GlobalSettingsResponse.serializer(),
                pushed
            ),
            metadata = emptyMap(),
            timestamp = 0L
        )
        sockets.emit(frame)
        runCurrent()
        advanceUntilIdle()

        assertEquals(pushed.toGlobalSettings(), storage.stored)
        val after = repo.getGlobalSettings()
        assertIs<AppResult.Success<GlobalSettings>>(after)
        assertEquals(pushed.toGlobalSettings(), after.data)
    }
}
