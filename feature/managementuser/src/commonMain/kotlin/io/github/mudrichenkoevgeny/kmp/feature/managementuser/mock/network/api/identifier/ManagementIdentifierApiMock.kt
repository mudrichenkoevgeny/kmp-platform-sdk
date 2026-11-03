package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.identifier.ManagementIdentifierApi
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.identifier.globallist.ManagementGlobalIdentifierListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.identifier.userlist.ManagementUserIdentifierListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierPrivatePayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierSummaryPayload

@InternalApi
class ManagementIdentifierApiMock : ManagementIdentifierApi {

    var getIdentifiersResult: AppResult<PagedResult<UserIdentifierSummaryPayload>> = AppResult.Error(CommonError.Unknown())
    var getUserIdentifiersResult: AppResult<PagedResult<UserIdentifierSummaryPayload>> = AppResult.Error(CommonError.Unknown())
    var getIdentifierResult: AppResult<UserIdentifierPrivatePayload> = AppResult.Error(CommonError.Unknown())
    var deleteIdentifierResult: AppResult<Unit> = AppResult.Success(Unit)
    var deleteIdentifierPasswordResult: AppResult<Unit> = AppResult.Success(Unit)

    override suspend fun getIdentifiers(
        params: ManagementGlobalIdentifierListParams?
    ): AppResult<PagedResult<UserIdentifierSummaryPayload>> = getIdentifiersResult

    override suspend fun getUserIdentifiers(
        params: ManagementUserIdentifierListParams
    ): AppResult<PagedResult<UserIdentifierSummaryPayload>> = getUserIdentifiersResult

    override suspend fun getIdentifier(identifierId: String): AppResult<UserIdentifierPrivatePayload> = getIdentifierResult

    override suspend fun deleteIdentifier(userId: UserId, identifierId: String): AppResult<Unit> = deleteIdentifierResult

    override suspend fun deleteIdentifierPassword(userId: UserId, identifierId: String): AppResult<Unit> = deleteIdentifierPasswordResult
}
