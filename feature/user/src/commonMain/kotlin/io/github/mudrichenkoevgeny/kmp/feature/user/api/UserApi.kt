package io.github.mudrichenkoevgeny.kmp.feature.user.api

import io.github.mudrichenkoevgeny.kmp.feature.user.api.auth.AuthApi
import io.ktor.client.HttpClient

class UserApi(client: HttpClient) {
    val auth = AuthApi(client)
}