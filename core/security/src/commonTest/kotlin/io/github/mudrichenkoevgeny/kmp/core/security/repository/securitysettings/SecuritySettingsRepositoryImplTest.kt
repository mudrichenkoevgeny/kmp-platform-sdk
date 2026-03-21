package io.github.mudrichenkoevgeny.kmp.core.security.repository.securitysettings

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.mapper.securitysettings.toSecuritySettings
import io.github.mudrichenkoevgeny.kmp.core.security.model.securitysettings.SecuritySettings
import io.github.mudrichenkoevgeny.kmp.core.security.network.api.securitysettings.SecuritySettingsApi
import io.github.mudrichenkoevgeny.kmp.core.security.storage.securitysettings.SecuritySettingsStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.model.websocket.SocketFrame
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.contract.SecurityWebSocketEventTypes
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.response.settings.PasswordPolicyResponse
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.response.settings.SecuritySettingsResponse
import io.github.mudrichenkoevgeny.shared.foundation.core.security.passwordpolicy.model.PasswordPolicy
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

private class FakeSecuritySettingsStorage : SecuritySettingsStorage {
    var stored: SecuritySettings? = null

    override suspend fun getSecuritySettings(): SecuritySettings? = stored

    override suspend fun updateSecuritySettings(securitySettings: SecuritySettings) {
        stored = securitySettings
    }

    override suspend fun clearSecuritySettings() {
        stored = null
    }
}

private class FakeSecuritySettingsApi : SecuritySettingsApi {
    var result: AppResult<SecuritySettingsResponse> = AppResult.Error(CommonError.Unknown())
    var callCount = 0

    override suspend fun getSecuritySettings(): AppResult<SecuritySettingsResponse> {
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

private fun wirePolicy(
    minLength: Int = 8,
    requireLetter: Boolean = true,
    requireUpperCase: Boolean = false,
    requireLowerCase: Boolean = false,
    requireDigit: Boolean = false,
    requireSpecialChar: Boolean = false,
    commonPasswords: Set<String> = emptySet()
) = PasswordPolicyResponse(
    minLength = minLength,
    requireLetter = requireLetter,
    requireUpperCase = requireUpperCase,
    requireLowerCase = requireLowerCase,
    requireDigit = requireDigit,
    requireSpecialChar = requireSpecialChar,
    commonPasswords = commonPasswords
)

private fun wire(minLength: Int = 8) =
    SecuritySettingsResponse(passwordPolicy = wirePolicy(minLength = minLength))

class SecuritySettingsRepositoryImplTest {

    @Test
    fun `getSecuritySettings uses in memory cache without calling api when preloaded from storage`() =
        runTest {
            val persisted = SecuritySettings(
                passwordPolicy = PasswordPolicy(minLength = 5, commonPasswords = emptySet())
            )
            val storage = FakeSecuritySettingsStorage().apply { stored = persisted }
            val api = FakeSecuritySettingsApi().apply {
                result = AppResult.Success(wire())
            }

            val repo = SecuritySettingsRepositoryImpl(
                securitySettingsApi = api,
                securitySettingsStorage = storage,
                webSocketService = FakeWebSocketService(),
                repositoryScope = backgroundScope
            )
            advanceUntilIdle()

            val r = repo.getSecuritySettings()
            assertIs<AppResult.Success<SecuritySettings>>(r)
            assertEquals(persisted, r.data)
            assertEquals(0, api.callCount)
        }

    @Test
    fun `getSecuritySettings fetches from api when nothing in storage`() = runTest {
        val storage = FakeSecuritySettingsStorage()
        val response = wire(minLength = 10)
        val api = FakeSecuritySettingsApi().apply {
            result = AppResult.Success(response)
        }

        val repo = SecuritySettingsRepositoryImpl(
            securitySettingsApi = api,
            securitySettingsStorage = storage,
            webSocketService = FakeWebSocketService(),
            repositoryScope = backgroundScope
        )
        advanceUntilIdle()

        val r = repo.getSecuritySettings()
        assertIs<AppResult.Success<SecuritySettings>>(r)
        assertEquals(response.toSecuritySettings(), r.data)
        assertEquals(response.toSecuritySettings(), storage.stored)
        assertEquals(1, api.callCount)
    }

    @Test
    fun `refreshSecuritySettings always calls api`() = runTest {
        val storage = FakeSecuritySettingsStorage().apply {
            stored = SecuritySettings(
                passwordPolicy = PasswordPolicy(minLength = 1, commonPasswords = emptySet())
            )
        }
        val fresh = wire(minLength = 99)
        val api = FakeSecuritySettingsApi().apply {
            result = AppResult.Success(fresh)
        }

        val repo = SecuritySettingsRepositoryImpl(
            securitySettingsApi = api,
            securitySettingsStorage = storage,
            webSocketService = FakeWebSocketService(),
            repositoryScope = backgroundScope
        )
        advanceUntilIdle()

        val r = repo.refreshSecuritySettings()
        assertIs<AppResult.Success<SecuritySettings>>(r)
        assertEquals(fresh.toSecuritySettings(), r.data)
        assertEquals(fresh.toSecuritySettings(), storage.stored)
        assertTrue(api.callCount >= 1)
    }

    @Test
    fun `security settings updated websocket event persists and updates state`() = runTest {
        val storage = FakeSecuritySettingsStorage()
        val initialWire = wire(minLength = 1)
        val api = FakeSecuritySettingsApi().apply {
            result = AppResult.Success(initialWire)
        }
        val sockets = FakeWebSocketService()

        val repo = SecuritySettingsRepositoryImpl(
            securitySettingsApi = api,
            securitySettingsStorage = storage,
            webSocketService = sockets,
            repositoryScope = backgroundScope
        )
        advanceUntilIdle()

        assertIs<AppResult.Success<SecuritySettings>>(repo.getSecuritySettings())
        assertEquals(initialWire.toSecuritySettings(), storage.stored)

        val pushed = wire(minLength = 77)
        val frame = SocketFrame(
            id = FRAME_ID,
            type = SecurityWebSocketEventTypes.SECURITY_SETTINGS_UPDATED,
            payload = FoundationJson.encodeToJsonElement(
                SecuritySettingsResponse.serializer(),
                pushed
            ),
            metadata = emptyMap(),
            timestamp = 0L
        )
        sockets.emit(frame)
        runCurrent()
        advanceUntilIdle()

        assertEquals(pushed.toSecuritySettings(), storage.stored)
        val after = repo.getSecuritySettings()
        assertIs<AppResult.Success<SecuritySettings>>(after)
        assertEquals(pushed.toSecuritySettings(), after.data)
    }
}
