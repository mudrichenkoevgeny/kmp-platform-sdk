package io.github.mudrichenkoevgeny.kmp.core.security.mock.di

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.network.websocket.service.WebSocketServiceMock
import io.github.mudrichenkoevgeny.kmp.core.common.mock.storage.EncryptedSettingsMock
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.security.di.SecurityComponent
import io.github.mudrichenkoevgeny.kmp.core.security.mock.network.securitysettings.SecuritySettingsApiMock
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * Creates a [SecurityComponent] wired with in-memory/mock infrastructure.
 *
 * Intended for previews and tests that do not require platform encrypted storage. Uses:
 * - [EncryptedSettingsMock] for persistence
 * - optional [HttpClient] (default empty client)
 * - optional [WebSocketService] (default [WebSocketServiceMock])
 *
 * @param httpClient Ktor client passed through to the security API module.
 * @param webSocketService Socket abstraction for repository subscriptions.
 * @param parentScope Coroutine scope for repository background work.
 * @return A fully wired [SecurityComponent] suitable for non-production hosts.
 */
@InternalApi
fun securityComponentMock(
    httpClient: HttpClient = HttpClient(),
    webSocketService: WebSocketService = WebSocketServiceMock(),
    parentScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
): SecurityComponent {
    return SecurityComponent(
        webSocketService = webSocketService,
        securitySettingsApi = SecuritySettingsApiMock(),
        encryptedSettings = EncryptedSettingsMock(),
        parentScope = parentScope
    )
}