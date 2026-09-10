package io.github.mudrichenkoevgeny.kmp.feature.user.network.httpclient

import io.github.mudrichenkoevgeny.kmp.core.common.network.httpclient.HttpClientConfigPlugin
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.logging.Logger

/**
 * [HttpClientConfigPlugin] that installs user-auth behavior (base URL, logging hooks, bearer/refresh wiring)
 * via [setupAuthConfig], using the given [authStorage] and [refreshTokenRoute].
 *
 * Pass an instance to `CommonComponent` so all feature HTTP calls share the same token lifecycle.
 *
 * @param baseUrl API origin used when configuring the client.
 * @param authStorage Source of access/refresh tokens and related auth state for the plugin.
 * @param refreshTokenRoute Explicit route path for token refresh operations.
 */
class AuthHttpClientConfigPlugin(
    private val baseUrl: String,
    private val authStorage: AuthStorage,
    private val refreshTokenRoute: String,
    private val onSessionCleared: (suspend () -> Unit)? = null
) : HttpClientConfigPlugin {
    override fun install(
        config: HttpClientConfig<out HttpClientEngineConfig>,
        networkLogger: Logger
    ) {
        config.setupAuthConfig(
            baseUrl = baseUrl,
            networkLogger = networkLogger,
            authStorage = authStorage,
            refreshTokenRoute = refreshTokenRoute,
            onSessionCleared = onSessionCleared
        )
    }
}
