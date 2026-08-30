package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierPayload

/** Administrative user identifiers management. */
interface ManagementIdentifierApi {

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
     * @return Paginated result containing matching identifier payloads, or a mapped failure.
     */
    suspend fun getIdentifiers(
        pageNumber: Int? = null,
        pageSize: Int? = null,
        sortBy: UserSortValues.UserIdentifierSortBy? = null,
        sortOrder: SortOrder? = null,
        userIds: List<String>? = null,
        userAuthProviders: List<UserAuthProvider>? = null,
        identifiers: List<String>? = null
    ): AppResult<PagedResult<UserIdentifierPayload>>

    /**
     * Retrieves specific identifier details.
     *
     * @param identifierId Unique identifier record ID.
     * @return Detailed information of the target identifier, or a mapped failure.
     */
    suspend fun getIdentifier(identifierId: String): AppResult<UserIdentifierPayload>

    /**
     * Removes the identifier record for the given user.
     *
     * @param userId Unique identifier of the record owner.
     * @param identifierId Unique identifier record ID to delete.
     * @return Empty success indicator, or a mapped failure.
     */
    suspend fun deleteIdentifier(userId: UserId, identifierId: String): AppResult<Unit>
}