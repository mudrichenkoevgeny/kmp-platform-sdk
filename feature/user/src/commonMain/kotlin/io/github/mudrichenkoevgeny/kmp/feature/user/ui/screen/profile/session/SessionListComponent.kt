package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session

import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId

/**
 * Manages the active sessions lifecycle: listing, revoking specific sessions, and revoking all other sessions.
 */
interface SessionListComponent {
    /** Reactive UI state. */
    val state: Value<SessionListScreenState>

    /** Refreshes the session list. */
    fun onRefresh()

    /** Revokes a specific session. */
    fun onRevokeSessionClick(sessionId: UserSessionId)

    /** Revokes all sessions except the current one. */
    fun onRevokeAllOtherSessionsClick()

    /** Requests the next page of sessions if available. */
    fun onLoadNextPage()

    /** Navigates back. */
    fun onBackClick()

    /** Toggles the filter/sort panel visibility. */
    fun onToggleFilterPanel()

    /** Updates current sort state and reloads sessions. */
    fun onSortChanged(sortState: ListingSortState)

    /** Updates current filter state and reloads sessions. */
    fun onFilterChanged(filterId: String, filterState: ListingFilterState?)

    /** Applies current filters and reloads events. */
    fun onApplyFilters()
}
