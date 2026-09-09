package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.sessions

import com.arkivanov.decompose.value.Value

interface UserSessionsComponent {
    val state: Value<UserSessionsScreenState>

    fun onRefresh()
    fun onLoadNextPage()
    fun onBackClick()
}
