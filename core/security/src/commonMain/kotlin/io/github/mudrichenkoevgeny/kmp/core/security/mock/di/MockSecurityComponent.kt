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