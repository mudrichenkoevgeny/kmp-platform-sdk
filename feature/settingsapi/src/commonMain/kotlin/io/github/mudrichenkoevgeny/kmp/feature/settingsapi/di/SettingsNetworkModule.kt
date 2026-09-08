package io.github.mudrichenkoevgeny.kmp.feature.settingsapi.di

import io.github.mudrichenkoevgeny.kmp.core.settings.network.globalsettings.OpenGlobalSettingsApi
import io.github.mudrichenkoevgeny.kmp.feature.settingsapi.network.globalsettings.KtorOpenGlobalSettingsApi
import io.ktor.client.HttpClient

/**
 * Internal network wiring for the `feature:settingsapi` module.
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
    val openGlobalSettingsApi by lazy {
        KtorOpenGlobalSettingsApi(
            httpClient
        )
    }
}