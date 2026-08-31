package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.identifier.ManagementIdentifierRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues

/**
 * Administrative retrieval of user identity identifiers based on filters.
 *
 * @param managementIdentifierRepository Administrative identifier management API.
 */
class ManagementGetIdentifiersUseCase(
    private val managementIdentifierRepository: ManagementIdentifierRepository
) {
    /**
     * @param pageNumber One-based page index.
     * @param pageSize Maximum items returned per page.
     * @param sortBy Field to sort by.
     * @param sortOrder Sorting direction.
     * @param userIds Filters by specific user identifiers.
     * @param userAuthProviders Filters by authentication provider types.
     * @param identifiers Filters by substring patterns of identifier values.
     * @return Paginated result containing matching user identifier models, or a mapped failure.
     */
    suspend operator fun invoke(
        pageNumber: Int? = null,
        pageSize: Int? = null,
        sortBy: UserSortValues.UserIdentifierSortBy? = null,
        sortOrder: SortOrder? = null,
        userIds: List<String>? = null,
        userAuthProviders: List<UserAuthProvider>? = null,
        identifiers: List<String>? = null
    ): AppResult<PagedResult<UserIdentifier>> {
        return managementIdentifierRepository.getIdentifiers(
            pageNumber = pageNumber,
            pageSize = pageSize,
            sortBy = sortBy,
            sortOrder = sortOrder,
            userIds = userIds,
            userAuthProviders = userAuthProviders,
            identifiers = identifiers
        )
    }
}
