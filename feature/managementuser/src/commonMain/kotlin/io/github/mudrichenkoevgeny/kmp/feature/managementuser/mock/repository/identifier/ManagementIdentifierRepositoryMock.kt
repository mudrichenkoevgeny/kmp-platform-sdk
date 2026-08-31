package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.identifier.ManagementIdentifierRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.identifier.userIdentifierMock
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId

@InternalApi
open class ManagementIdentifierRepositoryMock : ManagementIdentifierRepository {

    var getIdentifiersResultProvider: () -> AppResult<PagedResult<UserIdentifier>> = {
        AppResult.Success(
            io.github.mudrichenkoevgeny.kmp.core.common.mock.domain.model.listing.pagedResultMock(
                listOf(userIdentifierMock())
            )
        )
    }

    var getIdentifierResultProvider: (String) -> AppResult<UserIdentifier> = {
        AppResult.Success(userIdentifierMock())
    }

    var deleteIdentifierResultProvider: (UserId, String) -> AppResult<Unit> = { _, _ -> AppResult.Success(Unit) }

    var lastUserId: UserId? = null
    var lastIdentifierId: String? = null

    override suspend fun getIdentifiers(
        pageNumber: Int?,
        pageSize: Int?,
        sortBy: UserSortValues.UserIdentifierSortBy?,
        sortOrder: SortOrder?,
        userIds: List<String>?,
        userAuthProviders: List<UserAuthProvider>?,
        identifiers: List<String>?
    ): AppResult<PagedResult<UserIdentifier>> = getIdentifiersResultProvider()

    override suspend fun getIdentifier(identifierId: String): AppResult<UserIdentifier> {
        lastIdentifierId = identifierId
        return getIdentifierResultProvider(identifierId)
    }

    override suspend fun deleteIdentifier(userId: UserId, identifierId: String): AppResult<Unit> {
        lastUserId = userId
        lastIdentifierId = identifierId
        return deleteIdentifierResultProvider(userId, identifierId)
    }
}
