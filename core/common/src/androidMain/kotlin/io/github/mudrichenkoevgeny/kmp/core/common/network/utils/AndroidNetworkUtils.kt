package io.github.mudrichenkoevgeny.kmp.core.common.network.utils

import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

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