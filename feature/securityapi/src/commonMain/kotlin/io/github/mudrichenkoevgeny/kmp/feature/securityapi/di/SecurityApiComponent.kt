package io.github.mudrichenkoevgeny.kmp.feature.securityapi.di

import io.github.mudrichenkoevgeny.kmp.core.security.network.securitysettings.OpenSecuritySettingsApi
import io.ktor.client.HttpClient

/**
 * Root wiring component for the `feature:securityapi` module (networking implementation for `core:security`).
 *
 * Assembles the network layer and exposes:
 * - [OpenSecuritySettingsApi] (`securitySettingsApi`) for host registration
 *
 * Constructor dependencies:
 * - [HttpClient]: shared Ktor client (typically from `core:common`) for REST calls.
 */
class SecurityApiComponent(
    httpClient: HttpClient
) {
    private val networkModule by lazy {
        SecurityNetworkModule(
            httpClient
        )
    }

    /**
     * Ktor-backed implementation of [OpenSecuritySettingsApi].
     */
    val openSecuritySettingsApi get() = networkModule.openSecuritySettingsApi
}