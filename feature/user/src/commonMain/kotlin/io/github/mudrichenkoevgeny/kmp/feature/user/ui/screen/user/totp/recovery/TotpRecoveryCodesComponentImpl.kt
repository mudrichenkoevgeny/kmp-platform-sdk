package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.totp.recovery

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackDispatcher
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.result.onError
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.GetRecoveryCodesUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.RegenerateRecoveryCodesUseCase
import kotlinx.coroutines.launch

/** Default implementation of [TotpRecoveryCodesComponent]. */
public class TotpRecoveryCodesComponentImpl(
    componentContext: ComponentContext,
    private val getRecoveryCodesUseCase: GetRecoveryCodesUseCase,
    private val regenerateRecoveryCodesUseCase: RegenerateRecoveryCodesUseCase,
    private val onBack: () -> Unit
) : TotpRecoveryCodesComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()
    private val _state = MutableValue<TotpRecoveryCodesScreenState>(TotpRecoveryCodesScreenState.Loading)
    public override val state: Value<TotpRecoveryCodesScreenState> = _state

    init {
        loadRecoveryCodes()
    }

    public override fun onRegenerateClick() {
        val current = _state.value as? TotpRecoveryCodesScreenState.Content ?: return
        _state.value = current.copy(showRegenerateConfirmation = true)
    }

    public override fun onConfirmRegenerate() {
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

    public override fun onDismissDialogs() {
        val current = _state.value as? TotpRecoveryCodesScreenState.Content ?: return
        _state.value = current.copy(showRegenerateConfirmation = false)
    }

    public override fun onBackClick() {
        if ((backHandler as? BackDispatcher)?.back() != true) {
            onBack()
        }
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
