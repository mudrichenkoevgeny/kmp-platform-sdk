package io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.api.session

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.session.SessionApi
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.verifytotp.VerifyTotpPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.DeletedSessionsPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.UserSessionPayload

@InternalApi
open class SessionApiMock : SessionApi {

    var getSessionsResult: AppResult<PagedResult<UserSessionPayload>> = AppResult.Success(PagedResult(emptyList(), 0, 1, 20, 0))
    var getSessionResult: AppResult<UserSessionPayload> = AppResult.Error(CommonError.Unknown())
    var logoutResult: AppResult<Unit> = AppResult.Success(Unit)
    var deleteSessionResult: AppResult<Unit> = AppResult.Success(Unit)
    var deleteAllOtherSessionsResult: AppResult<DeletedSessionsPayload> = AppResult.Success(DeletedSessionsPayload(emptyList()))
    var reauthenticateSessionResult: AppResult<Unit> = AppResult.Success(Unit)

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
    ): AppResult<PagedResult<UserSessionPayload>> = getSessionsResult

    override suspend fun getSession(userSessionId: UserSessionId): AppResult<UserSessionPayload> = getSessionResult

    override suspend fun logout(): AppResult<Unit> = logoutResult

    override suspend fun deleteSession(userSessionId: UserSessionId): AppResult<Unit> = deleteSessionResult

    override suspend fun deleteAllOtherSessions(): AppResult<DeletedSessionsPayload> = deleteAllOtherSessionsResult

    override suspend fun reauthenticateSession(request: VerifyTotpPayload): AppResult<Unit> = reauthenticateSessionResult
}
