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

expect fun isNoInternetException(e: Throwable): Boolean