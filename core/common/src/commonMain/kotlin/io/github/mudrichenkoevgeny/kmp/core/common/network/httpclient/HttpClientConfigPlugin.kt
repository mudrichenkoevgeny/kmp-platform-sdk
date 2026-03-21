package io.github.mudrichenkoevgeny.kmp.core.common.network.httpclient

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.logging.Logger

/**
 * Allows feature modules to extend the shared Ktor HTTP client configuration.
 *
 * Implementations are installed by `setupCommonConfig` inside `core/common` and can:
 * - add authentication headers/plugins
 * - configure request/response behavior
 * - register additional serializers or logging
 */
fun interface HttpClientConfigPlugin {
    /**
     * Install this plugin into the provided Ktor [config].
     *
     * @param config Ktor client configuration (client plugins installation scope).
     * @param networkLogger SDK logger instance for consistent logging.
     */
    fun install(config: HttpClientConfig<out HttpClientEngineConfig>, networkLogger: Logger)
}