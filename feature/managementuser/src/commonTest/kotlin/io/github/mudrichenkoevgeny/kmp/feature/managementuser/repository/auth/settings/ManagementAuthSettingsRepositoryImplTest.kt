package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.network.websocket.service.WebSocketServiceMock
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.auth.settings.ManagementAuthSettingsApiMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.storage.auth.settings.ManagementAuthSettingsStorageMock
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.common.network.model.websocket.createTestSocketFrame
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.auth.settings.createTestManagementAuthSettings
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.network.model.auth.settings.createTestManagementAuthSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.ManagementAuthSettings
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.auth.settings.toManagementAuthSettings
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.contract.UserWebSocketEventTypes
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.auth.settings.ManagementAuthSettingsPayload
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class ManagementAuthSettingsRepositoryImplTest {

    private lateinit var storage: ManagementAuthSettingsStorageMock
    private lateinit var api: ManagementAuthSettingsApiMock
    private lateinit var sockets: WebSocketServiceMock

    @BeforeTest
    fun setUp() {
        storage = ManagementAuthSettingsStorageMock()
        api = ManagementAuthSettingsApiMock()
        sockets = WebSocketServiceMock()
    }

    private fun createRepository(scope: TestScope) = ManagementAuthSettingsRepositoryImpl(
        managementAuthSettingsApi = api,
        managementAuthSettingsStorage = storage,
        webSocketService = sockets,
        repositoryScope = scope.backgroundScope
    )

    @Test
    fun getManagementAuthSettings_returnsMemoryWithoutApi_whenPreloadedFromStorage() = runTest {
        val persisted = createTestManagementAuthSettings()
        storage.updateManagementAuthSettings(persisted)
        api.getResultProvider = { AppResult.Success(createTestManagementAuthSettingsPayload()) }

        val repo = createRepository(this)
        runCurrent()

        val settingsResult = repo.getManagementAuthSettings()
        assertIs<AppResult.Success<ManagementAuthSettings>>(settingsResult)
        assertEquals(persisted, settingsResult.data)
    }

    @Test
    fun getManagementAuthSettings_fetchesFromApi_whenNothingInStorage() = runTest {
        val wire = createTestManagementAuthSettingsPayload()
        api.getResultProvider = { AppResult.Success(wire) }

        val repo = createRepository(this)
        runCurrent()

        val settingsResult = repo.getManagementAuthSettings()

        assertIs<AppResult.Success<ManagementAuthSettings>>(settingsResult)
        assertEquals(wire.toManagementAuthSettings(), settingsResult.data)
        assertEquals(wire.toManagementAuthSettings(), storage.getManagementAuthSettings())
    }

    @Test
    fun refreshManagementAuthSettings_alwaysCallsApi() = runTest {
        storage.updateManagementAuthSettings(createTestManagementAuthSettings())
        val fresh = createTestManagementAuthSettingsPayload()
        api.getResultProvider = { AppResult.Success(fresh) }

        val repo = createRepository(this)
        runCurrent()

        val refreshed = repo.refreshManagementAuthSettings()
        assertIs<AppResult.Success<ManagementAuthSettings>>(refreshed)
        assertEquals(fresh.toManagementAuthSettings(), refreshed.data)
        assertEquals(fresh.toManagementAuthSettings(), storage.getManagementAuthSettings())
    }

    @Test
    fun authSettingsUpdatedWebSocketEvent_persistsAndUpdatesObservableState() = runTest {
        val initialWire = createTestManagementAuthSettingsPayload()
        api.getResultProvider = { AppResult.Success(initialWire) }

        val repo = createRepository(this)
        runCurrent()

        val pushed = createTestManagementAuthSettingsPayload(maxTotalIdentifiers = 999)
        val frame = createTestSocketFrame(
            type = UserWebSocketEventTypes.MANAGEMENT_AUTH_SETTINGS_UPDATED,
            payload = FoundationJson.encodeToJsonElement(
                ManagementAuthSettingsPayload.serializer(), pushed
            )
        )
        sockets.emit(frame)
        runCurrent()

        assertEquals(pushed.toManagementAuthSettings(), storage.getManagementAuthSettings())
        val after = repo.getManagementAuthSettings()
        assertIs<AppResult.Success<ManagementAuthSettings>>(after)
        assertEquals(pushed.toManagementAuthSettings(), after.data)
    }
}
