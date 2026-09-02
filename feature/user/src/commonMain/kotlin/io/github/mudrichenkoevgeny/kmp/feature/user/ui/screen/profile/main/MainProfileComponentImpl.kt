package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.main

import co.touchlab.kermit.Logger
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.asValue
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.result.onError
import io.github.mudrichenkoevgeny.kmp.feature.user.model.apptype.AppType
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.UserRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.LogoutUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.RestoreUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.ScheduleUserDeletionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

/**
 * Default [MainProfileComponent]: maps user flow to UI state and executes high-level profile actions.
 *
 * @param componentContext Decompose [ComponentContext].
 * @param appType Defines the application context (Client/Management) to toggle features.
 * @param userRepository Source of the current user profile state.
 * @param logoutUseCase Ends the current session and clears local storage.
 * @param scheduleUserDeletionUseCase Initiates account deletion for end-users.
 * @param restoreUserUseCase Restores an account scheduled for deletion.
 * @param onNavigateToLogin Invoked when the user needs to sign in.
 * @param onNavigateToTotp Opens the TOTP settings screen.
 * @param onNavigateToSessions Opens the active sessions list.
 * @param onNavigateToIdentifiers Opens the linked identifiers management.
 */
class MainProfileComponentImpl(
    componentContext: ComponentContext,
    appType: AppType,
    userRepository: UserRepository,
    private val logoutUseCase: LogoutUseCase,
    private val scheduleUserDeletionUseCase: ScheduleUserDeletionUseCase,
    private val restoreUserUseCase: RestoreUserUseCase,
    private val onNavigateToLogin: () -> Unit,
    private val onNavigateToTotp: () -> Unit,
    private val onNavigateToSessions: () -> Unit,
    private val onNavigateToIdentifiers: () -> Unit
) : MainProfileComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()
    private val showDeleteConfirmation = MutableStateFlow(false)
    private val showLogoutConfirmation = MutableStateFlow(false)
    private val actionState = MutableStateFlow<ActionState>(ActionState.Idle)

    override val state: Value<MainProfileScreenState> = combine(
        userRepository.currentUser,
        showDeleteConfirmation,
        showLogoutConfirmation,
        actionState
    ) { user, showDeleteConfirm, showLogoutConfirm, action ->
        if (user == null) {
            MainProfileScreenState.Unauthorized
        } else {
            MainProfileScreenState.Content(
                user = user,
                isAccountDeletionAvailable = appType == AppType.CLIENT,
                showDeleteConfirmation = showDeleteConfirm,
                showLogoutConfirmation = showLogoutConfirm,
                actionLoading = action is ActionState.Loading,
                actionError = (action as? ActionState.Error)?.error
            )
        }
    }.catch { error ->
        Logger.i { "error received ${error.message}" }
        emit(MainProfileScreenState.Error(CommonError.Unknown()))
    }.asValue(
        initialValue = MainProfileScreenState.Loading,
        lifecycle = lifecycle
    )

    private sealed interface ActionState {
        data object Idle : ActionState
        data object Loading : ActionState
        data class Error(val error: AppError) : ActionState
    }

    override fun onLoginClick() {
        onNavigateToLogin()
    }

    override fun onLogoutClick() {
        showLogoutConfirmation.value = true
    }

    override fun onConfirmLogout() {
        showLogoutConfirmation.value = false
        actionState.value = ActionState.Loading

        scope.launch {
            logoutUseCase()
        }
    }

    override fun onTotpSettingsClick() {
        onNavigateToTotp()
    }

    override fun onSessionsClick() {
        onNavigateToSessions()
    }

    override fun onIdentifiersClick() {
        onNavigateToIdentifiers()
    }

    override fun onDeleteAccountClick() {
        showDeleteConfirmation.value = true
    }

    override fun onConfirmDeleteAccount() {
        showDeleteConfirmation.value = false
        actionState.value = ActionState.Loading

        scope.launch {
            scheduleUserDeletionUseCase()
                .onError { error ->
                    actionState.value = ActionState.Error(error)
                }
        }
    }

    override fun onRestoreAccountClick() {
        actionState.value = ActionState.Loading

        scope.launch {
            restoreUserUseCase()
                .onError { error ->
                    actionState.value = ActionState.Error(error)
                }
        }
    }

    override fun onDismissDialog() {
        showDeleteConfirmation.value = false
        showLogoutConfirmation.value = false
    }
}
