package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.userlist

import com.arkivanov.decompose.value.Value

interface UserSessionListComponent {
    val state: Value<UserSessionListScreenState>

    fun onRefresh()
    fun onLoadNextPage()
    fun onBackClick()
    fun onDeleteSessionClick(sessionId: String)
    fun onDeleteAllSessionsClick()
}
