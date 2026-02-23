package io.github.mudrichenkoevgeny.kmp.feature.user.network.httpclient

import io.github.mudrichenkoevgeny.kmp.core.common.network.httpclient.HttpClientConfigPlugin
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.logging.Logger

class AuthHttpClientConfigPlugin(
    private val baseUrl: String,
    private val authStorage: AuthStorage
) : HttpClientConfigPlugin {
    override fun install(
        config: HttpClientConfig<out HttpClientEngineConfig>,
        networkLogger: Logger
    ) {
        config.setupAuthConfig(
            baseUrl = baseUrl,
            networkLogger = networkLogger,
            authStorage = authStorage
        )
    }
}