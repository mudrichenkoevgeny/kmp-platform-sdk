package io.github.mudrichenkoevgeny.kmp.feature.user.api.auth

import io.github.mudrichenkoevgeny.kmp.feature.user.api.auth.login.KtorLoginApi
import io.github.mudrichenkoevgeny.kmp.feature.user.api.auth.refreshtoken.KtorRefreshTokenApi
import io.ktor.client.HttpClient

class AuthApi(client: HttpClient) {
    val loginApi = KtorLoginApi(client)
    val refreshTokenApi = KtorRefreshTokenApi(client)
}