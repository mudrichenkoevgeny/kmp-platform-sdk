package io.github.mudrichenkoevgeny.kmp.feature.user.network.auth

import io.ktor.client.request.HttpRequestBuilder

/**
 * Marks this request as a public (unauthenticated) API call so [IsPublicApi] is set and the bearer
 * `sendWithoutRequest` hook (installed with the user HTTP client) does not attach credentials.
 */
fun HttpRequestBuilder.markAsPublic() {
    attributes.put(IsPublicApi, true)
}