package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp.main

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.result.onError
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.UserRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.DisableTotpUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.EnableTotpUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.SetupTotpUseCase
import kotlinx.coroutines.launch

/**
 * Default [TotpMainComponent]: implementation of TOTP lifecycle management (setup, verification, recovery).
 *
 * @param componentContext Decompose [ComponentContext].
 * @param userRepository Source of the current user profile state.
 * @param setupTotpUseCase Generates TOTP secret and setup URI.
 * @param enableTotpUseCase Verifies initial code and enables TOTP.
 * @param disableTotpUseCase Turns off TOTP for the account.
 * @param onNavigateToRecoveryCodes Opens the recovery codes screen.
 * @param onBack Pops this screen from the navigation stack.
 */
class TotpMainComponentImpl(
    componentContext: ComponentContext,
    private val userRepository: UserRepository,
    private val setupTotpUseCase: SetupTotpUseCase,
    private val enableTotpUseCase: EnableTotpUseCase,
    private val disableTotpUseCase: DisableTotpUseCase,
    private val onNavigateToRecoveryCodes: () -> Unit,
    val onBack: () -> Unit
) : TotpMainComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()
    private val _state = MutableValue<TotpMainScreenState>(TotpMainScreenState.Loading)
    override val state: Value<TotpMainScreenState> = _state

    init {
        scope.launch {
            userRepository.currentUser.collect { user ->
                if (user == null) {
                    _state.value = TotpMainScreenState.Error(CommonError.Unknown())
                } else if (user.isTotpEnabled) {
                    _state.value = TotpMainScreenState.Enabled()
                } else if (!user.isTotpEnabled && _state.value !is TotpMainScreenState.SetupInProgress) {
                    _state.value = TotpMainScreenState.Disabled()
                }
            }
        }
    }

    override fun onSetupClick() {
        val current = _state.value as? TotpMainScreenState.Disabled ?: return
        _state.value = current.copy(actionLoading = true, actionError = null)

        scope.launch {
            setupTotpUseCase()
                .onSuccess { setup ->
                    _state.value = TotpMainScreenState.SetupInProgress(setup = setup)
                }
                .onError { error ->
                    _state.value = current.copy(actionLoading = false, actionError = error)
                }
        }
    }

    override fun onCodeChanged(code: String) {
        val current = _state.value as? TotpMainScreenState.SetupInProgress ?: return
        _state.value = current.copy(code = code, actionError = null)
    }

    override fun onConfirmSetupClick() {
        val current = _state.value as? TotpMainScreenState.SetupInProgress ?: return
        if (!current.canConfirm) return

        _state.value = current.copy(actionLoading = true, actionError = null)

        scope.launch {
            enableTotpUseCase(current.setup.mfaToken, current.code)
                .onSuccess {
                    userRepository.refreshCurrentUser()
                    onNavigateToRecoveryCodes()
                }
                .onError { error ->
                    _state.value = current.copy(actionLoading = false, actionError = error)
                }
        }
    }

    override fun onRecoveryCodesClick() {
        onNavigateToRecoveryCodes()
    }

    override fun onDisableClick() {
        val current = _state.value as? TotpMainScreenState.Enabled ?: return
        _state.value = current.copy(showDisableConfirmation = true)
    }

    override fun onConfirmDisable() {
        val current = _state.value as? TotpMainScreenState.Enabled ?: return
        _state.value = current.copy(showDisableConfirmation = false, actionLoading = true, actionError = null)

        scope.launch {
            disableTotpUseCase()
                .onSuccess {
                    _state.value = TotpMainScreenState.Disabled()
                    userRepository.refreshCurrentUser()
                }
                .onError { error ->
                    val latest = _state.value as? TotpMainScreenState.Enabled ?: return@onError
                    _state.value = latest.copy(
                        showDisableConfirmation = false,
                        actionLoading = false,
                        actionError = error
                    )
                }
        }
    }

    override fun onDismissDialogs() {
        val current = _state.value as? TotpMainScreenState.Enabled ?: return
        _state.value = current.copy(showDisableConfirmation = false)
    }

    override fun onBackClick() {
        onBack()
    }
}
