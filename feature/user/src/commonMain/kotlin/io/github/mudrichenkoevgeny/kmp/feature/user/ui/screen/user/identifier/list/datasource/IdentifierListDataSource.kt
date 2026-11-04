package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.list.datasource

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierSummary

/**
 * Data source abstraction providing identifier fetch capabilities for identifier list UI screens.
 */
interface IdentifierListDataSource {

    /** Whether adding a new identifier is available in this data source contour. */
    val isAddIdentifierSupported: Boolean

    /**
     * Fetches a paginated list of user identifiers according to [query].
     *
     * @param query Filtering, sorting, and pagination parameters.
     * @return [AppResult] containing a [PagedResult] of [UserIdentifierSummary].
     */
    suspend fun getIdentifiers(query: IdentifierListQuery): AppResult<PagedResult<UserIdentifierSummary>>
}
