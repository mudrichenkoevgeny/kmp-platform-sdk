package io.github.mudrichenkoevgeny.kmp.core.settings.mock.di

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.network.websocket.service.WebSocketServiceMock
import io.github.mudrichenkoevgeny.kmp.core.common.mock.storage.EncryptedSettingsMock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.settings.di.SettingsComponent
import io.github.mudrichenkoevgeny.kmp.core.settings.mock.network.globalsettings.GlobalSettingsApiMock
import io.ktor.client.HttpClient

/**
 * Creates a [SettingsComponent] wired with in-memory/mock infrastructure.
 *
 * Intended for previews and tests that do not require platform encrypted storage. Uses:
 * - [EncryptedSettingsMock] for persistence
 * - optional [HttpClient] (default empty client)
 * - optional [WebSocketService] (default [WebSocketServiceMock])
 *
 * @param httpClient Ktor client passed through to the settings API module.
 * @param webSocketService Socket abstraction for repository subscriptions.
 * @param parentScope Coroutine scope for repository background work.
 * @return A fully wired [SettingsComponent] suitable for non-production hosts.
 */
@InternalApi
fun settingsComponentMock(
    httpClient: HttpClient = HttpClient(),
    webSocketService: WebSocketService = WebSocketServiceMock(),
    parentScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
): SettingsComponent {
    return SettingsComponent(
        webSocketService = webSocketService,
        globalSettingsApi = GlobalSettingsApiMock(),
        encryptedSettings = EncryptedSettingsMock(),
        parentScope = parentScope
    )
}