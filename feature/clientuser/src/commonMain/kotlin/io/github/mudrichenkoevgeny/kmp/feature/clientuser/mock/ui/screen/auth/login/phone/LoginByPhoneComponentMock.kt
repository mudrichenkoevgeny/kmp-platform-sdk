package io.github.mudrichenkoevgeny.kmp.feature.clientuser.mock.ui.screen.auth.login.phone

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.phone.LoginByPhoneComponent
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.phone.LoginByPhoneScreenState
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.phone.LoginByPhoneStepConfig

/**
 * Mock implementation of [LoginByPhoneComponent] for testing and Compose previews.
 *
 * @param initialState Initial screen state to initialize the mock with.
 */
@InternalApi
public class LoginByPhoneComponentMock(
    initialState: LoginByPhoneScreenState = LoginByPhoneScreenState()
) : LoginByPhoneComponent {

    override val stackNavigation: StackNavigation<LoginByPhoneStepConfig> = StackNavigation<LoginByPhoneStepConfig>()

    private val defaultStepConfig: LoginByPhoneStepConfig =
        LoginByPhoneStepConfig.PhoneInput(initialPhone = initialState.phoneNumber)

    private val _stack = MutableValue(
        ChildStack(
            configuration = defaultStepConfig,
            instance = createChild(defaultStepConfig)
        )
    )
    override val stack: Value<ChildStack<LoginByPhoneStepConfig, LoginByPhoneComponent.Child>> = _stack

    private val _state = MutableValue(initialState)
    override val state: Value<LoginByPhoneScreenState> = _state

    public var sendCodeCalls: Int = 0
    public var confirmCodeCalls: Int = 0
    public var resetPhoneCalls: Int = 0
    public var backCalls: Int = 0
    public var lastPhoneChanged: String? = null
    public var lastCodeChanged: String? = null

    /**
     * Updates the current active step configuration in the Decompose child stack.
     *
     * @param config Target step configuration to push onto the stack.
     */
    public fun setStep(config: LoginByPhoneStepConfig) {
        _stack.value = ChildStack(
            configuration = config,
            instance = createChild(config)
        )
    }

    private fun createChild(config: LoginByPhoneStepConfig): LoginByPhoneComponent.Child = when (config) {
        is LoginByPhoneStepConfig.PhoneInput -> LoginByPhoneComponent.Child.PhoneInput(config.initialPhone)
        is LoginByPhoneStepConfig.CodeInput -> LoginByPhoneComponent.Child.CodeInput(config.phone)
    }

    /**
     * Updates the current screen state emitted by [state].
     *
     * @param state New screen state to emit.
     */
    public fun updateState(state: LoginByPhoneScreenState) {
        _state.value = state
    }

    override fun onPhoneChanged(phone: String) {
        lastPhoneChanged = phone
    }

    override fun onSendCodeClick() {
        sendCodeCalls++
    }

    override fun onCodeChanged(code: String) {
        lastCodeChanged = code
    }

    override fun onConfirmCodeClick() {
        confirmCodeCalls++
    }

    override fun onResetPhoneClick() {
        resetPhoneCalls++
    }

    override fun onBackClick() {
        backCalls++
    }
}
