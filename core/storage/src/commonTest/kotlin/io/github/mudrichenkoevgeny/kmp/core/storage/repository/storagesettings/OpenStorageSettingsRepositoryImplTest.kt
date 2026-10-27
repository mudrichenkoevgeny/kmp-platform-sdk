package io.github.mudrichenkoevgeny.kmp.core.storage.repository.storagesettings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.network.websocket.service.WebSocketServiceMock
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.mock.network.storagesettings.OpenStorageSettingsApiMock
import io.github.mudrichenkoevgeny.kmp.core.storage.mock.storage.storagesettings.OpenStorageSettingsStorageMock
import io.github.mudrichenkoevgeny.kmp.core.storage.repository.settings.OpenStorageSettingsRepositoryImpl
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.storagesettings.OpenStorageSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.mapper.storagesettings.toOpenStorageSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.contract.StorageWebSocketEventTypes
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.model.storagesettings.OpenStorageSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.common.network.model.websocket.createTestSocketFrame
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.storage.domain.model.storagesettings.createTestOpenStorageSettings
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.storage.network.model.storagesettings.createTestOpenStorageSettingsPayload
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

@InternalApi
class OpenStorageSettingsRepositoryImplTest {

    private val storage = OpenStorageSettingsStorageMock()
    private val api = OpenStorageSettingsApiMock()
    private val sockets = WebSocketServiceMock()

    private fun createRepository(scope: TestScope) = OpenStorageSettingsRepositoryImpl(
        openStorageSettingsApi = api,
        openStorageSettingsStorage = storage,
        webSocketService = sockets,
        repositoryScope = scope.backgroundScope
    )

    @Test
    fun getStorageSettings_returnsMemoryWithoutApi_whenPreloadedFromStorage() = runTest {
        val persisted = createTestOpenStorageSettings()
        storage.stored = persisted
        api.result = AppResult.Success(createTestOpenStorageSettingsPayload())

        val repo = createRepository(this)
        advanceUntilIdle()

        val r = repo.getOpenStorageSettings()
        assertIs<AppResult.Success<OpenStorageSettings>>(r)
        assertEquals(persisted, r.data)
        assertEquals(0, api.getStorageSettingsCallCount)
    }

    @Test
    fun getStorageSettings_fetchesFromApi_whenNothingInStorage() = runTest {
        val response = createTestOpenStorageSettingsPayload(maxUploadSizeBytes = 104857600L)
        api.result = AppResult.Success(response)

        val repo = createRepository(this)
        advanceUntilIdle()

        val r = repo.getOpenStorageSettings()
        assertIs<AppResult.Success<OpenStorageSettings>>(r)
        assertEquals(response.toOpenStorageSettings(), r.data)
        assertEquals(response.toOpenStorageSettings(), storage.stored)
        assertEquals(1, api.getStorageSettingsCallCount)
    }

    @Test
    fun refreshStorageSettings_alwaysCallsApi() = runTest {
        storage.stored = createTestOpenStorageSettings()
        val fresh = createTestOpenStorageSettingsPayload(maxUploadSizeBytes = 209715200L)
        api.result = AppResult.Success(fresh)

        val repo = createRepository(this)
        advanceUntilIdle()

        val r = repo.refreshOpenStorageSettings()
        assertIs<AppResult.Success<OpenStorageSettings>>(r)
        assertEquals(fresh.toOpenStorageSettings(), r.data)
        assertEquals(fresh.toOpenStorageSettings(), storage.stored)
        assertTrue(api.getStorageSettingsCallCount >= 1)
    }

    @Test
    fun storageSettingsUpdatedWebSocketEvent_persistsAndUpdatesState() = runTest {
        val initialWire = createTestOpenStorageSettingsPayload(maxUploadSizeBytes = 104857600L)
        api.result = AppResult.Success(initialWire)

        val repo = createRepository(this)
        runCurrent()
        advanceUntilIdle()

        repo.getOpenStorageSettings()

        val pushed = createTestOpenStorageSettingsPayload(maxUploadSizeBytes = 524288000L)
        val frame = createTestSocketFrame(
            type = StorageWebSocketEventTypes.OPEN_STORAGE_SETTINGS_UPDATED,
            payload = FoundationJson.encodeToJsonElement(
                OpenStorageSettingsPayload.serializer(),
                pushed
            )
        )

        sockets.emit(frame)
        runCurrent()
        advanceUntilIdle()

        val expected = pushed.toOpenStorageSettings()
        assertEquals(expected, storage.stored)

        val after = repo.getOpenStorageSettings()
        assertIs<AppResult.Success<OpenStorageSettings>>(after)
        assertEquals(expected, after.data)
    }
}
