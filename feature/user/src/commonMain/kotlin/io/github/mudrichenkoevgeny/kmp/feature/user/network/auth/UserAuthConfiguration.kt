package io.github.mudrichenkoevgeny.kmp.feature.user.network.auth

import io.ktor.client.request.HttpRequestBuilder

fun HttpRequestBuilder.markAsPublic() {
    attributes.put(IsPublicApi, true)
}