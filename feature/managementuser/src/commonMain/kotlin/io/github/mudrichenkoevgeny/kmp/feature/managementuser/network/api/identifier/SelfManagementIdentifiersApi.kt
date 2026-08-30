package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.security.password.EmailPasswordChangeRequest

/** Manage user identifiers (email, phone, external providers) and related confirmations. */
interface SelfManagementIdentifiersApi {
    /**
     * Retrieves specific identifier details by its unique id.
     *
     * @param userIdentifierId Unique identifier payload id.
     * @return Detailed identifier info or a mapped failure.
     */
    suspend fun getUserIdentifier(userIdentifierId: UserIdentifierId): AppResult<UserIdentifierPayload>

    /**
     * Returns a paginated and filtered list of identifiers linked to the current authenticated management account.
     *
     * @param pageNumber One-based page index.
     * @param pageSize Maximum items returned per page.
     * @param sortBy Field to sort by (created_at, updated_at).
     * @param sortOrder Sorting direction (ASC, DESC).
     * @param userAuthProviders Filters by specific provider types.
     * @param identifiers Filters by substring patterns of identifier values.
     * @return Paginated result containing matching user identifier payloads, or a mapped failure.
     */
    suspend fun getUserIdentifiers(
        pageNumber: Int?,
        pageSize: Int?,
        sortBy: UserSortValues.UserIdentifierSortBy?,
        sortOrder: SortOrder? = null,
        userAuthProviders: List<UserAuthProvider>?,
        identifiers: List<String>?
    ): AppResult<PagedResult<UserIdentifierPayload>>

    /**
     * Updates the management account password using current credentials.
     *
     * @param request Current and new password payload from the shared contract.
     * @return Empty success indicator, or a mapped failure.
     */
    suspend fun emailChangePassword(request: EmailPasswordChangeRequest): AppResult<Unit>
}