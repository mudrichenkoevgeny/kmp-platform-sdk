package io.github.mudrichenkoevgeny.kmp.feature.user.repository.session

import io.github.mudrichenkoevgeny.kmp.core.common.mapper.pagedresult.mapItems
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.session.SessionApi
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.user.UserStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.verifytotp.VerifyTotpPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.toUserSessionIdOrNull
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.session.toUserSession

/**
 * Implements [SessionRepository] using [SessionApi].
 *
 * @param sessionApi HTTP endpoints for session management and re-authentication.
 */
class SessionRepositoryImpl(
    private val sessionApi: SessionApi,
    private val userStorage: UserStorage
) : SessionRepository {

    override suspend fun getSessions(): AppResult<PagedResult<UserSession>> {
        return sessionApi.getSessions().mapSuccess { pagedPayload ->
            userStorage.updateUserSessionsPayloadList(pagedPayload)
            pagedPayload.mapItems { it.toUserSession() }
        }
    }

    override suspend fun getSession(userSessionId: UserSessionId): AppResult<UserSession> {
        return sessionApi.getSession(userSessionId).mapSuccess { it.toUserSession() }
    }

    override suspend fun logout(): AppResult<Unit> {
        return sessionApi.logout()
    }

    override suspend fun deleteSession(userSessionId: UserSessionId): AppResult<Unit> {
        return sessionApi.deleteSession(userSessionId).mapSuccess {
            userStorage.removeUserSession(userSessionId)
        }
    }

    override suspend fun deleteAllOtherSessions(): AppResult<Unit> {
        return sessionApi.deleteAllOtherSessions().mapSuccess { payload ->
            val sessionIds = payload.deletedSessionIds.mapNotNull { userSessionId ->
                userSessionId.toUserSessionIdOrNull()
            }

            if (sessionIds.isNotEmpty()) {
                userStorage.removeUserSessions(sessionIds)
            }
        }
    }

    override suspend fun reauthenticateSession(mfaToken: String, code: String): AppResult<Unit> {
        return sessionApi.reauthenticateSession(
            VerifyTotpPayload(
                mfaToken = mfaToken,
                code = code
            )
        )
    }
}