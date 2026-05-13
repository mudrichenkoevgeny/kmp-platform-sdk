package io.github.mudrichenkoevgeny.kmp.feature.securityapi.repository.securitysettings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.network.model.websocket.socketFrameMock
import io.github.mudrichenkoevgeny.kmp.core.common.mock.network.websocket.service.WebSocketServiceMock
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.mock.domain.model.securitySettingsMock
import io.github.mudrichenkoevgeny.kmp.core.security.mock.network.model.passwordpolicy.passwordPolicyPayloadMock
import io.github.mudrichenkoevgeny.kmp.core.security.mock.network.model.securitysettings.securitySettingsPayloadMock
import io.github.mudrichenkoevgeny.kmp.core.security.mock.network.securitysettings.SecuritySettingsApiMock
import io.github.mudrichenkoevgeny.kmp.core.security.mock.storage.securitysettings.SecuritySettingsStorageMock
import io.github.mudrichenkoevgeny.kmp.core.security.repository.SecuritySettingsRepositoryImpl
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.securitysettings.SecuritySettings
import io.github.mudrichenkoevgeny.shared.foundation.core.security.mapper.securitysettings.toSecuritySettings
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.contract.SecurityWebSocketEventTypes
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.securitysettings.SecuritySettingsPayload
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

@InternalApi
class SecuritySettingsRepositoryImplTest {

    private val storage = SecuritySettingsStorageMock()
    private val api = SecuritySettingsApiMock()
    private val sockets = WebSocketServiceMock()

    private fun createRepository(scope: TestScope) = SecuritySettingsRepositoryImpl(
        securitySettingsApi = api,
        securitySettingsStorage = storage,
        webSocketService = sockets,
        repositoryScope = scope.backgroundScope
    )

    @Test
    fun `getSecuritySettings uses in memory cache without calling api when preloaded from storage`() =
        runTest {
            val persisted = securitySettingsMock()
            storage.stored = persisted
            api.result = AppResult.Success(securitySettingsPayloadMock())

            val repo = createRepository(this)
            advanceUntilIdle()

            val r = repo.getSecuritySettings()
            assertIs<AppResult.Success<SecuritySettings>>(r)
            assertEquals(persisted, r.data)
            assertEquals(0, api.callCount)
        }

    @Test
    fun `getSecuritySettings fetches from api when nothing in storage`() = runTest {
        val response = securitySettingsPayloadMock(
            passwordPolicy = passwordPolicyPayloadMock(minLength = 10)
        )
        api.result = AppResult.Success(response)

        val repo = createRepository(this)
        advanceUntilIdle()

        val r = repo.getSecuritySettings()
        assertIs<AppResult.Success<SecuritySettings>>(r)
        assertEquals(response.toSecuritySettings(), r.data)
        assertEquals(response.toSecuritySettings(), storage.stored)
        assertEquals(1, api.callCount)
    }

    @Test
    fun `refreshSecuritySettings always calls api`() = runTest {
        storage.stored = securitySettingsMock()
        val fresh = securitySettingsPayloadMock(
            passwordPolicy = passwordPolicyPayloadMock(minLength = 99)
        )
        api.result = AppResult.Success(fresh)

        val repo = createRepository(this)
        advanceUntilIdle()

        val r = repo.refreshSecuritySettings()
        assertIs<AppResult.Success<SecuritySettings>>(r)
        assertEquals(fresh.toSecuritySettings(), r.data)
        assertEquals(fresh.toSecuritySettings(), storage.stored)
        assertTrue(api.callCount >= 1)
    }

    @Test
    fun `security settings updated websocket event persists and updates state`() = runTest {
        val initialWire = securitySettingsPayloadMock(
            passwordPolicy = passwordPolicyPayloadMock(minLength = 1)
        )
        api.result = AppResult.Success(initialWire)

        val repo = createRepository(this)
        runCurrent()
        advanceUntilIdle()

        repo.getSecuritySettings()

        val pushed = securitySettingsPayloadMock(
            passwordPolicy = passwordPolicyPayloadMock(minLength = 77)
        )
        val frame = socketFrameMock(
            type = SecurityWebSocketEventTypes.SECURITY_SETTINGS_UPDATED,
            payload = FoundationJson.encodeToJsonElement(
                SecuritySettingsPayload.serializer(),
                pushed
            )
        )

        sockets.emit(frame)
        runCurrent()
        advanceUntilIdle()

        val expected = pushed.toSecuritySettings()
        assertEquals(expected, storage.stored)

        val after = repo.getSecuritySettings()
        assertIs<AppResult.Success<SecuritySettings>>(after)
        assertEquals(expected, after.data)
    }
}