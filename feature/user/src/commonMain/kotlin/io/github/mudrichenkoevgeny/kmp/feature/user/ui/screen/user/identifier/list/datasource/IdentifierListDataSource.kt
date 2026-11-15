package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.list.datasource

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierSummary

/**
 * Data source abstraction providing identifier fetch capabilities for identifier list UI screens.
 */
public interface IdentifierListDataSource {

    /** Whether adding a new identifier is available in this data source contour. */
    public val isAddIdentifierSupported: Boolean

    /**
     * Fetches a paginated list of user identifiers according to [query].
     *
     * @param query Filtering, sorting, and pagination parameters.
     * @return [AppResult] containing a [PagedResult] of [UserIdentifierSummary].
     */
    public suspend fun getIdentifiers(query: IdentifierListQuery): AppResult<PagedResult<UserIdentifierSummary>>
}
