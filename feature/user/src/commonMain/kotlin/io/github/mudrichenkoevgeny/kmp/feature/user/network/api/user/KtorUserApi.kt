package io.github.mudrichenkoevgeny.kmp.feature.user.network.api.user

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.user.CurrentUserResponse
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.route.user.UserRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get

class KtorUserApi(
    private val client: HttpClient
) : UserApi {

    override suspend fun getUser(): AppResult<CurrentUserResponse> = client.callResult {
        get(UserRoutes.GET_USER)
    }

    override suspend fun deleteUser(): AppResult<Unit> = client.callResult {
        delete(UserRoutes.DELETE_USER)
    }
}