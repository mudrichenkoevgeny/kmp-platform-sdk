package io.github.mudrichenkoevgeny.kmp.core.common.network.utils

import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Android implementation of [isNoInternetException] used when mapping transport errors in [callResult].
 *
 * Returns `true` for [UnknownHostException], [ConnectException], and [SocketTimeoutException]. For other
 * [IOException] types, matches common Android/Linux message fragments such as unresolved host, no route,
 * or connection refused.
 *
 * @param e Failure from HTTP/WebSocket or lower-level I/O.
 * @return `true` if the SDK should treat this as a no-internet style condition.
 */
actual fun isNoInternetException(e: Throwable): Boolean {
    return when (e) {
        is UnknownHostException -> true
        is ConnectException -> true
        is SocketTimeoutException -> true
        is IOException -> {
            val message = e.message?.lowercase() ?: ""
            message.contains("unable to resolve host") ||
                    message.contains("no route to host") ||
                    message.contains("connection refused")
        }
        else -> false
    }
}