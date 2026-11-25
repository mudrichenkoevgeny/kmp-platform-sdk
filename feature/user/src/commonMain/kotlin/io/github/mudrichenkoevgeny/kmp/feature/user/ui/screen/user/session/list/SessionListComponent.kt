package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list

import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionSummary

/**
 * Universal Decompose component contract for active sessions listing screens (client & management).
 */
public interface SessionListComponent : SessionListOwner {

    /** Observable screen state flow. */
    public val state: Value<SessionListScreenState>

    /** Whether revoking a single session is available for this contour. */
    public val isRevokeAvailable: Boolean

    /** Whether revoking all other sessions / all user sessions is available for this contour. */
    public val isRevokeAllOthersAvailable: Boolean

    /** Whether filter panel features are included (administrative mode). */
    public val includeFilterPanel: Boolean

    /** Navigates back to previous screen. */
    public fun onBackClick()

    /** Refreshes current session list page. */
    public fun onRefresh()

    /** Triggers loading next page. */
    public fun onLoadNextPage()

    /** Toggles expansion state of filter options panel. */
    public fun onToggleFilterPanel()

    /** Called when active sort state changes. */
    public fun onSortChanged(sortState: ListingSortState)

    /** Called when an individual filter state changes. */
    public fun onFilterChanged(
        filterId: String,
        filterState: ListingFilterState?
    )

    /** Applies selected filters and re-fetches list. */
    public fun onApplyFilters()

    /** Navigates to detail view for the specified session. */
    public fun onSessionClick(session: UserSessionSummary)

    /** Revokes a specific session by ID. */
    public fun onRevokeSessionClick(sessionId: UserSessionId)

    /** Revokes all other sessions (or all user sessions). */
    public fun onRevokeAllOtherSessionsClick()
}
