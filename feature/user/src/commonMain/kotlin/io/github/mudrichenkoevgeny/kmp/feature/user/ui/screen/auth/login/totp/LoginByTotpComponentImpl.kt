package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.totp

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.result.onError
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.LoginByTotpRecoveryCodeUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.LoginByTotpUseCase
import kotlinx.coroutines.launch

/**
 * Default [LoginByTotpComponent]: manages MFA state and performs login through TOTP or recovery codes.
 *
 * @param componentContext Decompose [ComponentContext].
 * @param mfaToken Opaque intermediate token required for the login call.
 * @param loginByTotpUseCase Performs sign-in with TOTP code.
 * @param loginByTotpRecoveryCodeUseCase Performs sign-in with recovery code.
 * @param onBack Pops this screen on the parent stack.
 * @param onFinished Invoked when login succeeds so the host can close the flow.
 */
class LoginByTotpComponentImpl(
    componentContext: ComponentContext,
    mfaToken: String,
    private val loginByTotpUseCase: LoginByTotpUseCase,
    private val loginByTotpRecoveryCodeUseCase: LoginByTotpRecoveryCodeUseCase,
    private val onBack: () -> Unit,
    private val onFinished: () -> Unit
) : LoginByTotpComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()

    private val _state = MutableValue<LoginByTotpScreenState>(
        LoginByTotpScreenState.Content(mfaToken = mfaToken)
    )
    override val state: Value<LoginByTotpScreenState> = _state

    override fun onCodeChanged(code: String) {
        val current = _state.value as? LoginByTotpScreenState.Content ?: return
        _state.value = current.copy(
            code = code,
            actionError = null
        )
    }

    override fun onToggleModeClick() {
        val current = _state.value as? LoginByTotpScreenState.Content ?: return
        val nextMode = when (current.mode) {
            LoginByTotpScreenState.Mode.TOTP -> LoginByTotpScreenState.Mode.RECOVERY_CODE
            LoginByTotpScreenState.Mode.RECOVERY_CODE -> LoginByTotpScreenState.Mode.TOTP
        }
        _state.value = current.copy(
            mode = nextMode,
            code = "",
            actionError = null
        )
    }

    override fun onSubmitClick() {
        val current = _state.value as? LoginByTotpScreenState.Content ?: return
        if (!current.canSubmit) return

        _state.value = current.copy(actionLoading = true, actionError = null)

        scope.launch {
            val result = when (current.mode) {
                LoginByTotpScreenState.Mode.TOTP -> loginByTotpUseCase.execute(current.mfaToken, current.code)
                LoginByTotpScreenState.Mode.RECOVERY_CODE -> loginByTotpRecoveryCodeUseCase.execute(current.mfaToken, current.code)
            }

            result
                .onSuccess { onFinished() }
                .onError { error ->
                    _state.value = current.copy(
                        actionLoading = false,
                        actionError = error
                    )
                }
        }
    }

    override fun onBackClick() {
        onBack()
    }
}
