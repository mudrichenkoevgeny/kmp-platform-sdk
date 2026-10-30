package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.main

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.result.onError
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.UserRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.LogoutUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.ScheduleUserDeletionUseCase
import kotlinx.coroutines.launch

/**
 * Default implementation of [MainUserComponent].
 */
class MainUserComponentImpl(
    componentContext: ComponentContext,
    private val userRepository: UserRepository,
    private val logoutUseCase: LogoutUseCase,
    private val scheduleUserDeletionUseCase: ScheduleUserDeletionUseCase,
    private val onNavigateToLogin: () -> Unit,
    private val onNavigateToTotp: () -> Unit,
    private val onNavigateToSessions: () -> Unit,
    private val onNavigateToIdentifiers: () -> Unit,
    private val onNavigateToFilesList: () -> Unit = {}
) : MainUserComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()
    private val _state = MutableValue<MainUserScreenState>(MainUserScreenState.Loading)
    override val state: Value<MainUserScreenState> = _state

    init {
        observeUser()
    }

    override fun onRetry() {
        observeUser()
    }

    override fun onTotpMainClick() {
        onNavigateToTotp()
    }

    override fun onSessionsClick() {
        onNavigateToSessions()
    }

    override fun onIdentifiersClick() {
        onNavigateToIdentifiers()
    }

    override fun onFilesListClick() {
        onNavigateToFilesList()
    }

    override fun onDeleteUserClick() {
        val current = _state.value as? MainUserScreenState.Content ?: return
        _state.value = current.copy(isDeleteConfirmationVisible = true, actionError = null)
    }

    override fun onConfirmDeleteUserClick() {
        val current = _state.value as? MainUserScreenState.Content ?: return
        _state.value = current.copy(actionLoading = true, actionError = null)

        scope.launch {
            scheduleUserDeletionUseCase()
                .onSuccess {
                    _state.value = current.copy(
                        isDeleteConfirmationVisible = false,
                        actionLoading = false
                    )
                }
                .onError { error ->
                    _state.value = current.copy(
                        actionLoading = false,
                        actionError = error
                    )
                }
        }
    }

    override fun onDismissDeleteUserDialog() {
        val current = _state.value as? MainUserScreenState.Content ?: return
        _state.value = current.copy(isDeleteConfirmationVisible = false)
    }

    override fun onLogoutClick() {
        val current = _state.value as? MainUserScreenState.Content ?: return
        _state.value = current.copy(isLogoutConfirmationVisible = true, actionError = null)
    }

    override fun onConfirmLogoutClick() {
        val current = _state.value as? MainUserScreenState.Content ?: return
        _state.value = current.copy(isLogoutConfirmationVisible = false)
        scope.launch {
            logoutUseCase()
        }
    }

    override fun onDismissLogoutDialog() {
        val current = _state.value as? MainUserScreenState.Content ?: return
        _state.value = current.copy(isLogoutConfirmationVisible = false)
    }

    override fun onLoginClick() {
        onNavigateToLogin()
    }

    private fun observeUser() {
        scope.launch {
            userRepository.currentUser.collect { user ->
                if (user != null) {
                    _state.value = MainUserScreenState.Content(user = user)
                } else {
                    _state.value = MainUserScreenState.Unauthorized()
                }
            }
        }
    }
}
