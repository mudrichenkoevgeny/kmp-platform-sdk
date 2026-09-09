package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.detail

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.result.onError
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.DeleteUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.GetUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.UpdateUserUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.user.UpdateUserRequest
import kotlinx.coroutines.launch

class UserDetailComponentImpl(
    componentContext: ComponentContext,
    private val userId: UserId,
    private val getUserUseCase: GetUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val onNavigateToSessions: (UserId) -> Unit,
    private val onNavigateToIdentifiers: (UserId) -> Unit,
    private val onBack: () -> Unit
) : UserDetailComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()
    private val _state = MutableValue<UserDetailScreenState>(UserDetailScreenState.Loading)
    override val state: Value<UserDetailScreenState> = _state

    init {
        loadUser()
    }

    override fun onAuthorityLevelChanged(value: String) {
        val current = _state.value as? UserDetailScreenState.Content ?: return
        _state.value = current.copy(authorityLevelInput = value, saveError = null)
    }

    override fun onAccountStatusChanged(value: String) {
        val current = _state.value as? UserDetailScreenState.Content ?: return
        _state.value = current.copy(accountStatusInput = value, saveError = null)
    }

    override fun onUpdateClick() {
        val current = _state.value as? UserDetailScreenState.Content ?: return
        val authLevel = current.authorityLevelInput.toIntOrNull() ?: current.user.authorityLevel
        val status = current.accountStatusInput.takeIf { it.isNotBlank() } ?: current.user.accountStatus.name

        _state.value = current.copy(isSaving = true, saveError = null)

        scope.launch {
            updateUserUseCase(
                userId = userId,
                request = UpdateUserRequest(
                    accountStatus = status,
                    authorityLevel = authLevel,
                    permissionCodes = null
                )
            ).onSuccess {
                loadUser()
            }.onError { error ->
                _state.value = current.copy(isSaving = false, saveError = error)
            }
        }
    }

    override fun onDeleteClick() {
        val current = _state.value as? UserDetailScreenState.Content ?: return
        _state.value = current.copy(isDeleting = true, deleteError = null)

        scope.launch {
            deleteUserUseCase(userId)
                .onSuccess {
                    onBack()
                }
                .onError { error ->
                    _state.value = current.copy(isDeleting = false, deleteError = error)
                }
        }
    }

    override fun onSessionsClick() {
        onNavigateToSessions(userId)
    }

    override fun onIdentifiersClick() {
        onNavigateToIdentifiers(userId)
    }

    override fun onRetry() {
        loadUser()
    }

    override fun onBackClick() {
        onBack()
    }

    private fun loadUser() {
        _state.value = UserDetailScreenState.Loading
        scope.launch {
            getUserUseCase(userId)
                .onSuccess { user ->
                    _state.value = UserDetailScreenState.Content(
                        user = user,
                        authorityLevelInput = user.authorityLevel.toString(),
                        accountStatusInput = user.accountStatus.name
                    )
                }
                .onError { error ->
                    _state.value = UserDetailScreenState.Error(error)
                }
        }
    }
}
