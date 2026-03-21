package io.github.mudrichenkoevgeny.kmp.core.common.network.utils

import io.github.mudrichenkoevgeny.kmp.core.common.error.mapper.toServerError
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.ApiException
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import kotlin.coroutines.cancellation.CancellationException

/**
 * Executes an HTTP call and converts its result into [AppResult].
 *
 * Error mapping:
 * - [ApiException] -> server payload into [CommonError] via [toServerError]
 * - IO/network errors -> `CommonError.NoInternetConnection` or `CommonError.Network`
 * - JSON/serialization errors -> `CommonError.ContractViolation`
 *
 * @param isRetryable Indicates whether the operation should be treated as retryable when an IO failure happens.
 */
suspend inline fun <reified T> HttpClient.callResult(
    isRetryable: Boolean = false,
    block: HttpClient.() -> HttpResponse
): AppResult<T> = try {
    val response = this.block()
    AppResult.Success(response.body())
} catch (e: ApiException) {
    AppResult.Error(e.apiErrorResponse.toServerError(isRetryable))
} catch (e: IOException) {
    val error = if (isNoInternetException(e)) {
        CommonError.NoInternetConnection(e, isRetryable)
    } else {
        CommonError.Network(e, isRetryable)
    }
    AppResult.Error(error)
} catch (e: SerializationException) {
    AppResult.Error(CommonError.ContractViolation(e))
} catch (t: Throwable) {
    if (t is CancellationException) {
        throw t
    }
    AppResult.Error(CommonError.Internal(t, isRetryable))
}

/**
 * Expected platform check for detecting "no internet" conditions from a transport exception.
 *
 * The networking layer should use this to decide between `CommonError.NoInternetConnection` and `CommonError.Network`.
 */
expect fun isNoInternetException(e: Throwable): Boolean