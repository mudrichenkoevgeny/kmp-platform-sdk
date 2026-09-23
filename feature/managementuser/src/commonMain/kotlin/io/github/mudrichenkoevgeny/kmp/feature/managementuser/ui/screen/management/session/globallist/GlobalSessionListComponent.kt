package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.globallist

import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.list.SessionListOwner
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId

interface GlobalSessionListComponent : SessionListOwner {
    val state: Value<GlobalSessionListScreenState>

    fun onRefresh()
    fun onLoadNextPage()
    fun onBackClick()
    fun onToggleFilterPanel()
    fun onSortChanged(sortState: ListingSortState)
    fun onFilterChanged(filterId: String, filterState: ListingFilterState?)
    fun onApplyFilters()
    fun onSessionClick(session: UserSession)
    fun onDeleteSessionClick(userId: UserId, sessionId: String)
}
