package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.create

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.result.onError
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.CreateUserUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.create.CreateByEmailRequest
import kotlinx.coroutines.launch

class CreateUserComponentImpl(
    componentContext: ComponentContext,
    private val createUserUseCase: CreateUserUseCase,
    private val onSuccess: () -> Unit,
    private val onBack: () -> Unit,
) : CreateUserComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()
    private val _state = MutableValue(CreateUserScreenState())
    override val state: Value<CreateUserScreenState> = _state

    override fun onEmailChanged(value: String) {
        _state.value = _state.value.copy(email = value, error = null)
    }

    override fun onPasswordChanged(value: String) {
        _state.value = _state.value.copy(password = value, error = null)
    }

    override fun onRoleChanged(value: String) {
        _state.value = _state.value.copy(role = value, error = null)
    }

    override fun onStatusChanged(value: String) {
        _state.value = _state.value.copy(status = value, error = null)
    }

    override fun onAuthorityLevelChanged(value: String) {
        _state.value = _state.value.copy(authorityLevel = value, error = null)
    }

    override fun onCreateClick() {
        val current = _state.value
        val authLevel = current.authorityLevel.toIntOrNull() ?: 0

        _state.value = current.copy(isLoading = true, error = null)
        scope.launch {
            createUserUseCase(
                CreateByEmailRequest(
                    email = current.email,
                    password = current.password,
                    role = current.role,
                    status = current.status,
                    authorityLevel = authLevel,
                    permissionCodes = emptySet(),
                )
            ).onSuccess {
                onSuccess()
            }.onError { error ->
                _state.value = current.copy(isLoading = false, error = error)
            }
        }
    }

    override fun onBackClick() {
        onBack()
    }
}
