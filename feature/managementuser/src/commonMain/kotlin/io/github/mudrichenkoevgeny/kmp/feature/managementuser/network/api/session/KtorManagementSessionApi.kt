package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.session

import io.github.mudrichenkoevgeny.kmp.core.common.network.httprequest.parameters
import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.session.globallist.ManagementGlobalSessionListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.session.userlist.ManagementUserSessionListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.contract.UserApiPaths
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.UserSessionPrivatePayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.UserSessionSummaryPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.route.management.users.sessions.ManagementSessionsRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter

/** [ManagementSessionApi] backed by [HttpClient]. */
public class KtorManagementSessionApi(
    private val client: HttpClient
) : ManagementSessionApi {

    override suspend fun getSessions(
        params: ManagementGlobalSessionListParams?
    ): AppResult<PagedResult<UserSessionSummaryPayload>> = client.callResult {
        get(ManagementSessionsRoutes.GET_SESSIONS) {
            parameters(params)
        }
    }

    override suspend fun getUserSessions(
        params: ManagementUserSessionListParams
    ): AppResult<PagedResult<UserSessionSummaryPayload>> = client.callResult {
        get(
            ManagementSessionsRoutes.GET_USER_SESSIONS
                .replace(
                    "{${UserApiPaths.USER_ID}}",
                    params.userId
                )
        ) {
            parameters(params)
        }
    }

    override suspend fun getSession(sessionId: String): AppResult<UserSessionPrivatePayload> = client.callResult {
        get(ManagementSessionsRoutes.GET_SESSION) {
            parameter(UserApiPaths.SESSION_ID, sessionId)
        }
    }

    override suspend fun deleteSession(
        userId: UserId,
        sessionId: String
    ): AppResult<Unit> = client.callResult {
        delete(ManagementSessionsRoutes.DELETE_SESSION) {
            parameter(UserApiPaths.USER_ID, userId.value)
            parameter(UserApiPaths.SESSION_ID, sessionId)
        }
    }

    override suspend fun deleteAllUserSessions(userId: UserId): AppResult<Unit> = client.callResult {
        delete(ManagementSessionsRoutes.DELETE_ALL_USER_SESSIONS) {
            parameter(UserApiPaths.USER_ID, userId.value)
        }
    }
}
