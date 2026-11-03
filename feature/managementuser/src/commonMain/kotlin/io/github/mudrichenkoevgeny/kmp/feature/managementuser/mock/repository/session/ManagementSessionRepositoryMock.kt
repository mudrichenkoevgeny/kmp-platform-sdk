package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.session

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.session.ManagementSessionRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.session.globallist.ManagementGlobalSessionListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.session.userlist.ManagementUserSessionListParams
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.common.domain.model.listing.createTestPagedResult
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.session.createTestUserSessionPrivate
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.session.createTestUserSessionSummary

@InternalApi
open class ManagementSessionRepositoryMock : ManagementSessionRepository {

    var getSessionsResultProvider: () -> AppResult<PagedResult<UserSessionSummary>> = {
        AppResult.Success(createTestPagedResult(listOf(createTestUserSessionSummary())))
    }

    var getUserSessionsResultProvider: (ManagementUserSessionListParams) -> AppResult<PagedResult<UserSessionSummary>> = { _ ->
        AppResult.Success(createTestPagedResult(listOf(createTestUserSessionSummary())))
    }

    var getSessionResultProvider: (String) -> AppResult<UserSessionPrivate> = {
        AppResult.Success(createTestUserSessionPrivate())
    }

    var deleteSessionResultProvider: (UserId, String) -> AppResult<Unit> = { _, _ -> AppResult.Success(Unit) }

    var deleteAllUserSessionsResultProvider: (UserId) -> AppResult<Unit> = { _ -> AppResult.Success(Unit) }

    var lastUserId: UserId? = null
    var lastSessionId: String? = null
    var lastGlobalParams: ManagementGlobalSessionListParams? = null
    var lastUserParams: ManagementUserSessionListParams? = null

    override suspend fun getSessions(
        params: ManagementGlobalSessionListParams?
    ): AppResult<PagedResult<UserSessionSummary>> {
        lastGlobalParams = params
        return getSessionsResultProvider()
    }

    override suspend fun getUserSessions(
        params: ManagementUserSessionListParams
    ): AppResult<PagedResult<UserSessionSummary>> {
        lastUserParams = params
        return getUserSessionsResultProvider(params)
    }

    override suspend fun getSession(sessionId: String): AppResult<UserSessionPrivate> {
        lastSessionId = sessionId
        return getSessionResultProvider(sessionId)
    }

    override suspend fun deleteSession(userId: UserId, sessionId: String): AppResult<Unit> {
        lastUserId = userId
        lastSessionId = sessionId
        return deleteSessionResultProvider(userId, sessionId)
    }

    override suspend fun deleteAllUserSessions(userId: UserId): AppResult<Unit> {
        lastUserId = userId
        return deleteAllUserSessionsResultProvider(userId)
    }
}
