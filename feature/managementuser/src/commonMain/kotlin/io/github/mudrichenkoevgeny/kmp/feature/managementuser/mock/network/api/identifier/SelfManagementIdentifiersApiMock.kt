package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.identifier.SelfManagementIdentifiersApi
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierPrivatePayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierSummaryPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.security.password.EmailPasswordChangeRequest

@InternalApi
class SelfManagementIdentifiersApiMock : SelfManagementIdentifiersApi {

    var getUserIdentifierResult: AppResult<UserIdentifierPrivatePayload> = AppResult.Error(CommonError.Unknown())
    var getUserIdentifiersResult: AppResult<PagedResult<UserIdentifierSummaryPayload>> = AppResult.Error(CommonError.Unknown())
    var emailChangePasswordResult: AppResult<Unit> = AppResult.Error(CommonError.Unknown())

    override suspend fun getUserIdentifier(userIdentifierId: UserIdentifierId): AppResult<UserIdentifierPrivatePayload> = getUserIdentifierResult

    override suspend fun getUserIdentifiers(
        pageNumber: Int?,
        pageSize: Int?,
        sortBy: UserSortValues.UserIdentifierSortBy?,
        sortOrder: SortOrder?,
        userAuthProviders: List<UserAuthProvider>?,
        identifiers: List<String>?
    ): AppResult<PagedResult<UserIdentifierSummaryPayload>> = getUserIdentifiersResult

    override suspend fun emailChangePassword(
        userIdentifierId: UserIdentifierId,
        request: EmailPasswordChangeRequest
    ): AppResult<Unit> = emailChangePasswordResult
}