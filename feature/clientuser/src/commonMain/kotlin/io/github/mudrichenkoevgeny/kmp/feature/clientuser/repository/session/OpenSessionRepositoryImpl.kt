package io.github.mudrichenkoevgeny.kmp.feature.clientuser.repository.session

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.session.SessionApi
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.session.SessionRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.user.UserStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.mapper.pagedresult.mapItems
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.verifytotp.VerifyTotpPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.session.toUserSessionPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.session.toUserSessionSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.user.session.list.SelfSessionListParams

/**
 * Implements [SessionRepository] using [SessionApi].
 *
 * @param sessionApi HTTP endpoints for session management and re-authentication.
 * @param userStorage Local persistent storage cache.
 * @param authStorage Auth storage for tokens.
 */
class OpenSessionRepositoryImpl(
    private val sessionApi: SessionApi,
    private val userStorage: UserStorage,
    private val authStorage: AuthStorage
) : SessionRepository {

    override suspend fun getSessions(
        params: SelfSessionListParams?
    ): AppResult<PagedResult<UserSessionSummary>> = sessionApi.getSessions(params).mapSuccess { pagedPayload ->
        pagedPayload.mapItems { it.toUserSessionSummary() }
    }

    override suspend fun getSession(userSessionId: UserSessionId): AppResult<UserSessionPrivate> =
        sessionApi.getSession(
            userSessionId
        ).mapSuccess {
            it.toUserSessionPrivate()
        }

    override suspend fun logout(): AppResult<Unit> = sessionApi.logout().onSuccess {
        userStorage.clear()
        authStorage.clearTokens()
    }

    override suspend fun deleteSession(userSessionId: UserSessionId): AppResult<Unit> = sessionApi.deleteSession(
        userSessionId
    )

    override suspend fun deleteAllOtherSessions(): AppResult<Unit> = sessionApi.deleteAllOtherSessions().mapSuccess { }

    override suspend fun reauthenticateSession(
        mfaToken: String,
        code: String
    ): AppResult<Unit> = sessionApi.reauthenticateSession(
        VerifyTotpPayload(
            mfaToken = mfaToken,
            code = code
        )
    )
}
