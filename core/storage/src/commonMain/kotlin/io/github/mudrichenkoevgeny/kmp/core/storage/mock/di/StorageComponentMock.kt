package io.github.mudrichenkoevgeny.kmp.core.storage.mock.di

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.network.websocket.service.WebSocketServiceMock
import io.github.mudrichenkoevgeny.kmp.core.common.mock.storage.EncryptedSettingsMock
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.storage.di.StorageComponent
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * Creates a [StorageComponent] wired with in-memory/mock infrastructure.
 *
 * @param httpClient Ktor client passed through to the storage API module.
 * @param webSocketService Socket abstraction for repository subscriptions.
 * @param parentScope Coroutine scope for repository background work.
 * @return A fully wired [StorageComponent] suitable for non-production hosts.
 */
@InternalApi
fun storageComponentMock(
    httpClient: HttpClient = HttpClient(),
    webSocketService: WebSocketService = WebSocketServiceMock(),
    parentScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
): StorageComponent {
    return StorageComponent(
        httpClient = httpClient,
        encryptedSettings = EncryptedSettingsMock(),
        webSocketService = webSocketService,
        parentScope = parentScope
    )
}
