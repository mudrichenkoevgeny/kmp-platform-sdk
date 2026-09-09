package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.create

import com.arkivanov.decompose.value.Value

interface CreateUserComponent {
    val state: Value<CreateUserScreenState>

    fun onEmailChanged(value: String)
    fun onPasswordChanged(value: String)
    fun onRoleChanged(value: String)
    fun onStatusChanged(value: String)
    fun onAuthorityLevelChanged(value: String)
    fun onCreateClick()
    fun onBackClick()
}
