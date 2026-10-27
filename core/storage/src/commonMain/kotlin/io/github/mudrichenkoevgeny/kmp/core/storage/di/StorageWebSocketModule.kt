package io.github.mudrichenkoevgeny.kmp.core.storage.di

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.core.storage.network.api.storagesettings.OpenStorageSettingsApi
import io.github.mudrichenkoevgeny.kmp.core.storage.network.websocket.messagehandler.StorageWebSocketMessageHandler

/**
 * Internal network wiring for `core/storage`.
 *
 * Provides the Ktor-backed [OpenStorageSettingsApi] and the [StorageWebSocketMessageHandler] for host
 * registration alongside other [WebSocketMessageHandler] instances.
 */
internal class StorageWebSocketModule {
    /**
     * Handler offered to the shared WebSocket pipeline for storage-related frame types.
     */
    val storageWebSocketMessageHandler: WebSocketMessageHandler by lazy {
        StorageWebSocketMessageHandler()
    }
}
