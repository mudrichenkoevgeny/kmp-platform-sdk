package io.github.mudrichenkoevgeny.kmp.core.common.network.utils

import io.ktor.client.engine.darwin.DarwinHttpRequestException
import platform.Foundation.NSURLErrorDomain
import platform.Foundation.NSURLErrorNetworkConnectionLost
import platform.Foundation.NSURLErrorNotConnectedToInternet
import platform.Foundation.NSURLErrorTimedOut

/**
 * Checks whether the given exception indicates a lack of network connectivity on iOS platform.
 *
 * @param e The exception to check.
 * @return True if the exception represents a network connectivity failure, false otherwise.
 */
actual fun isNoInternetException(e: Throwable): Boolean {
    val origin = (e as? DarwinHttpRequestException)?.origin

    return if (origin != null && origin.domain == NSURLErrorDomain) {
        when (origin.code) {
            NSURLErrorNotConnectedToInternet -> true
            NSURLErrorNetworkConnectionLost -> true
            NSURLErrorTimedOut -> true
            else -> false
        }
    } else {
        e is kotlinx.coroutines.CancellationException ||
            e.message?.contains("Unable to resolve host") == true
    }
}
