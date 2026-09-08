package io.github.mudrichenkoevgeny.kmp.feature.settingsapi.api.repository.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.network.model.websocket.socketFrameMock
import io.github.mudrichenkoevgeny.kmp.core.common.mock.network.websocket.service.WebSocketServiceMock
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.settings.mock.domain.model.globalsettings.openGlobalSettingsMock
import io.github.mudrichenkoevgeny.kmp.core.settings.mock.network.globalsettings.OpenGlobalSettingsApiMock
import io.github.mudrichenkoevgeny.kmp.core.settings.mock.network.model.globalsettings.openGlobalSettingsPayloadMock
import io.github.mudrichenkoevgeny.kmp.core.settings.mock.storage.globalsettings.OpenGlobalSettingsStorageMock
import io.github.mudrichenkoevgeny.kmp.core.settings.repository.OpenGlobalSettingsRepositoryImpl
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.domain.model.globalsettings.OpenGlobalSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.mapper.globalsettings.toOpenGlobalSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.contract.SettingsWebSocketEventTypes
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.model.globalsettings.OpenGlobalSettingsPayload
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

@InternalApi
class OpenGlobalSettingsRepositoryImplTest {

    private val storage = OpenGlobalSettingsStorageMock()
    private val api = OpenGlobalSettingsApiMock()
    private val sockets = WebSocketServiceMock()

    private fun createRepository(scope: TestScope) = OpenGlobalSettingsRepositoryImpl(
        openGlobalSettingsApi = api,
        openGlobalSettingsStorage = storage,
        webSocketService = sockets,
        repositoryScope = scope.backgroundScope
    )

    @Test
    fun getGlobalSettings_returnsMemoryWithoutApi_whenPreloadedFromStorage() = runTest {
        val persisted = openGlobalSettingsMock()
        storage.stored = persisted
        api.result = AppResult.Success(openGlobalSettingsPayloadMock())

        val repo = createRepository(this)
        advanceUntilIdle()

        val r = repo.getOpenGlobalSettings()
        assertIs<AppResult.Success<OpenGlobalSettings>>(r)
        assertEquals(persisted, r.data)
        assertEquals(0, api.getGlobalSettingsCallCount)
    }

    @Test
    fun getGlobalSettings_fetchesFromApi_whenNothingInStorage() = runTest {
        val response = openGlobalSettingsPayloadMock()
        api.result = AppResult.Success(response)

        val repo = createRepository(this)
        advanceUntilIdle()

        val r = repo.getOpenGlobalSettings()
        assertIs<AppResult.Success<OpenGlobalSettings>>(r)
        assertEquals(response.toOpenGlobalSettings(), r.data)
        assertEquals(response.toOpenGlobalSettings(), storage.stored)
        assertEquals(1, api.getGlobalSettingsCallCount)
    }

    @Test
    fun refreshGlobalSettings_alwaysCallsApi() = runTest {
        storage.stored = openGlobalSettingsMock()
        val fresh = openGlobalSettingsPayloadMock()
        api.result = AppResult.Success(fresh)

        val repo = createRepository(this)
        advanceUntilIdle()

        val r = repo.refreshOpenGlobalSettings()
        assertIs<AppResult.Success<OpenGlobalSettings>>(r)
        assertEquals(fresh.toOpenGlobalSettings(), r.data)
        assertEquals(fresh.toOpenGlobalSettings(), storage.stored)
        assertTrue(api.getGlobalSettingsCallCount >= 1)
    }

    @Test
    fun globalSettingsUpdatedWebSocketEvent_persistsAndUpdatesFlow() = runTest {
        val initialWire = openGlobalSettingsPayloadMock(privacy = "first")
        api.result = AppResult.Success(initialWire)

        val repo = createRepository(this)
        runCurrent()
        advanceUntilIdle()

        repo.getOpenGlobalSettings()
        assertEquals(initialWire.toOpenGlobalSettings(), storage.stored)

        val pushed = openGlobalSettingsPayloadMock(privacy = "ws-p", terms = "ws-t", email = "ws@e")
        val frame = socketFrameMock(
            type = SettingsWebSocketEventTypes.OPEN_GLOBAL_SETTINGS_UPDATED,
            payload = FoundationJson.encodeToJsonElement(
                OpenGlobalSettingsPayload.serializer(),
                pushed
            )
        )

        sockets.emit(frame)
        runCurrent()
        advanceUntilIdle()

        val expected = pushed.toOpenGlobalSettings()
        assertEquals(expected, storage.stored)

        val after = repo.getOpenGlobalSettings()
        assertIs<AppResult.Success<OpenGlobalSettings>>(after)
        assertEquals(expected, after.data)
    }
}
