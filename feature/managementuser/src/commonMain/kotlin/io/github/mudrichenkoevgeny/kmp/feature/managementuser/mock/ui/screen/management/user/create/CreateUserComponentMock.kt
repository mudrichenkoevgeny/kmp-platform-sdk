package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.user.create

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.create.CreateUserComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.create.CreateUserScreenState

@InternalApi
class CreateUserComponentMock(
    initialState: CreateUserScreenState = CreateUserScreenState(),
) : CreateUserComponent {

    private val _state = MutableValue(initialState)
    override val state: Value<CreateUserScreenState> = _state

    var createCalls = 0
    var backCalls = 0
    var lastEmailChanged: String? = null
    var lastPasswordChanged: String? = null
    var lastRoleChanged: String? = null
    var lastStatusChanged: String? = null
    var lastAuthorityLevelChanged: String? = null

    fun updateState(state: CreateUserScreenState) {
        _state.value = state
    }

    override fun onEmailChanged(value: String) {
        lastEmailChanged = value
    }

    override fun onPasswordChanged(value: String) {
        lastPasswordChanged = value
    }

    override fun onRoleChanged(value: String) {
        lastRoleChanged = value
    }

    override fun onStatusChanged(value: String) {
        lastStatusChanged = value
    }

    override fun onAuthorityLevelChanged(value: String) {
        lastAuthorityLevelChanged = value
    }

    override fun onCreateClick() {
        createCalls++
    }

    override fun onBackClick() {
        backCalls++
    }
}
