package io.github.mudrichenkoevgeny.kmp.feature.clientuser.mock.ui.screen.auth.registration.email

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.registration.email.RegistrationByEmailComponent
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.registration.email.RegistrationByEmailScreenState
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.registration.email.RegistrationByEmailStepConfig

/**
 * Mock implementation of [RegistrationByEmailComponent] for testing and Compose previews.
 *
 * @param initialState Initial screen state to initialize the mock with.
 */
@InternalApi
public class RegistrationByEmailComponentMock(
    initialState: RegistrationByEmailScreenState = RegistrationByEmailScreenState()
) : RegistrationByEmailComponent {

    override val stackNavigation: StackNavigation<RegistrationByEmailStepConfig> =
        StackNavigation<RegistrationByEmailStepConfig>()

    private val defaultStepConfig: RegistrationByEmailStepConfig =
        RegistrationByEmailStepConfig.EmailInput(initialEmail = initialState.email)

    private val _stack = MutableValue(
        ChildStack(
            configuration = defaultStepConfig,
            instance = createChild(defaultStepConfig)
        )
    )
    override val stack: Value<ChildStack<RegistrationByEmailStepConfig, RegistrationByEmailComponent.Child>> = _stack

    private val _state = MutableValue(initialState)
    override val state: Value<RegistrationByEmailScreenState> = _state

    public var sendCodeCalls: Int = 0
    public var registerCalls: Int = 0
    public var backCalls: Int = 0
    public var togglePasswordVisibilityCalls: Int = 0
    public var lastEmailChanged: String? = null
    public var lastCodeChanged: String? = null
    public var lastPasswordChanged: String? = null

    /**
     * Updates the current active step configuration in the Decompose child stack.
     *
     * @param config Target step configuration to push onto the stack.
     */
    public fun setStep(config: RegistrationByEmailStepConfig) {
        _stack.value = ChildStack(
            configuration = config,
            instance = createChild(config)
        )
    }

    private fun createChild(config: RegistrationByEmailStepConfig): RegistrationByEmailComponent.Child = when (config) {
        is RegistrationByEmailStepConfig.EmailInput -> RegistrationByEmailComponent.Child.EmailInput(
            config.initialEmail
        )

        is RegistrationByEmailStepConfig.ConfirmInput -> RegistrationByEmailComponent.Child.ConfirmInput(config.email)
    }

    /**
     * Updates the current screen state emitted by [state].
     *
     * @param state New screen state to emit.
     */
    public fun updateState(state: RegistrationByEmailScreenState) {
        _state.value = state
    }

    override fun onEmailChanged(email: String) {
        lastEmailChanged = email
    }

    override fun onSendCodeClick() {
        sendCodeCalls++
    }

    override fun onCodeChanged(code: String) {
        lastCodeChanged = code
    }

    override fun onPasswordChanged(password: String) {
        lastPasswordChanged = password
    }

    override fun onTogglePasswordVisibility() {
        togglePasswordVisibilityCalls++
    }

    override fun onRegisterClick() {
        registerCalls++
    }

    override fun onBackClick() {
        backCalls++
    }
}
