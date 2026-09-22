package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp.recovery

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.result.onError
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.GetRecoveryCodesUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.RegenerateRecoveryCodesUseCase
import kotlinx.coroutines.launch

/** Default implementation of [TotpRecoveryCodesComponent]. */
class TotpRecoveryCodesComponentImpl(
    componentContext: ComponentContext,
    private val getRecoveryCodesUseCase: GetRecoveryCodesUseCase,
    private val regenerateRecoveryCodesUseCase: RegenerateRecoveryCodesUseCase,
    private val onBack: () -> Unit
) : TotpRecoveryCodesComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()
    private val _state = MutableValue<TotpRecoveryCodesScreenState>(TotpRecoveryCodesScreenState.Loading)
    override val state: Value<TotpRecoveryCodesScreenState> = _state

    init {
        loadRecoveryCodes()
    }

    override fun onRegenerateClick() {
        val current = _state.value as? TotpRecoveryCodesScreenState.Content ?: return
        _state.value = current.copy(showRegenerateConfirmation = true)
    }

    override fun onConfirmRegenerate() {
        val current = _state.value as? TotpRecoveryCodesScreenState.Content ?: return
        _state.value = current.copy(showRegenerateConfirmation = false, actionLoading = true, actionError = null)

        scope.launch {
            regenerateRecoveryCodesUseCase()
                .onSuccess { recoveryCodes ->
                    val latest = _state.value as? TotpRecoveryCodesScreenState.Content ?: return@onSuccess
                    _state.value = latest.copy(
                        recoveryCodes = recoveryCodes,
                        showRegenerateConfirmation = false,
                        actionLoading = false
                    )
                }
                .onError { error ->
                    val latest = _state.value as? TotpRecoveryCodesScreenState.Content ?: return@onError
                    _state.value = latest.copy(
                        showRegenerateConfirmation = false,
                        actionLoading = false,
                        actionError = error
                    )
                }
        }
    }

    override fun onDismissDialogs() {
        val current = _state.value as? TotpRecoveryCodesScreenState.Content ?: return
        _state.value = current.copy(showRegenerateConfirmation = false)
    }

    override fun onBackClick() {
        onBack()
    }

    private fun loadRecoveryCodes() {
        scope.launch {
            getRecoveryCodesUseCase()
                .onSuccess { recoveryCodes ->
                    _state.value = TotpRecoveryCodesScreenState.Content(recoveryCodes = recoveryCodes)
                }
                .onError { error ->
                    _state.value = TotpRecoveryCodesScreenState.Error(error)
                }
        }
    }
}
