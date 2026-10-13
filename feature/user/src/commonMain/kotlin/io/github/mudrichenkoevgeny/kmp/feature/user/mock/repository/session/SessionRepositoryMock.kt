package io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.session

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.domain.model.listing.pagedResultMock
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.session.userSessionPrivateMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.session.userSessionSummaryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.session.SessionRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionSummary

@InternalApi
open class SessionRepositoryMock : SessionRepository {

    var getSessionsResultProvider: () -> AppResult<PagedResult<UserSessionSummary>> = {
        AppResult.Success(pagedResultMock(listOf(userSessionSummaryMock())))
    }

    var getSessionResultProvider: (UserSessionId) -> AppResult<UserSessionPrivate> = {
        AppResult.Success(userSessionPrivateMock())
    }

    var logoutResultProvider: () -> AppResult<Unit> = { AppResult.Success(Unit) }

    var deleteSessionResultProvider: (UserSessionId) -> AppResult<Unit> = { _ -> AppResult.Success(Unit) }

    var deleteAllOtherSessionsResultProvider: () -> AppResult<Unit> = { AppResult.Success(Unit) }

    var reauthenticateSessionResultProvider: (String, String) -> AppResult<Unit> = { _, _ -> AppResult.Success(Unit) }

    var lastSessionId: UserSessionId? = null
    var lastMfaToken: String? = null
    var lastCode: String? = null

    override suspend fun getSessions(
        pageNumber: Int?,
        pageSize: Int?,
        sortBy: UserSortValues.UserSessionSortBy?,
        sortOrder: SortOrder?,
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
    ): AppResult<PagedResult<UserSessionSummary>> = getSessionsResultProvider()

    override suspend fun getSession(userSessionId: UserSessionId): AppResult<UserSessionPrivate> {
        lastSessionId = userSessionId
        return getSessionResultProvider(userSessionId)
    }

    override suspend fun logout(): AppResult<Unit> = logoutResultProvider()

    override suspend fun deleteSession(userSessionId: UserSessionId): AppResult<Unit> {
        lastSessionId = userSessionId
        return deleteSessionResultProvider(userSessionId)
    }

    override suspend fun deleteAllOtherSessions(): AppResult<Unit> = deleteAllOtherSessionsResultProvider()

    override suspend fun reauthenticateSession(mfaToken: String, code: String): AppResult<Unit> {
        lastMfaToken = mfaToken
        lastCode = code
        return reauthenticateSessionResultProvider(mfaToken, code)
    }
}