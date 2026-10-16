package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.totp

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.result.onError
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.LoginByTotpRecoveryCodeUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.LoginByTotpUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.accountstatus.UserAccountStatus
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.auth.login.totp.LoginByTotpMode
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.auth.login.totp.LoginByTotpParams
import kotlinx.coroutines.launch

/**
 * Default [LoginByTotpComponent]: manages MFA state and performs login through TOTP or recovery codes.
 *
 * @param componentContext Decompose [ComponentContext].
 * @param params Screen destination parameters.
 * @param loginByTotpUseCase Performs sign-in with TOTP code.
 * @param loginByTotpRecoveryCodeUseCase Performs sign-in with recovery code.
 * @param onNavigateToPendingDeletion Opens the pending deletion restoration screen on the parent stack.
 * @param onBack Pops this screen on the parent stack.
 * @param onFinished Invoked when login succeeds so the host can close the flow.
 */
class LoginByTotpComponentImpl(
    componentContext: ComponentContext,
    params: LoginByTotpParams = LoginByTotpParams(),
    private val loginByTotpUseCase: LoginByTotpUseCase,
    private val loginByTotpRecoveryCodeUseCase: LoginByTotpRecoveryCodeUseCase,
    private val onNavigateToPendingDeletion: () -> Unit,
    private val onBack: () -> Unit,
    private val onFinished: () -> Unit
) : LoginByTotpComponent, ComponentContext by componentContext {

    constructor(
        componentContext: ComponentContext,
        mfaToken: String,
        loginByTotpUseCase: LoginByTotpUseCase,
        loginByTotpRecoveryCodeUseCase: LoginByTotpRecoveryCodeUseCase,
        onNavigateToPendingDeletion: () -> Unit,
        onBack: () -> Unit,
        onFinished: () -> Unit
    ) : this(
        componentContext = componentContext,
        params = LoginByTotpParams(mfaToken = mfaToken),
        loginByTotpUseCase = loginByTotpUseCase,
        loginByTotpRecoveryCodeUseCase = loginByTotpRecoveryCodeUseCase,
        onNavigateToPendingDeletion = onNavigateToPendingDeletion,
        onBack = onBack,
        onFinished = onFinished
    )

    private val scope = componentCoroutineScope()

    private val _state = MutableValue<LoginByTotpScreenState>(
        LoginByTotpScreenState.Content(mfaToken = params.mfaToken ?: "")
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
        val newMode = if (current.mode == LoginByTotpMode.TOTP) LoginByTotpMode.RECOVERY_CODE else LoginByTotpMode.TOTP
        _state.value = current.copy(
            mode = newMode,
            code = "",
            actionError = null
        )
    }

    override fun onSubmitClick() {
        val current = _state.value as? LoginByTotpScreenState.Content ?: return
        if (!current.canSubmit) {
            return
        }

        _state.value = current.copy(actionLoading = true, actionError = null)

        scope.launch {
            val result = if (current.mode == LoginByTotpMode.RECOVERY_CODE) {
                loginByTotpRecoveryCodeUseCase.execute(current.mfaToken, current.code)
            } else {
                loginByTotpUseCase.execute(current.mfaToken, current.code)
            }

            result
                .onSuccess { authData ->
                    if (authData.userPrivate.accountStatus == UserAccountStatus.PENDING_DELETION) {
                        onNavigateToPendingDeletion()
                    } else {
                        onFinished()
                    }
                }
                .onError { error ->
                    val latest = _state.value as? LoginByTotpScreenState.Content
                    if (latest != null) {
                        _state.value = latest.copy(actionLoading = false, actionError = error)
                    }
                }
        }
    }

    override fun onBackClick() {
        onBack()
    }
}
