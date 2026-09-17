package io.github.mudrichenkoevgeny.kmp.core.settings.di

import io.github.mudrichenkoevgeny.kmp.core.settings.network.globalsettings.KtorOpenGlobalSettingsApi
import io.github.mudrichenkoevgeny.kmp.core.settings.network.globalsettings.OpenGlobalSettingsApi
import io.ktor.client.HttpClient

/**
 * Internal network wiring for the `core:settings` module.
 *
 * Provides the Ktor-backed [OpenGlobalSettingsApi] for host
 * registration alongside other network services.
 *
 * @param httpClient Shared Ktor client for REST operations.
 */
internal class SettingsNetworkModule(
    httpClient: HttpClient
) {
    /**
     * Ktor-backed implementation of [OpenGlobalSettingsApi].
     */
    val openGlobalSettingsApi: OpenGlobalSettingsApi by lazy {
        KtorOpenGlobalSettingsApi(
            httpClient
        )
    }
}
