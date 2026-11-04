package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list

import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionSummary

/**
 * Universal Decompose component contract for active sessions listing screens (client & management).
 */
interface SessionListComponent : SessionListOwner {

    /** Observable screen state flow. */
    val state: Value<SessionListScreenState>

    /** Whether revoking a single session is available for this contour. */
    val isRevokeAvailable: Boolean

    /** Whether revoking all other sessions / all user sessions is available for this contour. */
    val isRevokeAllOthersAvailable: Boolean

    /** Whether filter panel features are included (administrative mode). */
    val includeFilterPanel: Boolean

    /** Navigates back to previous screen. */
    fun onBackClick()

    /** Refreshes current session list page. */
    fun onRefresh()

    /** Triggers loading next page. */
    fun onLoadNextPage()

    /** Toggles expansion state of filter options panel. */
    fun onToggleFilterPanel()

    /** Called when active sort state changes. */
    fun onSortChanged(sortState: ListingSortState)

    /** Called when an individual filter state changes. */
    fun onFilterChanged(filterId: String, filterState: ListingFilterState?)

    /** Applies selected filters and re-fetches list. */
    fun onApplyFilters()

    /** Navigates to detail view for the specified session. */
    fun onSessionClick(session: UserSessionSummary)

    /** Revokes a specific session by ID. */
    fun onRevokeSessionClick(sessionId: UserSessionId)

    /** Revokes all other sessions (or all user sessions). */
    fun onRevokeAllOtherSessionsClick()
}
