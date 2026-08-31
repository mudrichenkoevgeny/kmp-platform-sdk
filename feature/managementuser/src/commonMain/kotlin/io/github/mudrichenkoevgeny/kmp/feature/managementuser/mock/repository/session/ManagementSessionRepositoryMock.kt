package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.session

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.domain.model.listing.pagedResultMock
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.session.ManagementSessionRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.session.userSessionMock
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId

@InternalApi
open class ManagementSessionRepositoryMock : ManagementSessionRepository {

    var getSessionsResultProvider: () -> AppResult<PagedResult<UserSession>> = {
        AppResult.Success(pagedResultMock(listOf(userSessionMock())))
    }

    var getSessionResultProvider: (String) -> AppResult<UserSession> = {
        AppResult.Success(userSessionMock())
    }

    var deleteSessionResultProvider: (UserId, String) -> AppResult<Unit> = { _, _ -> AppResult.Success(Unit) }

    var deleteAllUserSessionsResultProvider: (UserId) -> AppResult<Unit> = { _ -> AppResult.Success(Unit) }

    var lastUserId: UserId? = null
    var lastSessionId: String? = null

    override suspend fun getSessions(
        pageNumber: Int?,
        pageSize: Int?,
        sortBy: UserSortValues.UserSessionSortBy?,
        sortOrder: SortOrder?,
        userIds: List<String>?,
        userRoles: List<UserRole>?,
        identifiers: List<String>?,
        identifierIds: List<String>?,
        userAuthProviders: List<UserAuthProvider>?,
        clientTypes: List<ClientType>?,
        userAgents: List<String>?,
        ipAddresses: List<String>?,
        languages: List<String>?,
        deviceIds: List<String>?,
        deviceNames: List<String>?,
        appVersions: List<String>?,
        operationSystemVersions: List<String>?
    ): AppResult<PagedResult<UserSession>> = getSessionsResultProvider()

    override suspend fun getSession(sessionId: String): AppResult<UserSession> {
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
