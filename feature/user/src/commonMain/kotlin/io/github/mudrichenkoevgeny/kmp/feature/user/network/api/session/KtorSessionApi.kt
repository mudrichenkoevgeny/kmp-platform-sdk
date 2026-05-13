package io.github.mudrichenkoevgeny.kmp.feature.user.network.api.session

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.contract.CommonApiFields
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.verifytotp.VerifyTotpPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.DeletedSessionsPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.UserSessionPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.route.open.session.OpenSessionRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

/** [SessionApi] backed by [HttpClient]. */
class KtorSessionApi(
    private val client: HttpClient
) : SessionApi {

    override suspend fun getSessions(): AppResult<PagedResult<UserSessionPayload>> =
        client.callResult {
            get(OpenSessionRoutes.GET_SESSIONS)
        }

    override suspend fun getSession(userSessionId: UserSessionId): AppResult<UserSessionPayload> =
        client.callResult {
            get(
                OpenSessionRoutes.GET_SESSION.replace(
                    "{${CommonApiFields.ID}}",
                    userSessionId.asHexDashString()
                )
            )
        }

    override suspend fun logout(): AppResult<Unit> = client.callResult {
        post(OpenSessionRoutes.LOGOUT)
    }

    override suspend fun deleteSession(userSessionId: UserSessionId): AppResult<Unit> =
        client.callResult {
            delete(
                OpenSessionRoutes.DELETE_SESSION.replace(
                    "{${CommonApiFields.ID}}",
                    userSessionId.asHexDashString()
                )
            )
    }

    override suspend fun deleteAllOtherSessions(): AppResult<DeletedSessionsPayload> =
        client.callResult {
            delete(OpenSessionRoutes.DELETE_ALL_OTHER_SESSIONS)
        }

    override suspend fun reauthenticateSession(request: VerifyTotpPayload): AppResult<Unit> =
        client.callResult  {
            post(OpenSessionRoutes.REAUTHENTICATE_SESSION) {
                setBody(request)
            }
        }
}