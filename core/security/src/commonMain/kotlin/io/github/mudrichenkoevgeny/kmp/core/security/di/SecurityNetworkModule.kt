package io.github.mudrichenkoevgeny.kmp.core.security.di

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.core.security.network.api.securitysettings.KtorSecuritySettingsApi
import io.github.mudrichenkoevgeny.kmp.core.security.network.websocket.messagehandler.SecurityWebSocketMessageHandler
import io.ktor.client.HttpClient

internal class SecurityNetworkModule(httpClient: HttpClient) {
    val securitySettingsApi by lazy {
        KtorSecuritySettingsApi(
            httpClient
        )
    }

    val securityWebSocketMessageHandler: WebSocketMessageHandler by lazy {
        SecurityWebSocketMessageHandler()
    }
}