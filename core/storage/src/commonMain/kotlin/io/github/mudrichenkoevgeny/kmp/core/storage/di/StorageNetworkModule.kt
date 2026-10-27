package io.github.mudrichenkoevgeny.kmp.core.storage.di

import io.github.mudrichenkoevgeny.kmp.core.storage.network.api.storagesettings.KtorOpenStorageSettingsApi
import io.github.mudrichenkoevgeny.kmp.core.storage.network.api.storagesettings.OpenStorageSettingsApi
import io.github.mudrichenkoevgeny.kmp.core.storage.uploader.DirectFileUploader
import io.github.mudrichenkoevgeny.kmp.core.storage.uploader.KtorDirectFileUploader
import io.ktor.client.HttpClient

/**
 * Internal network wiring for the `core:storage` module.
 *
 * Provides the Ktor-backed [OpenStorageSettingsApi] and [DirectFileUploader] for host
 * registration alongside other network services.
 *
 * @param httpClient Shared Ktor client for REST operations.
 */
internal class StorageNetworkModule(httpClient: HttpClient) {
    /**
     * Ktor-backed implementation of [OpenStorageSettingsApi].
     */
    val openStorageSettingsApi: OpenStorageSettingsApi by lazy {
        KtorOpenStorageSettingsApi(
            httpClient
        )
    }

    /**
     * Ktor-backed implementation of [DirectFileUploader].
     */
    val directFileUploader: DirectFileUploader by lazy {
        KtorDirectFileUploader(
            httpClient
        )
    }
}
