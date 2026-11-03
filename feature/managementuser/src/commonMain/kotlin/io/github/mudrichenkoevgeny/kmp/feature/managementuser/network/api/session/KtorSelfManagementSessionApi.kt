package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.session

import io.github.mudrichenkoevgeny.kmp.core.common.network.httprequest.parameters
import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.session.SessionApi
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.verifytotp.VerifyTotpPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.user.session.list.SelfSessionListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.contract.UserApiPaths
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.DeletedSessionsPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.UserSessionPrivatePayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.UserSessionSummaryPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.route.management.self.user.session.SelfManagementSessionRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

/** [SessionApi] backed by [HttpClient]. */
class KtorSelfManagementSessionApi(
    private val client: HttpClient
) : SessionApi {

    override suspend fun getSessions(
        params: SelfSessionListParams?
    ): AppResult<PagedResult<UserSessionSummaryPayload>> = client.callResult {
        get(SelfManagementSessionRoutes.GET_SESSIONS) {
            parameters(params)
        }
    }

    override suspend fun getSession(userSessionId: UserSessionId): AppResult<UserSessionPrivatePayload> =
        client.callResult {
            get(
                SelfManagementSessionRoutes.GET_SESSION.replace(
                    "{${UserApiPaths.SESSION_ID}}",
                    userSessionId.asHexDashString()
                )
            )
        }

    override suspend fun logout(): AppResult<Unit> = client.callResult {
        post(SelfManagementSessionRoutes.LOGOUT)
    }

    override suspend fun deleteSession(userSessionId: UserSessionId): AppResult<Unit> =
        client.callResult {
            delete(
                SelfManagementSessionRoutes.DELETE_SESSION.replace(
                    "{${UserApiPaths.SESSION_ID}}",
                    userSessionId.asHexDashString()
                )
            )
        }

    override suspend fun deleteAllOtherSessions(): AppResult<DeletedSessionsPayload> =
        client.callResult {
            delete(SelfManagementSessionRoutes.DELETE_ALL_OTHER_SESSIONS)
        }

    override suspend fun reauthenticateSession(request: VerifyTotpPayload): AppResult<Unit> =
        client.callResult {
            post(SelfManagementSessionRoutes.REAUTHENTICATE_SESSION) {
                setBody(request)
            }
        }
}
