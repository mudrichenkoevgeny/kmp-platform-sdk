package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.identifier.ManagementIdentifierApi
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierPayload

@InternalApi
class ManagementIdentifierApiMock : ManagementIdentifierApi {

    var getIdentifiersResult: AppResult<PagedResult<UserIdentifierPayload>> = AppResult.Error(CommonError.Unknown())
    var getIdentifierResult: AppResult<UserIdentifierPayload> = AppResult.Error(CommonError.Unknown())
    var deleteIdentifierResult: AppResult<Unit> = AppResult.Error(CommonError.Unknown())

    override suspend fun getIdentifiers(
        pageNumber: Int?,
        pageSize: Int?,
        sortBy: UserSortValues.UserIdentifierSortBy?,
        sortOrder: SortOrder?,
        userIds: List<String>?,
        userAuthProviders: List<UserAuthProvider>?,
        identifiers: List<String>?
    ): AppResult<PagedResult<UserIdentifierPayload>> = getIdentifiersResult

    override suspend fun getIdentifier(identifierId: String): AppResult<UserIdentifierPayload> = getIdentifierResult

    override suspend fun deleteIdentifier(userId: UserId, identifierId: String): AppResult<Unit> = deleteIdentifierResult
}