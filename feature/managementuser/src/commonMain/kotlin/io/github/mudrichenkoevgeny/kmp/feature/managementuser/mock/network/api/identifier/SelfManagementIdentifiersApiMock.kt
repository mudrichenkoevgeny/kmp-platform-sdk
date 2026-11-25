package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.identifier.SelfManagementIdentifiersApi
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.user.identifier.list.SelfIdentifierListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierPrivatePayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierSummaryPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.security.password.EmailPasswordChangeRequest

/**
 * Mock implementation of [SelfManagementIdentifiersApi] for testing.
 */
@InternalApi
public class SelfManagementIdentifiersApiMock : SelfManagementIdentifiersApi {

    public var getUserIdentifierResult: AppResult<UserIdentifierPrivatePayload> = AppResult.Error(CommonError.Unknown())
    public var getUserIdentifiersResult: AppResult<PagedResult<UserIdentifierSummaryPayload>> =
        AppResult.Error(CommonError.Unknown())
    public var emailChangePasswordResult: AppResult<Unit> = AppResult.Error(CommonError.Unknown())

    public override suspend fun getUserIdentifier(
        userIdentifierId: UserIdentifierId
    ): AppResult<UserIdentifierPrivatePayload> =
        getUserIdentifierResult

    public override suspend fun getUserIdentifiers(
        params: SelfIdentifierListParams?
    ): AppResult<PagedResult<UserIdentifierSummaryPayload>> = getUserIdentifiersResult

    public override suspend fun emailChangePassword(
        userIdentifierId: UserIdentifierId,
        request: EmailPasswordChangeRequest
    ): AppResult<Unit> = emailChangePasswordResult
}
