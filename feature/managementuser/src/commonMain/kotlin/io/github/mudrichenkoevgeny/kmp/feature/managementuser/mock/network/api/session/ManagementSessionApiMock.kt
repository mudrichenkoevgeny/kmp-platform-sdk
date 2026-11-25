package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.session

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.session.ManagementSessionApi
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.session.globallist.ManagementGlobalSessionListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.session.userlist.ManagementUserSessionListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.UserSessionPrivatePayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.UserSessionSummaryPayload

/**
 * Mock implementation of [ManagementSessionApi] for testing.
 */
@InternalApi
public class ManagementSessionApiMock : ManagementSessionApi {

    public var getSessionsResult: AppResult<PagedResult<UserSessionSummaryPayload>> = AppResult.Error(
        CommonError.Unknown()
    )
    public var getUserSessionsResult: AppResult<PagedResult<UserSessionSummaryPayload>> =
        AppResult.Error(CommonError.Unknown())
    public var getSessionResult: AppResult<UserSessionPrivatePayload> = AppResult.Error(CommonError.Unknown())
    public var deleteSessionResult: AppResult<Unit> = AppResult.Error(CommonError.Unknown())
    public var deleteAllUserSessionsResult: AppResult<Unit> = AppResult.Error(CommonError.Unknown())

    public override suspend fun getSessions(
        params: ManagementGlobalSessionListParams?
    ): AppResult<PagedResult<UserSessionSummaryPayload>> = getSessionsResult

    public override suspend fun getUserSessions(
        params: ManagementUserSessionListParams
    ): AppResult<PagedResult<UserSessionSummaryPayload>> = getUserSessionsResult

    public override suspend fun getSession(
        sessionId: UserSessionId
    ): AppResult<UserSessionPrivatePayload> = getSessionResult

    public override suspend fun deleteSession(
        userId: UserId,
        sessionId: UserSessionId
    ): AppResult<Unit> = deleteSessionResult

    public override suspend fun deleteAllUserSessions(userId: UserId): AppResult<Unit> = deleteAllUserSessionsResult
}
