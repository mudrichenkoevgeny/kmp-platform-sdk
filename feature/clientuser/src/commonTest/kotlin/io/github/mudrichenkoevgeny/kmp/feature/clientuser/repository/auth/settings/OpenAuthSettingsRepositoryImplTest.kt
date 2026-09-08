package io.github.mudrichenkoevgeny.kmp.feature.clientuser.repository.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.network.model.websocket.socketFrameMock
import io.github.mudrichenkoevgeny.kmp.core.common.mock.network.websocket.service.WebSocketServiceMock
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.mock.network.api.auth.settings.OpenAuthSettingsApiMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.auth.settings.openAuthSettingsPayloadMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.auth.settings.OpenAuthSettingsStorageMock
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.OpenAuthSettings
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.auth.settings.toOpenAuthSettings
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.contract.UserWebSocketEventTypes
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.auth.settings.OpenAuthSettingsPayload
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

    private val storage = OpenAuthSettingsStorageMock()
    private val api = OpenAuthSettingsApiMock()
    private val sockets = WebSocketServiceMock()

    private fun createRepository(scope: TestScope) = OpenAuthSettingsRepositoryImpl(
        openAuthSettingsApi = api,
        openAuthSettingsStorage = storage,
        webSocketService = sockets,
        repositoryScope = scope.backgroundScope
    )

    @Test
    fun getOpenAuthSettings_returnsMemoryWithoutApi_whenPreloadedFromStorage() = runTest {
        val persisted = openAuthSettingsPayloadMock().toOpenAuthSettings()
        storage.updateOpenAuthSettings(persisted)
        api.result = AppResult.Success(openAuthSettingsPayloadMock())

        val repo = createRepository(this)
        advanceUntilIdle()

        val settingsResult = repo.getOpenAuthSettings()
        assertIs<AppResult.Success<OpenAuthSettings>>(settingsResult)
        assertEquals(persisted, settingsResult.data)
        assertEquals(0, api.callCount)
    }

    @Test
    fun getOpenAuthSettings_fetchesFromApi_whenNothingInStorage() = runTest {
        val wire = openAuthSettingsPayloadMock()
        api.result = AppResult.Success(wire)

        val repo = createRepository(this)
        advanceUntilIdle()

        val settingsResult = repo.getOpenAuthSettings()

        assertIs<AppResult.Success<OpenAuthSettings>>(settingsResult)
        assertEquals(wire.toOpenAuthSettings(), settingsResult.data)
        assertEquals(wire.toOpenAuthSettings(), storage.getOpenAuthSettings())
        assertEquals(1, api.callCount)
    }

    @Test
    fun refreshOpenAuthSettings_alwaysCallsApi() = runTest {
        storage.updateOpenAuthSettings(openAuthSettingsPayloadMock().toOpenAuthSettings())
        val fresh = openAuthSettingsPayloadMock()
        api.result = AppResult.Success(fresh)

        val repo = createRepository(this)
        advanceUntilIdle()

        val refreshed = repo.refreshOpenAuthSettings()
        assertIs<AppResult.Success<OpenAuthSettings>>(refreshed)
        assertEquals(fresh.toOpenAuthSettings(), refreshed.data)
        assertEquals(fresh.toOpenAuthSettings(), storage.getOpenAuthSettings())
        assertTrue(api.callCount >= 1)
    }

    @Test
    fun openAuthSettingsUpdatedWebSocketEvent_persistsAndUpdatesObservableState() = runTest {
        val initialWire = openAuthSettingsPayloadMock()
        api.result = AppResult.Success(initialWire)

        val repo = createRepository(this)
        advanceUntilIdle()

        assertIs<AppResult.Success<OpenAuthSettings>>(repo.getOpenAuthSettings())
        assertEquals(initialWire.toOpenAuthSettings(), storage.getOpenAuthSettings())

        val pushed = openAuthSettingsPayloadMock()
        val frame = socketFrameMock(
            type = UserWebSocketEventTypes.OPEN_AUTH_SETTINGS_UPDATED,
            payload = FoundationJson.encodeToJsonElement(
                OpenAuthSettingsPayload.serializer(), pushed
            )
        )
        sockets.emit(frame)
        runCurrent()
        advanceUntilIdle()

        assertEquals(pushed.toOpenAuthSettings(), storage.getOpenAuthSettings())
        val after = repo.getOpenAuthSettings()
        assertIs<AppResult.Success<OpenAuthSettings>>(after)
        assertEquals(pushed.toOpenAuthSettings(), after.data)
    }
}
