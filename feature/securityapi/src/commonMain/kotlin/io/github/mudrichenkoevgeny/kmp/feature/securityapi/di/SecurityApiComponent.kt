package io.github.mudrichenkoevgeny.kmp.feature.securityapi.di

import io.github.mudrichenkoevgeny.kmp.core.security.network.securitysettings.SecuritySettingsApi
import io.ktor.client.HttpClient

/**
 * Root wiring component for `core/security`.
 *
 * Assembles network, repository and use cases. Exposes:
 * - [SecuritySettingsApi] (`securitySettingsApi`) for host registration
 *
 * Constructor dependencies:
 * - [HttpClient]: shared Ktor client (typically from `core/common`) for REST calls.
 */
class SecurityApiComponent(
    httpClient: HttpClient
) {
    private val networkModule by lazy {
        SecurityNetworkModule(
            httpClient
        )
    }
    val securitySettingsApi get() = networkModule.securitySettingsApi
}