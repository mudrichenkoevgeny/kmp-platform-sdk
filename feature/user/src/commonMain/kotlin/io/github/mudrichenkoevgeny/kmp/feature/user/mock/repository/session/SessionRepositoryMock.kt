package io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.session

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.session.SessionRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.user.session.list.SelfSessionListParams
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.common.domain.model.listing.createTestPagedResult
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.session.createTestUserSessionPrivate
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.session.createTestUserSessionSummary

/**
 * Mock implementation of [SessionRepository] for testing.
 */
@InternalApi
public open class SessionRepositoryMock : SessionRepository {

    public var getSessionsResultProvider: () -> AppResult<PagedResult<UserSessionSummary>> = {
        AppResult.Success(createTestPagedResult(listOf(createTestUserSessionSummary())))
    }

    public var getSessionResultProvider: (UserSessionId) -> AppResult<UserSessionPrivate> = {
        AppResult.Success(createTestUserSessionPrivate())
    }

    public var logoutResultProvider: () -> AppResult<Unit> = { AppResult.Success(Unit) }

    public var deleteSessionResultProvider: (UserSessionId) -> AppResult<Unit> = { _ -> AppResult.Success(Unit) }

    public var deleteAllOtherSessionsResultProvider: () -> AppResult<Unit> = { AppResult.Success(Unit) }

    public var reauthenticateSessionResultProvider: (String, String) -> AppResult<Unit> = { _, _ ->
        AppResult.Success(Unit)
    }

    public var lastSessionId: UserSessionId? = null
    public var lastMfaToken: String? = null
    public var lastCode: String? = null

    public override suspend fun getSessions(
        params: SelfSessionListParams?
    ): AppResult<PagedResult<UserSessionSummary>> = getSessionsResultProvider()

    public override suspend fun getSession(userSessionId: UserSessionId): AppResult<UserSessionPrivate> {
        lastSessionId = userSessionId
        return getSessionResultProvider(userSessionId)
    }

    public override suspend fun logout(): AppResult<Unit> = logoutResultProvider()

    public override suspend fun deleteSession(userSessionId: UserSessionId): AppResult<Unit> {
        lastSessionId = userSessionId
        return deleteSessionResultProvider(userSessionId)
    }

    public override suspend fun deleteAllOtherSessions(): AppResult<Unit> = deleteAllOtherSessionsResultProvider()

    public override suspend fun reauthenticateSession(
        mfaToken: String,
        code: String
    ): AppResult<Unit> {
        lastMfaToken = mfaToken
        lastCode = code
        return reauthenticateSessionResultProvider(mfaToken, code)
    }
}
