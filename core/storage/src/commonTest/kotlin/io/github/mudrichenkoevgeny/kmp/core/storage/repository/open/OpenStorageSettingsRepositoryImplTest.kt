package io.github.mudrichenkoevgeny.kmp.core.storage.repository.open

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.network.websocket.service.WebSocketServiceMock
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.mock.network.api.open.OpenStorageSettingsApiMock
import io.github.mudrichenkoevgeny.kmp.core.storage.mock.storage.open.OpenStorageSettingsStorageMock
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.storagesettings.OpenStorageSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.mapper.storagesettings.toOpenStorageSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.contract.StorageWebSocketEventTypes
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.model.storagesettings.OpenStorageSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.common.network.model.websocket.createTestSocketFrame
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.storage.domain.model.storagesettings.createTestOpenStorageSettings
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.storage.network.model.storagesettings.createTestOpenStorageSettingsPayload
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class OpenStorageSettingsRepositoryImplTest {

    @Test
    fun getOpenStorageSettings_returnsCachedValue_whenPresent() = runTest {
        val cached = createTestOpenStorageSettings()
        val storage = OpenStorageSettingsStorageMock().apply { stored = cached }
        val repository = OpenStorageSettingsRepositoryImpl(
            openStorageSettingsApi = OpenStorageSettingsApiMock(),
            openStorageSettingsStorage = storage,
            webSocketService = WebSocketServiceMock(),
            repositoryScope = backgroundScope
        )

        val result = repository.getOpenStorageSettings()

        val success = assertIs<AppResult.Success<OpenStorageSettings>>(result)
        assertEquals(cached, success.data)
    }

    @Test
    fun getOpenStorageSettings_fetchesFromApi_whenCacheEmpty() = runTest {
        val storage = OpenStorageSettingsStorageMock()
        val api = OpenStorageSettingsApiMock()
        val repository = OpenStorageSettingsRepositoryImpl(
            openStorageSettingsApi = api,
            openStorageSettingsStorage = storage,
            webSocketService = WebSocketServiceMock(),
            repositoryScope = backgroundScope
        )

        val result = repository.getOpenStorageSettings()

        assertIs<AppResult.Success<OpenStorageSettings>>(result)
        assertEquals(1, api.getStorageSettingsCallCount)
        assertEquals(result.data, storage.stored)
    }

    @Test
    fun updateOpenStorageSettings_updatesStorageAndFlow() = runTest {
        val storage = OpenStorageSettingsStorageMock()
        val repository = OpenStorageSettingsRepositoryImpl(
            openStorageSettingsApi = OpenStorageSettingsApiMock(),
            openStorageSettingsStorage = storage,
            webSocketService = WebSocketServiceMock(),
            repositoryScope = backgroundScope
        )

        val newSettings = createTestOpenStorageSettings()
        repository.updateOpenStorageSettings(newSettings)

        assertEquals(newSettings, storage.stored)
    }

    @Test
    fun webSocketEvent_updatesStorageAndEmitsNewValue() = runTest {
        val storage = OpenStorageSettingsStorageMock()
        val wsMock = WebSocketServiceMock()

        val repository = OpenStorageSettingsRepositoryImpl(
            openStorageSettingsApi = OpenStorageSettingsApiMock(),
            openStorageSettingsStorage = storage,
            webSocketService = wsMock,
            repositoryScope = backgroundScope
        )

        val payload = createTestOpenStorageSettingsPayload()
        val frame = createTestSocketFrame(
            type = StorageWebSocketEventTypes.OPEN_STORAGE_SETTINGS_UPDATED,
            payload = FoundationJson.encodeToJsonElement(OpenStorageSettingsPayload.serializer(), payload)
        )

        wsMock.emit(frame)

        val updated = repository.getOpenStorageSettings()
        assertIs<AppResult.Success<OpenStorageSettings>>(updated)
        assertEquals(payload.toOpenStorageSettings(), updated.data)
    }
}
