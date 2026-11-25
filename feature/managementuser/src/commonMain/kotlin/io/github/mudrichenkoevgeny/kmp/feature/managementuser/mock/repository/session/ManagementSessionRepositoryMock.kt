package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.session

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.session.ManagementSessionRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.session.globallist.ManagementGlobalSessionListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.session.userlist.ManagementUserSessionListParams
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.common.domain.model.listing.createTestPagedResult
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.session.createTestUserSessionPrivate
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.session.createTestUserSessionSummary

/**
 * Mock implementation of [ManagementSessionRepository] for testing.
 */
@InternalApi
public open class ManagementSessionRepositoryMock : ManagementSessionRepository {

    public var getSessionsResultProvider: () -> AppResult<PagedResult<UserSessionSummary>> = {
        AppResult.Success(createTestPagedResult(listOf(createTestUserSessionSummary())))
    }

    public var getUserSessionsResultProvider:
            (ManagementUserSessionListParams) -> AppResult<PagedResult<UserSessionSummary>> = { _ ->
        AppResult.Success(createTestPagedResult(listOf(createTestUserSessionSummary())))
    }

    public var getSessionResultProvider: (UserSessionId) -> AppResult<UserSessionPrivate> = {
        AppResult.Success(createTestUserSessionPrivate())
    }

    public var deleteSessionResultProvider: (UserId, UserSessionId) -> AppResult<Unit> = { _, _ ->
        AppResult.Success(Unit)
    }

    public var deleteAllUserSessionsResultProvider: (UserId) -> AppResult<Unit> = { _ -> AppResult.Success(Unit) }

    public var lastUserId: UserId? = null
    public var lastSessionId: UserSessionId? = null
    public var lastGlobalParams: ManagementGlobalSessionListParams? = null
    public var lastUserParams: ManagementUserSessionListParams? = null

    public override suspend fun getSessions(
        params: ManagementGlobalSessionListParams?
    ): AppResult<PagedResult<UserSessionSummary>> {
        lastGlobalParams = params
        return getSessionsResultProvider()
    }

    public override suspend fun getUserSessions(
        params: ManagementUserSessionListParams
    ): AppResult<PagedResult<UserSessionSummary>> {
        lastUserParams = params
        return getUserSessionsResultProvider(params)
    }

    public override suspend fun getSession(sessionId: UserSessionId): AppResult<UserSessionPrivate> {
        lastSessionId = sessionId
        return getSessionResultProvider(sessionId)
    }

    public override suspend fun deleteSession(
        userId: UserId,
        sessionId: UserSessionId
    ): AppResult<Unit> {
        lastUserId = userId
        lastSessionId = sessionId
        return deleteSessionResultProvider(userId, sessionId)
    }

    public override suspend fun deleteAllUserSessions(userId: UserId): AppResult<Unit> {
        lastUserId = userId
        return deleteAllUserSessionsResultProvider(userId)
    }
}
