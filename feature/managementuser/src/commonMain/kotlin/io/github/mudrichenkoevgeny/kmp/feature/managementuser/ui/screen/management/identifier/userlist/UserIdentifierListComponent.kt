package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.identifier.userlist

import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.list.IdentifierListOwner

/**
 * Manages a specific user's identifiers: listing and navigation to detail screen.
 */
interface UserIdentifierListComponent : IdentifierListOwner {
    /** Reactive UI state. */
    val state: Value<UserIdentifierListScreenState>

    /** Refreshes the identifier list. */
    fun onRefresh()
    
    /** Navigates to identifier detail screen. */
    fun onIdentifierClick(identifierId: String)
    
    /** Requests the next page of identifiers if available. */
    fun onLoadNextPage()
    
    /** Navigates back. */
    fun onBackClick()
}
