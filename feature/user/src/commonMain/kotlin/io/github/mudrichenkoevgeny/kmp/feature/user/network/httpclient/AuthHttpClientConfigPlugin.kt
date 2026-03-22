package io.github.mudrichenkoevgeny.kmp.feature.user.network.httpclient

import io.github.mudrichenkoevgeny.kmp.core.common.network.httpclient.HttpClientConfigPlugin
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.logging.Logger

/**
 * [HttpClientConfigPlugin] that installs user-auth behavior (base URL, logging hooks, bearer/refresh wiring)
 * via [setupAuthConfig], using the given [authStorage].
 *
 * Pass an instance to `CommonComponent` so all feature HTTP calls share the same token lifecycle.
 *
 * @param baseUrl API origin used when configuring the client.
 * @param authStorage Source of access/refresh tokens and related auth state for the plugin.
 */
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