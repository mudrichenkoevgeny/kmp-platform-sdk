package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.ListingConstants
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.identifier.IdentifierRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.GetUserIdentifiersUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues

@InternalApi
class GetUserIdentifiersUseCaseMock : GetUserIdentifiersUseCase(
    identifierRepository = IdentifierRepositoryMock()
) {
    var resultProvider: (page: Int, size: Int) -> AppResult<PagedResult<UserIdentifier>> = { page, size ->
        AppResult.Success(
            PagedResult(
                items = emptyList(),
                totalCount = 0,
                pageNumber = page,
                pageSize = size,
                totalPages = 0
            )
        )
    }
    var executeCalls = 0

    override suspend fun invoke(
        pageNumber: Int?,
        pageSize: Int?,
        sortBy: UserSortValues.UserIdentifierSortBy?,
        sortOrder: SortOrder?,
        userAuthProviders: List<UserAuthProvider>?,
        identifiers: List<String>?
    ): AppResult<PagedResult<UserIdentifier>> {
        executeCalls++
        return resultProvider(pageNumber ?: ListingConstants.INITIAL_PAGE_NUMBER, pageSize ?: 10)
    }
}
