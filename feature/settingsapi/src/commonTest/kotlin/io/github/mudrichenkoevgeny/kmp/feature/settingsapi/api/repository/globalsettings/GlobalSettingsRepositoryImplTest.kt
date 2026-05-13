package io.github.mudrichenkoevgeny.kmp.feature.settingsapi.api.repository.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.network.model.websocket.socketFrameMock
import io.github.mudrichenkoevgeny.kmp.core.common.mock.network.websocket.service.WebSocketServiceMock
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.settings.mock.domain.model.globalsettings.globalSettingsMock
import io.github.mudrichenkoevgeny.kmp.core.settings.mock.network.globalsettings.GlobalSettingsApiMock
import io.github.mudrichenkoevgeny.kmp.core.settings.mock.network.model.globalsettings.globalSettingsPayloadMock
import io.github.mudrichenkoevgeny.kmp.core.settings.mock.storage.globalsettings.GlobalSettingsStorageMock
import io.github.mudrichenkoevgeny.kmp.core.settings.repository.GlobalSettingsRepositoryImpl
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.domain.model.globalsettings.GlobalSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.mapper.globalsettings.toGlobalSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.contract.SettingsWebSocketEventTypes
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.model.globalsettings.GlobalSettingsPayload
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

@InternalApi
class GlobalSettingsRepositoryImplTest {

    private val storage = GlobalSettingsStorageMock()
    private val api = GlobalSettingsApiMock()
    private val sockets = WebSocketServiceMock()

    private fun createRepository(scope: TestScope) = GlobalSettingsRepositoryImpl(
        globalSettingsApi = api,
        globalSettingsStorage = storage,
        webSocketService = sockets,
        repositoryScope = scope.backgroundScope
    )

    @Test
    fun `getGlobalSettings uses in memory cache without calling api when preloaded from storage`() = runTest {
        val persisted = globalSettingsMock()
        storage.stored = persisted
        api.result = AppResult.Success(globalSettingsPayloadMock())

        val repo = createRepository(this)
        advanceUntilIdle()

        val r = repo.getGlobalSettings()
        assertIs<AppResult.Success<GlobalSettings>>(r)
        assertEquals(persisted, r.data)
        assertEquals(0, api.callCount)
    }

    @Test
    fun `getGlobalSettings fetches from api when nothing in storage`() = runTest {
        val response = globalSettingsPayloadMock()
        api.result = AppResult.Success(response)

        val repo = createRepository(this)
        advanceUntilIdle()

        val r = repo.getGlobalSettings()
        assertIs<AppResult.Success<GlobalSettings>>(r)
        assertEquals(response.toGlobalSettings(), r.data)
        assertEquals(response.toGlobalSettings(), storage.stored)
        assertEquals(1, api.callCount)
    }

    @Test
    fun `refreshGlobalSettings always calls api`() = runTest {
        storage.stored = globalSettingsMock()
        val fresh = globalSettingsPayloadMock()
        api.result = AppResult.Success(fresh)

        val repo = createRepository(this)
        advanceUntilIdle()

        val r = repo.refreshGlobalSettings()
        assertIs<AppResult.Success<GlobalSettings>>(r)
        assertEquals(fresh.toGlobalSettings(), r.data)
        assertEquals(fresh.toGlobalSettings(), storage.stored)
        assertTrue(api.callCount >= 1)
    }

    @Test
    fun `global settings updated websocket event persists and updates flow`() = runTest {
        val initialWire = globalSettingsPayloadMock(privacy = "first")
        api.result = AppResult.Success(initialWire)

        val repo = createRepository(this)
        runCurrent()
        advanceUntilIdle()

        repo.getGlobalSettings()
        assertEquals(initialWire.toGlobalSettings(), storage.stored)

        val pushed = globalSettingsPayloadMock(privacy = "ws-p", terms = "ws-t", email = "ws@e")
        val frame = socketFrameMock(
            type = SettingsWebSocketEventTypes.GLOBAL_SETTINGS_UPDATED,
            payload = FoundationJson.encodeToJsonElement(
                GlobalSettingsPayload.serializer(),
                pushed
            )
        )

        sockets.emit(frame)
        runCurrent()
        advanceUntilIdle()

        val expected = pushed.toGlobalSettings()
        assertEquals(expected, storage.stored)

        val after = repo.getGlobalSettings()
        assertIs<AppResult.Success<GlobalSettings>>(after)
        assertEquals(expected, after.data)
    }
}