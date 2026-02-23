package io.github.mudrichenkoevgeny.kmp.core.common.network.httpclient

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.logging.Logger

fun interface HttpClientConfigPlugin {
    fun install(config: HttpClientConfig<out HttpClientEngineConfig>, networkLogger: Logger)
}