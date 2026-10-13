package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.identifier.IdentifierRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues

/**
 * Retrieves a paginated list of user identifiers linked to the current account.
 *
 * @param identifierRepository Identifiers repository.
 */
open class GetUserIdentifiersUseCase(
    private val identifierRepository: IdentifierRepository
) {
    /**
     * @param pageNumber One-based page index.
     * @param pageSize Maximum items returned per page.
     * @param sortBy Field to sort by.
     * @param sortOrder Sorting direction.
     * @param userAuthProviders Filters identifiers by auth provider type.
     * @param identifiers Filters identifiers by value.
     * @return Paginated result containing [UserIdentifierSummary] items on success, or a mapped failure.
     */
    open suspend operator fun invoke(
        pageNumber: Int? = null,
        pageSize: Int? = null,
        sortBy: UserSortValues.UserIdentifierSortBy? = null,
        sortOrder: SortOrder? = null,
        userAuthProviders: List<UserAuthProvider>? = null,
        identifiers: List<String>? = null
    ): AppResult<PagedResult<UserIdentifierSummary>> {
        return identifierRepository.getUserIdentifiers(
            pageNumber = pageNumber,
            pageSize = pageSize,
            sortBy = sortBy,
            sortOrder = sortOrder,
            userAuthProviders = userAuthProviders,
            identifiers = identifiers
        )
    }
}