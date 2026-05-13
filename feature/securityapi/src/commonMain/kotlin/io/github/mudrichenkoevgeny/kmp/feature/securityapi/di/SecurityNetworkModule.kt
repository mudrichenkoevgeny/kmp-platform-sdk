package io.github.mudrichenkoevgeny.kmp.feature.securityapi.di

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.feature.securityapi.network.securitysettings.KtorSecuritySettingsApi
import io.github.mudrichenkoevgeny.kmp.core.security.network.securitysettings.SecuritySettingsApi
import io.ktor.client.HttpClient

/**
 * Internal network wiring for `core/security`.
 *
 * Provides the Ktor-backed [SecuritySettingsApi] for host
 * registration alongside other [WebSocketMessageHandler] instances.
 */
internal class SecurityNetworkModule(httpClient: HttpClient) {
    /**
     * Ktor-backed implementation of [SecuritySettingsApi].
     */
    val securitySettingsApi by lazy {
        KtorSecuritySettingsApi(
            httpClient
        )
    }
}