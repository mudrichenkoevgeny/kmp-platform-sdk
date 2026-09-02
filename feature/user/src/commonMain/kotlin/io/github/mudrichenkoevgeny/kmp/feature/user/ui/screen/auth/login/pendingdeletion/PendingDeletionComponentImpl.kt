package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.pendingdeletion

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.result.onError
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.LogoutUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.RestoreUserUseCase
import kotlinx.coroutines.launch

/**
 * Default [PendingDeletionComponent]: allows restoring an account or signing out when login encounters a pending deletion status.
 *
 * @param componentContext Decompose [ComponentContext].
 * @param restoreUserUseCase Restores the account from pending deletion status.
 * @param logoutUseCase Ends the session and returns to login welcome.
 * @param onRestoreSuccess Invoked when account restoration succeeds so the host app can proceed.
 * @param onSignOut Invoked when the user chooses to sign out and return to the welcome screen.
 */
class PendingDeletionComponentImpl(
    componentContext: ComponentContext,
    private val restoreUserUseCase: RestoreUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val onRestoreSuccess: () -> Unit,
    private val onSignOut: () -> Unit
) : PendingDeletionComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()
    private val _state = MutableValue(PendingDeletionScreenState())
    override val state: Value<PendingDeletionScreenState> = _state

    override fun onRestoreAccountClick() {
        val current = _state.value
        if (current.actionLoading) return
        _state.value = current.copy(actionLoading = true, actionError = null)

        scope.launch {
            restoreUserUseCase()
                .onSuccess {
                    onRestoreSuccess()
                }
                .onError { error ->
                    _state.value = current.copy(actionLoading = false, actionError = error)
                }
        }
    }

    override fun onSignOutClick() {
        val current = _state.value
        if (current.actionLoading) return
        _state.value = current.copy(actionLoading = true, actionError = null)

        scope.launch {
            logoutUseCase()
            onSignOut()
        }
    }
}
