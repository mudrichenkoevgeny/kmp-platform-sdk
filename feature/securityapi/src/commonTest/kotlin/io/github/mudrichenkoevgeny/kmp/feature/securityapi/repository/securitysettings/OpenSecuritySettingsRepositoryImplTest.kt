package io.github.mudrichenkoevgeny.kmp.feature.securityapi.repository.securitysettings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.network.model.websocket.socketFrameMock
import io.github.mudrichenkoevgeny.kmp.core.common.mock.network.websocket.service.WebSocketServiceMock
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.mock.domain.model.openSecuritySettingsMock
import io.github.mudrichenkoevgeny.kmp.core.security.mock.network.model.passwordpolicy.openPasswordPolicyPayloadMock
import io.github.mudrichenkoevgeny.kmp.core.security.mock.network.model.securitysettings.openSecuritySettingsPayloadMock
import io.github.mudrichenkoevgeny.kmp.core.security.mock.network.securitysettings.OpenSecuritySettingsApiMock
import io.github.mudrichenkoevgeny.kmp.core.security.mock.storage.securitysettings.OpenSecuritySettingsStorageMock
import io.github.mudrichenkoevgeny.kmp.core.security.repository.OpenSecuritySettingsRepositoryImpl
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.securitysettings.OpenSecuritySettings
import io.github.mudrichenkoevgeny.shared.foundation.core.security.mapper.securitysettings.toOpenSecuritySettings
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.contract.SecurityWebSocketEventTypes
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.securitysettings.OpenSecuritySettingsPayload
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

@InternalApi
class OpenSecuritySettingsRepositoryImplTest {

    private val storage = OpenSecuritySettingsStorageMock()
    private val api = OpenSecuritySettingsApiMock()
    private val sockets = WebSocketServiceMock()

    private fun createRepository(scope: TestScope) = OpenSecuritySettingsRepositoryImpl(
        openSecuritySettingsApi = api,
        openSecuritySettingsStorage = storage,
        webSocketService = sockets,
        repositoryScope = scope.backgroundScope
    )

    @Test
    fun getSecuritySettings_returnsMemoryWithoutApi_whenPreloadedFromStorage() =
        runTest {
            val persisted = openSecuritySettingsMock()
            storage.stored = persisted
            api.result = AppResult.Success(openSecuritySettingsPayloadMock())

            val repo = createRepository(this)
            advanceUntilIdle()

            val r = repo.getOpenSecuritySettings()
            assertIs<AppResult.Success<OpenSecuritySettings>>(r)
            assertEquals(persisted, r.data)
            assertEquals(0, api.getSecuritySettingsCallCount)
        }

    @Test
    fun getSecuritySettings_fetchesFromApi_whenNothingInStorage() = runTest {
        val response = openSecuritySettingsPayloadMock(
            passwordPolicy = openPasswordPolicyPayloadMock(minLength = 10)
        )
        api.result = AppResult.Success(response)

        val repo = createRepository(this)
        advanceUntilIdle()

        val r = repo.getOpenSecuritySettings()
        assertIs<AppResult.Success<OpenSecuritySettings>>(r)
        assertEquals(response.toOpenSecuritySettings(), r.data)
        assertEquals(response.toOpenSecuritySettings(), storage.stored)
        assertEquals(1, api.getSecuritySettingsCallCount)
    }

    @Test
    fun refreshSecuritySettings_alwaysCallsApi() = runTest {
        storage.stored = openSecuritySettingsMock()
        val fresh = openSecuritySettingsPayloadMock(
            passwordPolicy = openPasswordPolicyPayloadMock(minLength = 99)
        )
        api.result = AppResult.Success(fresh)

        val repo = createRepository(this)
        advanceUntilIdle()

        val r = repo.refreshOpenSecuritySettings()
        assertIs<AppResult.Success<OpenSecuritySettings>>(r)
        assertEquals(fresh.toOpenSecuritySettings(), r.data)
        assertEquals(fresh.toOpenSecuritySettings(), storage.stored)
        assertTrue(api.getSecuritySettingsCallCount >= 1)
    }

    @Test
    fun securitySettingsUpdatedWebSocketEvent_persistsAndUpdatesState() = runTest {
        val initialWire = openSecuritySettingsPayloadMock(
            passwordPolicy = openPasswordPolicyPayloadMock(minLength = 1)
        )
        api.result = AppResult.Success(initialWire)

        val repo = createRepository(this)
        runCurrent()
        advanceUntilIdle()

        repo.getOpenSecuritySettings()

        val pushed = openSecuritySettingsPayloadMock(
            passwordPolicy = openPasswordPolicyPayloadMock(minLength = 77)
        )
        val frame = socketFrameMock(
            type = SecurityWebSocketEventTypes.OPEN_SECURITY_SETTINGS_UPDATED,
            payload = FoundationJson.encodeToJsonElement(
                OpenSecuritySettingsPayload.serializer(),
                pushed
            )
        )

        sockets.emit(frame)
        runCurrent()
        advanceUntilIdle()

        val expected = pushed.toOpenSecuritySettings()
        assertEquals(expected, storage.stored)

        val after = repo.getOpenSecuritySettings()
        assertIs<AppResult.Success<OpenSecuritySettings>>(after)
        assertEquals(expected, after.data)
    }
}
