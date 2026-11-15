package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.resetpassword

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.resetpassword.ResetEmailPasswordComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.resetpassword.ResetEmailPasswordScreenState
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.resetpassword.ResetPasswordStepConfig

/**
 * Mock implementation of [ResetEmailPasswordComponent] for Compose previews and testing.
 */
@InternalApi
public class ResetEmailPasswordComponentMock(
    initialState: ResetEmailPasswordScreenState = ResetEmailPasswordScreenState()
) : ResetEmailPasswordComponent {

    public override val stackNavigation: StackNavigation<ResetPasswordStepConfig> =
        StackNavigation<ResetPasswordStepConfig>()

    private val defaultStepConfig: ResetPasswordStepConfig =
        ResetPasswordStepConfig.EmailInput(initialEmail = initialState.email)

    private val _stack = MutableValue(
        ChildStack(
            configuration = defaultStepConfig,
            instance = createChild(defaultStepConfig)
        )
    )
    public override val stack: Value<ChildStack<ResetPasswordStepConfig, ResetEmailPasswordComponent.Child>> = _stack

    private val _state = MutableValue(initialState)
    public override val state: Value<ResetEmailPasswordScreenState> = _state

    public var sendCodeCalls: Int = 0
    public var resetEmailCalls: Int = 0
    public var confirmResetCalls: Int = 0
    public var backCalls: Int = 0
    public var lastEmailChanged: String? = null
    public var lastCodeChanged: String? = null
    public var lastPasswordChanged: String? = null

    /**
     * Updates the current active step configuration in the child stack.
     *
     * @param config The step configuration.
     */
    public fun setStep(config: ResetPasswordStepConfig) {
        _stack.value = ChildStack(
            configuration = config,
            instance = createChild(config)
        )
    }

    private fun createChild(config: ResetPasswordStepConfig): ResetEmailPasswordComponent.Child = when (config) {
        is ResetPasswordStepConfig.EmailInput -> ResetEmailPasswordComponent.Child.EmailInput(config.initialEmail)
        is ResetPasswordStepConfig.ConfirmInput -> ResetEmailPasswordComponent.Child.ConfirmInput(config.email)
    }

    /**
     * Updates the component state value.
     *
     * @param state The new state value.
     */
    public fun updateState(state: ResetEmailPasswordScreenState) {
        _state.value = state
    }

    public override fun onEmailChanged(email: String) {
        lastEmailChanged = email
    }

    public override fun onCodeChanged(code: String) {
        lastCodeChanged = code
    }

    public override fun onPasswordChanged(password: String) {
        lastPasswordChanged = password
    }

    public override fun onSendCodeClick() {
        sendCodeCalls++
    }

    public override fun onResetEmailClick() {
        resetEmailCalls++
    }

    public override fun onConfirmResetClick() {
        confirmResetCalls++
    }

    public override fun onBackClick() {
        backCalls++
    }
}
