package io.github.mudrichenkoevgeny.kmp.feature.clientuser.repository.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.network.model.websocket.socketFrameMock
import io.github.mudrichenkoevgeny.kmp.core.common.mock.network.websocket.service.WebSocketServiceMock
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.mock.network.api.auth.settings.OpenAuthSettingsApiMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.auth.settings.publicAuthSettingsPayloadMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.auth.AuthStorageMock
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.PublicAuthSettings
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.auth.settings.toAuthSettings
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.contract.UserWebSocketEventTypes
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.auth.settings.PublicAuthSettingsPayload
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

@InternalApi
class OpenAuthSettingsRepositoryImplTest {

    private val storage = AuthStorageMock()
    private val api = OpenAuthSettingsApiMock()
    private val sockets = WebSocketServiceMock()

    private fun createRepository(scope: TestScope) = OpenAuthSettingsRepositoryImpl(
        openAuthSettingsApi = api,
        authStorage = storage,
        webSocketService = sockets,
        repositoryScope = scope.backgroundScope
    )

    @Test
    fun getAuthSettings_returnsMemoryWithoutApi_whenPreloadedFromStorage() = runTest {
        val persisted = publicAuthSettingsPayloadMock().toAuthSettings()
        storage.updatePublicAuthSettings(persisted)
        api.result = AppResult.Success(publicAuthSettingsPayloadMock())

        val repo = createRepository(this)
        advanceUntilIdle()

        val settingsResult = repo.getAuthSettings()
        assertIs<AppResult.Success<PublicAuthSettings>>(settingsResult)
        assertEquals(persisted, settingsResult.data)
        assertEquals(0, api.callCount)
    }

    @Test
    fun getAuthSettings_fetchesFromApi_whenNothingInStorage() = runTest {
        val wire = publicAuthSettingsPayloadMock()
        api.result = AppResult.Success(wire)

        val repo = createRepository(this)
        advanceUntilIdle()

        val settingsResult = repo.getAuthSettings()

        assertIs<AppResult.Success<PublicAuthSettings>>(settingsResult)
        assertEquals(wire.toAuthSettings(), settingsResult.data)
        assertEquals(wire.toAuthSettings(), storage.getPublicAuthSettings())
        assertEquals(1, api.callCount)
    }

    @Test
    fun refreshAuthSettings_alwaysCallsApi() = runTest {
        storage.updatePublicAuthSettings(publicAuthSettingsPayloadMock().toAuthSettings())
        val fresh = publicAuthSettingsPayloadMock()
        api.result = AppResult.Success(fresh)

        val repo = createRepository(this)
        advanceUntilIdle()

        val refreshed = repo.refreshAuthSettings()
        assertIs<AppResult.Success<PublicAuthSettings>>(refreshed)
        assertEquals(fresh.toAuthSettings(), refreshed.data)
        assertEquals(fresh.toAuthSettings(), storage.getPublicAuthSettings())
        assertTrue(api.callCount >= 1)
    }

    @Test
    fun authSettingsUpdatedWebSocketEvent_persistsAndUpdatesObservableState() = runTest {
        val initialWire = publicAuthSettingsPayloadMock()
        api.result = AppResult.Success(initialWire)

        val repo = createRepository(this)
        advanceUntilIdle()

        assertIs<AppResult.Success<PublicAuthSettings>>(repo.getAuthSettings())
        assertEquals(initialWire.toAuthSettings(), storage.getPublicAuthSettings())

        val pushed = publicAuthSettingsPayloadMock()
        val frame = socketFrameMock(
            type = UserWebSocketEventTypes.AUTH_SETTINGS_UPDATED,
            payload = FoundationJson.encodeToJsonElement(
                PublicAuthSettingsPayload.serializer(), pushed
            )
        )
        sockets.emit(frame)
        runCurrent()
        advanceUntilIdle()

        assertEquals(pushed.toAuthSettings(), storage.getPublicAuthSettings())
        val after = repo.getAuthSettings()
        assertIs<AppResult.Success<PublicAuthSettings>>(after)
        assertEquals(pushed.toAuthSettings(), after.data)
    }
}
