package io.github.mudrichenkoevgeny.kmp.core.common.network.httprequest

import io.github.mudrichenkoevgeny.shared.foundation.core.common.navigation.params.NavigationScreenParams
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.parameter

/**
 * Marks this request as a public (unauthenticated) API call so [IsPublicApi] is set and the bearer
 * `sendWithoutRequest` hook (installed with the user HTTP client) does not attach credentials.
 */
fun HttpRequestBuilder.markAsPublic() {
    attributes.put(IsPublicApi, true)
}


/**
 * Automatically appends all query parameters serialized from [NavigationScreenParams] to the Ktor request.
 *
 * @param params Parameter object implementing [NavigationScreenParams], or `null`.
 */
fun HttpRequestBuilder.parameters(params: NavigationScreenParams<*>?) {
    params?.toQueryMap()?.forEach { (key, values) ->
        values.forEach { value ->
            parameter(key, value)
        }
    }
}