package io.github.mudrichenkoevgeny.kmp.core.common.network.utils

/**
 * Wasm/JS implementation of [isNoInternetException] used when mapping transport errors in [callResult].
 *
 * Heuristic only: inspects the lowercased [Throwable.message] for `fetch`, `networkerror`, `failed to fetch`,
 * or `dns`, which commonly appear when the browser cannot complete a request.
 *
 * @param e Failure from `fetch`, WebSocket, or other JS I/O.
 * @return `true` when the message suggests a connectivity or name-resolution problem.
 */
actual fun isNoInternetException(e: Throwable): Boolean {
    val message = e.message?.lowercase() ?: ""

    return message.contains("fetch") ||
            message.contains("networkerror") ||
            message.contains("failed to fetch") ||
            message.contains("dns")
}