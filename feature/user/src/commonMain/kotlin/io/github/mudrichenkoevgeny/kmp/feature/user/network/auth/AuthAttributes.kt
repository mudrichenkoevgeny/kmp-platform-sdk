package io.github.mudrichenkoevgeny.kmp.feature.user.network.auth

import io.ktor.util.AttributeKey

private const val IS_PUBLIC_API_NAME = "IsPublicApi"

val IsPublicApi = AttributeKey<Boolean>(IS_PUBLIC_API_NAME)