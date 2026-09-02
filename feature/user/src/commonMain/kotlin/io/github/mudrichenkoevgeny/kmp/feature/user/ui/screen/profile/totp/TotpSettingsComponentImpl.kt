package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp

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
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.GetRecoveryCodesUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.RegenerateRecoveryCodesUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.SetupTotpUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Default [TotpSettingsComponent]: implementation of TOTP lifecycle management (setup, verification, recovery).
 *
 * @param componentContext Decompose [ComponentContext].
 * @param userRepository Source of the current user profile state.
 * @param setupTotpUseCase Generates TOTP secret and setup URI.
 * @param enableTotpUseCase Verifies initial code and enables TOTP.
 * @param disableTotpUseCase Turns off TOTP for the account.
 * @param getRecoveryCodesUseCase Retrieves active backup recovery codes.
 * @param regenerateRecoveryCodesUseCase Generates a fresh set of backup codes.
 * @param onBack Pops this screen from the navigation stack.
 */
class TotpSettingsComponentImpl(
    componentContext: ComponentContext,
    private val userRepository: UserRepository,
    private val setupTotpUseCase: SetupTotpUseCase,
    private val enableTotpUseCase: EnableTotpUseCase,
    private val disableTotpUseCase: DisableTotpUseCase,
    private val getRecoveryCodesUseCase: GetRecoveryCodesUseCase,
    private val regenerateRecoveryCodesUseCase: RegenerateRecoveryCodesUseCase,
    val onBack: () -> Unit
) : TotpSettingsComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()
    private val _state = MutableValue<TotpSettingsScreenState>(TotpSettingsScreenState.Loading)
    override val state: Value<TotpSettingsScreenState> = _state

    init {
        scope.launch {
            val user = userRepository.currentUser.first()
            if (user == null) {
                _state.value = TotpSettingsScreenState.Error(CommonError.Unknown())
                return@launch
            }

            if (user.isTotpEnabled) {
                loadRecoveryCodes()
            } else {
                _state.value = TotpSettingsScreenState.Disabled()
            }
        }
    }

    override fun onSetupClick() {
        val current = _state.value as? TotpSettingsScreenState.Disabled ?: return
        _state.value = current.copy(actionLoading = true, actionError = null)

        scope.launch {
            setupTotpUseCase()
                .onSuccess { setup ->
                    _state.value = TotpSettingsScreenState.SetupInProgress(setup = setup)
                }
                .onError { error ->
                    _state.value = current.copy(actionLoading = false, actionError = error)
                }
        }
    }

    override fun onCodeChanged(code: String) {
        val current = _state.value as? TotpSettingsScreenState.SetupInProgress ?: return
        _state.value = current.copy(code = code, actionError = null)
    }

    override fun onConfirmSetupClick() {
        val current = _state.value as? TotpSettingsScreenState.SetupInProgress ?: return
        if (!current.canConfirm) return

        _state.value = current.copy(actionLoading = true, actionError = null)

        scope.launch {
            enableTotpUseCase(current.setup.mfaToken, current.code)
                .onSuccess { recoveryCodes ->
                    _state.value = TotpSettingsScreenState.Enabled(recoveryCodes = recoveryCodes)
                    userRepository.refreshCurrentUser()
                }
                .onError { error ->
                    _state.value = current.copy(actionLoading = false, actionError = error)
                }
        }
    }

    override fun onDisableClick() {
        val current = _state.value as? TotpSettingsScreenState.Enabled ?: return
        _state.value = current.copy(showDisableConfirmation = true)
    }

    override fun onConfirmDisable() {
        val current = _state.value as? TotpSettingsScreenState.Enabled ?: return
        _state.value = current.copy(showDisableConfirmation = false, actionLoading = true, actionError = null)

        scope.launch {
            disableTotpUseCase()
                .onSuccess {
                    _state.value = TotpSettingsScreenState.Disabled()
                    userRepository.refreshCurrentUser()
                }
                .onError { error ->
                    _state.value = current.copy(actionLoading = false, actionError = error)
                }
        }
    }

    override fun onRegenerateRecoveryCodesClick() {
        val current = _state.value as? TotpSettingsScreenState.Enabled ?: return
        _state.value = current.copy(showRegenerateConfirmation = true)
    }

    override fun onConfirmRegenerateRecoveryCodes() {
        val current = _state.value as? TotpSettingsScreenState.Enabled ?: return
        _state.value = current.copy(showRegenerateConfirmation = false, actionLoading = true, actionError = null)

        scope.launch {
            regenerateRecoveryCodesUseCase()
                .onSuccess { recoveryCodes ->
                    _state.value = current.copy(recoveryCodes = recoveryCodes, actionLoading = false)
                }
                .onError { error ->
                    _state.value = current.copy(actionLoading = false, actionError = error)
                }
        }
    }

    override fun onDismissDialogs() {
        val current = _state.value as? TotpSettingsScreenState.Enabled ?: return
        _state.value = current.copy(
            showDisableConfirmation = false,
            showRegenerateConfirmation = false
        )
    }

    override fun onBackClick() {
        onBack()
    }

    private suspend fun loadRecoveryCodes() {
        getRecoveryCodesUseCase()
            .onSuccess { recoveryCodes ->
                _state.value = TotpSettingsScreenState.Enabled(recoveryCodes = recoveryCodes)
            }
            .onError { error ->
                _state.value = TotpSettingsScreenState.Error(error)
            }
    }
}
