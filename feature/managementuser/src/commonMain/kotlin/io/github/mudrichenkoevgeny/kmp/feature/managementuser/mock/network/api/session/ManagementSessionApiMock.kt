package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.session

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.session.ManagementSessionApi
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.UserSessionPayload

@InternalApi
class ManagementSessionApiMock : ManagementSessionApi {

    var getSessionsResult: AppResult<PagedResult<UserSessionPayload>> = AppResult.Error(CommonError.Unknown())
    var getSessionResult: AppResult<UserSessionPayload> = AppResult.Error(CommonError.Unknown())
    var deleteSessionResult: AppResult<Unit> = AppResult.Error(CommonError.Unknown())
    var deleteAllUserSessionsResult: AppResult<Unit> = AppResult.Error(CommonError.Unknown())

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
    ): AppResult<PagedResult<UserSessionPayload>> = getSessionsResult

    override suspend fun getSession(sessionId: String): AppResult<UserSessionPayload> = getSessionResult

    override suspend fun deleteSession(userId: UserId, sessionId: String): AppResult<Unit> = deleteSessionResult

    override suspend fun deleteAllUserSessions(userId: UserId): AppResult<Unit> = deleteAllUserSessionsResult
}