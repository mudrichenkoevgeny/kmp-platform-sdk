package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.main

import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId

interface UsersManagementMainComponent {
    val state: Value<UsersManagementMainScreenState>

    fun onRefresh()
    fun onUserClick(userId: UserId)
    fun onCreateUserClick()
    fun onLoadNextPage()
    fun onBackClick()
}
