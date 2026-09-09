package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.identifiers

import com.arkivanov.decompose.value.Value

interface UserIdentifiersComponent {
    val state: Value<UserIdentifiersScreenState>

    fun onRefresh()
    fun onLoadNextPage()
    fun onBackClick()
}
