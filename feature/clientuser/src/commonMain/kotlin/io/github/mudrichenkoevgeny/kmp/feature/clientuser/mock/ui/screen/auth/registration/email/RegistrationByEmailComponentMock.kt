package io.github.mudrichenkoevgeny.kmp.feature.clientuser.mock.ui.screen.auth.registration.email

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.registration.email.RegistrationByEmailComponent
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.registration.email.RegistrationByEmailScreenState
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.registration.email.RegistrationByEmailStepConfig

@InternalApi
class RegistrationByEmailComponentMock(
    initialState: RegistrationByEmailScreenState = RegistrationByEmailScreenState()
) : RegistrationByEmailComponent {

    override val stackNavigation = StackNavigation<RegistrationByEmailStepConfig>()

    private val defaultStepConfig: RegistrationByEmailStepConfig = RegistrationByEmailStepConfig.EmailInput(initialEmail = initialState.email)

    private val _stack = MutableValue(
        ChildStack(
            configuration = defaultStepConfig,
            instance = createChild(defaultStepConfig)
        )
    )
    override val stack: Value<ChildStack<RegistrationByEmailStepConfig, RegistrationByEmailComponent.Child>> = _stack

    private val _state = MutableValue(initialState)
    override val state: Value<RegistrationByEmailScreenState> = _state

    var sendCodeCalls = 0
    var registerCalls = 0
    var backCalls = 0
    var togglePasswordVisibilityCalls = 0
    var lastEmailChanged: String? = null
    var lastCodeChanged: String? = null
    var lastPasswordChanged: String? = null

    fun setStep(config: RegistrationByEmailStepConfig) {
        _stack.value = ChildStack(
            configuration = config,
            instance = createChild(config)
        )
    }

    private fun createChild(config: RegistrationByEmailStepConfig): RegistrationByEmailComponent.Child = when (config) {
        is RegistrationByEmailStepConfig.EmailInput -> RegistrationByEmailComponent.Child.EmailInput(config.initialEmail)
        is RegistrationByEmailStepConfig.ConfirmInput -> RegistrationByEmailComponent.Child.ConfirmInput(config.email)
    }

    fun updateState(state: RegistrationByEmailScreenState) {
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
