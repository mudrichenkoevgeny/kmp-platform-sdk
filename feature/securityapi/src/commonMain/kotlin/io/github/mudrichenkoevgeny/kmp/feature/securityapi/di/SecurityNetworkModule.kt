package io.github.mudrichenkoevgeny.kmp.feature.securityapi.di

import io.github.mudrichenkoevgeny.kmp.feature.securityapi.network.securitysettings.KtorOpenSecuritySettingsApi
import io.github.mudrichenkoevgeny.kmp.core.security.network.securitysettings.OpenSecuritySettingsApi
import io.ktor.client.HttpClient

/**
 * Internal network wiring for the `feature:securityapi` module.
 *
 * Provides the Ktor-backed [OpenSecuritySettingsApi] for host
 * registration alongside other network services.
 *
 * @param httpClient Shared Ktor client for REST operations.
 */
internal class SecurityNetworkModule(httpClient: HttpClient) {
    /**
     * Ktor-backed implementation of [OpenSecuritySettingsApi].
     */
    val openSecuritySettingsApi by lazy {
        KtorOpenSecuritySettingsApi(
            httpClient
        )
    }
}