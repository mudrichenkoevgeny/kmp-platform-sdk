package io.github.mudrichenkoevgeny.kmp.core.security.mock.di

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.network.websockets.MockWebSocketService
import io.github.mudrichenkoevgeny.kmp.core.common.mock.storage.MockEncryptedSettings
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.security.di.SecurityComponent
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * Creates a [SecurityComponent] wired with in-memory/mock infrastructure.
 *
 * Intended for previews and tests that do not require platform encrypted storage. Uses:
 * - [MockEncryptedSettings] for persistence
 * - optional [HttpClient] (default empty client)
 * - optional [WebSocketService] (default [MockWebSocketService])
 *
 * @param httpClient Ktor client passed through to the security API module.
 * @param webSocketService Socket abstraction for repository subscriptions.
 * @param parentScope Coroutine scope for repository background work.
 * @return A fully wired [SecurityComponent] suitable for non-production hosts.
 */
@OptIn(InternalApi::class)
fun mockSecurityComponent(
    httpClient: HttpClient = HttpClient(),
    webSocketService: WebSocketService = MockWebSocketService(),
    parentScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
): SecurityComponent {
    return SecurityComponent(
        encryptedSettings = MockEncryptedSettings(),
        httpClient = httpClient,
        webSocketService = webSocketService,
        parentScope = parentScope
    )
}