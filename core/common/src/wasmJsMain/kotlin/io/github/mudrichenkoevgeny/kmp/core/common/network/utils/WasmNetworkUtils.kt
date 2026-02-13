package io.github.mudrichenkoevgeny.kmp.core.common.network.utils

actual fun isNoInternetException(e: Throwable): Boolean {
    val message = e.message?.lowercase() ?: ""

    return message.contains("fetch") ||
            message.contains("networkerror") ||
            message.contains("failed to fetch") ||
            message.contains("dns")
}