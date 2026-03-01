package io.github.mudrichenkoevgeny.kmp.core.settings.mock.di

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.network.websockets.MockWebSocketService
import io.github.mudrichenkoevgeny.kmp.core.common.mock.storage.MockEncryptedSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.TestScope
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.settings.di.SettingsComponent
import io.ktor.client.HttpClient

@OptIn(InternalApi::class)
fun mockSettingsComponent(
    httpClient: HttpClient = HttpClient(),
    webSocketService: WebSocketService = MockWebSocketService(),
    parentScope: CoroutineScope = TestScope()
): SettingsComponent {
    return SettingsComponent(
        encryptedSettings = MockEncryptedSettings(),
        httpClient = httpClient,
        webSocketService = webSocketService,
        parentScope = parentScope
    )
}