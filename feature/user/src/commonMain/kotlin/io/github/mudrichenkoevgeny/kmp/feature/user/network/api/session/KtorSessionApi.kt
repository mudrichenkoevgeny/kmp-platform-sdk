package io.github.mudrichenkoevgeny.kmp.feature.user.network.api.session

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.contract.CommonApiFields
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.session.UserSessionResponse
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.route.session.SessionRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post

class KtorSessionApi(
    private val client: HttpClient
) : SessionApi {

    override suspend fun getSessions(): AppResult<List<UserSessionResponse>> = client.callResult {
        get(SessionRoutes.GET_SESSIONS)
    }

    override suspend fun logout(): AppResult<Unit> = client.callResult {
        post(SessionRoutes.LOGOUT)
    }

    override suspend fun deleteSession(userSessionId: String): AppResult<Unit> = client.callResult {
        delete(SessionRoutes.DELETE_SESSION.replace(
            "{${CommonApiFields.ID}}",
            userSessionId)
        )
    }

    override suspend fun deleteAllOtherSessions(): AppResult<Unit> = client.callResult {
        delete(SessionRoutes.DELETE_ALL_OTHER_SESSIONS)
    }
}