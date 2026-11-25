package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.session

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.session.SessionApi
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.verifytotp.VerifyTotpPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.user.session.list.SelfSessionListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.DeletedSessionsPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.UserSessionPrivatePayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.UserSessionSummaryPayload

/**
 * Mock implementation of [SessionApi] for testing.
 */
@InternalApi
public class SelfManagementSessionApiMock : SessionApi {

    public var getSessionsResult: AppResult<PagedResult<UserSessionSummaryPayload>> = AppResult.Error(
        CommonError.Unknown()
    )
    public var getSessionResult: AppResult<UserSessionPrivatePayload> = AppResult.Error(CommonError.Unknown())
    public var logoutResult: AppResult<Unit> = AppResult.Error(CommonError.Unknown())
    public var deleteSessionResult: AppResult<Unit> = AppResult.Error(CommonError.Unknown())
    public var deleteAllOtherSessionsResult: AppResult<DeletedSessionsPayload> =
        AppResult.Error(CommonError.Unknown())
    public var reauthenticateSessionResult: AppResult<Unit> = AppResult.Error(CommonError.Unknown())

    public override suspend fun getSessions(
        params: SelfSessionListParams?
    ): AppResult<PagedResult<UserSessionSummaryPayload>> = getSessionsResult

    public override suspend fun getSession(userSessionId: UserSessionId): AppResult<UserSessionPrivatePayload> =
        getSessionResult

    public override suspend fun logout(): AppResult<Unit> = logoutResult

    public override suspend fun deleteSession(userSessionId: UserSessionId): AppResult<Unit> = deleteSessionResult

    public override suspend fun deleteAllOtherSessions(): AppResult<DeletedSessionsPayload> =
        deleteAllOtherSessionsResult

    public override suspend fun reauthenticateSession(request: VerifyTotpPayload): AppResult<Unit> =
        reauthenticateSessionResult
}
