package io.github.mudrichenkoevgeny.kmp.feature.settingsapi.di

import io.github.mudrichenkoevgeny.kmp.core.settings.network.globalsettings.GlobalSettingsApi
import io.ktor.client.HttpClient

/**
 * Root wiring component for `core/settings`.
 *
 * - [GlobalSettingsApi] (`globalSettingsApi`) for host registration
 *
 * Constructor dependencies:
 * - [HttpClient]: shared Ktor client (typically from `core/common`) for REST calls.
 */
class SettingsApiComponent(
    httpClient: HttpClient
) {
    private val networkModule by lazy {
        SettingsNetworkModule(
            httpClient = httpClient
        )
    }
    val globalSettingsApi get() = networkModule.globalSettingsApi
}