package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId

/**
 * Administrative repository for managing user identity identifiers across all accounts.
 */
interface ManagementIdentifierRepository {

    /**
     * Returns a paginated and filtered list of identifiers based on search criteria.
     *
     * @param pageNumber One-based page index.
     * @param pageSize Maximum items returned per page.
     * @param sortBy Field to sort by (created_at, updated_at).
     * @param sortOrder Sorting direction (ASC, DESC).
     * @param userIds Filters by specific user identifiers.
     * @param userAuthProviders Filters by authentication provider types.
     * @param identifiers Filters by server-defined free-text identifier values.
     * @return Paginated result containing matching user identifier models, or a mapped failure.
     */
    suspend fun getIdentifiers(
        pageNumber: Int? = null,
        pageSize: Int? = null,
        sortBy: UserSortValues.UserIdentifierSortBy? = null,
        sortOrder: SortOrder? = null,
        userIds: List<String>? = null,
        userAuthProviders: List<UserAuthProvider>? = null,
        identifiers: List<String>? = null
    ): AppResult<PagedResult<UserIdentifierSummary>>

    /**
     * Retrieves specific identifier details.
     */
    suspend fun getIdentifier(identifierId: String): AppResult<UserIdentifierPrivate>

    /**
     * Removes the identifier record for the given user.
     */
    suspend fun deleteIdentifier(userId: UserId, identifierId: String): AppResult<Unit>

    /**
     * Removes the password credential for the given user's identifier record.
     */
    suspend fun deleteIdentifierPassword(userId: UserId, identifierId: String): AppResult<Unit>
}