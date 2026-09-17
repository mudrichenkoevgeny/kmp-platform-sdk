package io.github.mudrichenkoevgeny.kmp.core.security.di

import io.github.mudrichenkoevgeny.kmp.core.security.network.securitysettings.KtorOpenSecuritySettingsApi
import io.github.mudrichenkoevgeny.kmp.core.security.network.securitysettings.OpenSecuritySettingsApi
import io.ktor.client.HttpClient

/**
 * Internal network wiring for the `core:security` module.
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
    val openSecuritySettingsApi: OpenSecuritySettingsApi by lazy {
        KtorOpenSecuritySettingsApi(
            httpClient
        )
    }
}
