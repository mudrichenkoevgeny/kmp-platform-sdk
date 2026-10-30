package io.github.mudrichenkoevgeny.kmp.core.storage.di

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.core.storage.network.api.base.StorageApi
import io.github.mudrichenkoevgeny.kmp.core.storage.network.api.management.KtorManagementStorageApi
import io.github.mudrichenkoevgeny.kmp.core.storage.network.api.management.KtorManagementStorageSettingsApi
import io.github.mudrichenkoevgeny.kmp.core.storage.network.api.management.KtorSelfManagementStorageApi
import io.github.mudrichenkoevgeny.kmp.core.storage.network.api.management.ManagementStorageApi
import io.github.mudrichenkoevgeny.kmp.core.storage.network.api.management.ManagementStorageSettingsApi
import io.github.mudrichenkoevgeny.kmp.core.storage.network.api.open.KtorOpenStorageApi
import io.github.mudrichenkoevgeny.kmp.core.storage.network.api.open.KtorOpenStorageSettingsApi
import io.github.mudrichenkoevgeny.kmp.core.storage.network.api.open.OpenStorageSettingsApi
import io.github.mudrichenkoevgeny.kmp.core.storage.network.websocket.messagehandler.StorageWebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.core.storage.uploader.DirectFileUploader
import io.github.mudrichenkoevgeny.kmp.core.storage.uploader.KtorDirectFileUploader
import io.ktor.client.HttpClient

/**
 * Internal network wiring for the `core:storage` module.
 *
 * Provides Ktor-backed storage REST APIs, [DirectFileUploader], and the storage-domain [WebSocketMessageHandler].
 *
 * @param httpClient Shared Ktor client for REST operations.
 */
internal class StorageNetworkModule(httpClient: HttpClient) {

    /** Ktor-backed implementation of [OpenStorageSettingsApi]. */
    val openStorageSettingsApi: OpenStorageSettingsApi by lazy {
        KtorOpenStorageSettingsApi(httpClient)
    }

    /** Ktor-backed implementation of [StorageApi] for open storage. */
    val openStorageApi: StorageApi by lazy {
        KtorOpenStorageApi(httpClient)
    }

    /** Ktor-backed implementation of [StorageApi] for staff self-service storage. */
    val selfManagementStorageApi: StorageApi by lazy {
        KtorSelfManagementStorageApi(httpClient)
    }

    /** Ktor-backed implementation of [ManagementStorageApi]. */
    val managementStorageApi: ManagementStorageApi by lazy {
        KtorManagementStorageApi(httpClient)
    }

    /** Ktor-backed implementation of [ManagementStorageSettingsApi]. */
    val managementStorageSettingsApi: ManagementStorageSettingsApi by lazy {
        KtorManagementStorageSettingsApi(httpClient)
    }

    /** Ktor-backed implementation of [DirectFileUploader]. */
    val directFileUploader: DirectFileUploader by lazy {
        KtorDirectFileUploader(httpClient)
    }

    /** WebSocket message handler for storage push events. */
    val storageWebSocketMessageHandler: WebSocketMessageHandler by lazy {
        StorageWebSocketMessageHandler()
    }
}