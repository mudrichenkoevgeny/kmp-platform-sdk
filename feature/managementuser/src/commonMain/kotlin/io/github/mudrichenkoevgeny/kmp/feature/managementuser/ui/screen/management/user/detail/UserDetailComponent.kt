package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.detail

import com.arkivanov.decompose.value.Value

interface UserDetailComponent {
    val state: Value<UserDetailScreenState>

    fun onAuthorityLevelChanged(value: String)
    fun onAccountStatusChanged(value: String)
    fun onLockoutTypeChanged(value: String)
    fun onTemporaryLockoutUntilChanged(value: String)
    fun onUpdateClick()
    fun onDeleteClick()
    fun onDisableTotpClick()
    fun onSessionsClick()
    fun onIdentifiersClick()
    fun onRetry()
    fun onBackClick()
}
