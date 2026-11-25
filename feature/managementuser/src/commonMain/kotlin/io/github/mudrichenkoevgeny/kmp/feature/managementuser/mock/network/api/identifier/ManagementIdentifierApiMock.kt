package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.identifier.ManagementIdentifierApi
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.identifier.globallist.ManagementGlobalIdentifierListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.identifier.userlist.ManagementUserIdentifierListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierPrivatePayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierSummaryPayload

/**
 * Mock implementation of [ManagementIdentifierApi] for testing.
 */
@InternalApi
public class ManagementIdentifierApiMock : ManagementIdentifierApi {

    public var getIdentifiersResult: AppResult<PagedResult<UserIdentifierSummaryPayload>> =
        AppResult.Error(CommonError.Unknown())
    public var getUserIdentifiersResult: AppResult<PagedResult<UserIdentifierSummaryPayload>> =
        AppResult.Error(CommonError.Unknown())
    public var getIdentifierResult: AppResult<UserIdentifierPrivatePayload> = AppResult.Error(CommonError.Unknown())
    public var deleteIdentifierResult: AppResult<Unit> = AppResult.Success(Unit)
    public var deleteIdentifierPasswordResult: AppResult<Unit> = AppResult.Success(Unit)

    public override suspend fun getIdentifiers(
        params: ManagementGlobalIdentifierListParams?
    ): AppResult<PagedResult<UserIdentifierSummaryPayload>> = getIdentifiersResult

    public override suspend fun getUserIdentifiers(
        params: ManagementUserIdentifierListParams
    ): AppResult<PagedResult<UserIdentifierSummaryPayload>> = getUserIdentifiersResult

    public override suspend fun getIdentifier(identifierId: String): AppResult<UserIdentifierPrivatePayload> =
        getIdentifierResult

    public override suspend fun deleteIdentifier(
        userId: UserId,
        identifierId: String
    ): AppResult<Unit> = deleteIdentifierResult

    public override suspend fun deleteIdentifierPassword(
        userId: UserId,
        identifierId: String
    ): AppResult<Unit> = deleteIdentifierPasswordResult
}
