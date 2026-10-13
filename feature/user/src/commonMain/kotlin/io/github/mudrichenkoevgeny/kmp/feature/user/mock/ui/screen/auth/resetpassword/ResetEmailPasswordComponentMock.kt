package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.resetpassword

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.resetpassword.ResetEmailPasswordComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.resetpassword.ResetEmailPasswordScreenState
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.resetpassword.ResetPasswordStepConfig

@InternalApi
class ResetEmailPasswordComponentMock(
    initialState: ResetEmailPasswordScreenState = ResetEmailPasswordScreenState(),
    initialConfig: ResetPasswordStepConfig = ResetPasswordStepConfig.EmailInput()
) : ResetEmailPasswordComponent {

    override val stackNavigation = StackNavigation<ResetPasswordStepConfig>()

    private val _stack = MutableValue(
        ChildStack(
            configuration = initialConfig,
            instance = createChild(initialConfig)
        )
    )
    override val stack: Value<ChildStack<ResetPasswordStepConfig, ResetEmailPasswordComponent.Child>> = _stack

    private val _state = MutableValue(initialState)
    override val state: Value<ResetEmailPasswordScreenState> = _state

    var sendCodeCalls = 0
    var resetEmailCalls = 0
    var confirmResetCalls = 0
    var backCalls = 0
    var lastEmailChanged: String? = null
    var lastCodeChanged: String? = null
    var lastPasswordChanged: String? = null

    fun setStep(config: ResetPasswordStepConfig) {
        _stack.value = ChildStack(
            configuration = config,
            instance = createChild(config)
        )
    }

    private fun createChild(config: ResetPasswordStepConfig): ResetEmailPasswordComponent.Child = when (config) {
        is ResetPasswordStepConfig.EmailInput -> ResetEmailPasswordComponent.Child.EmailInput(config.initialEmail)
        is ResetPasswordStepConfig.ConfirmInput -> ResetEmailPasswordComponent.Child.ConfirmInput(config.email)
    }

    fun updateState(state: ResetEmailPasswordScreenState) {
        _state.value = state
    }

    override fun onEmailChanged(email: String) {
        lastEmailChanged = email
    }

    override fun onCodeChanged(code: String) {
        lastCodeChanged = code
    }

    override fun onPasswordChanged(password: String) {
        lastPasswordChanged = password
    }

    override fun onSendCodeClick() {
        sendCodeCalls++
    }

    override fun onResetEmailClick() {
        resetEmailCalls++
    }

    override fun onConfirmResetClick() {
        confirmResetCalls++
    }

    override fun onBackClick() {
        backCalls++
    }
}
