package io.github.mudrichenkoevgeny.kmp.feature.settingsapi.di

import io.github.mudrichenkoevgeny.kmp.core.settings.network.globalsettings.GlobalSettingsApi
import io.ktor.client.HttpClient

/**
 * Root wiring component for the `feature:settingsapi` module (networking implementation for `core:settings`).
 *
 * Exposes:
 * - [GlobalSettingsApi] (`globalSettingsApi`) for host registration
 *
 * @param httpClient Shared Ktor client (typically from `core:common`) for REST calls.
 */
class SettingsApiComponent(
    httpClient: HttpClient
) {
    private val networkModule by lazy {
        SettingsNetworkModule(
            httpClient = httpClient
        )
    }

    /**
     * Ktor-backed implementation of [GlobalSettingsApi].
     */
    val globalSettingsApi get() = networkModule.globalSettingsApi
}