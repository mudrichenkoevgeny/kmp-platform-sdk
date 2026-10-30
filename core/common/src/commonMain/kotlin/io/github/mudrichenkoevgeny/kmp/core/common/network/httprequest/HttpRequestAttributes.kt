package io.github.mudrichenkoevgeny.kmp.core.common.network.httprequest

import io.ktor.util.AttributeKey

private const val IS_PUBLIC_API_NAME = "IsPublicApi"

/**
 * Request attribute set by [markAsPublic] so the bearer plugin skips attaching credentials on that call.
 */
val IsPublicApi = AttributeKey<Boolean>(IS_PUBLIC_API_NAME)