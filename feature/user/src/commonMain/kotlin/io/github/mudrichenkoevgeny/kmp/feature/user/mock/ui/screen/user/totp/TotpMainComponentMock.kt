package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.user.totp

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.totp.main.TotpDialogConfig
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.totp.main.TotpMainComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.totp.main.TotpMainScreenState
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.totp.main.TotpStepConfig

@InternalApi
class TotpMainComponentMock(
    initialState: TotpMainScreenState = TotpMainScreenState()
) : TotpMainComponent {

    override val stackNavigation = StackNavigation<TotpStepConfig>()
    override val dialogSlotNavigation = SlotNavigation<TotpDialogConfig>()

    private val defaultStepConfig: TotpStepConfig = TotpStepConfig.Status

    private val _stack = MutableValue(
        ChildStack(
            configuration = defaultStepConfig,
            instance = createChild(defaultStepConfig)
        )
    )
    override val stack: Value<ChildStack<TotpStepConfig, TotpMainComponent.Child>> = _stack

    override val dialogSlot: Value<ChildSlot<TotpDialogConfig, TotpMainComponent.DialogChild>> = MutableValue(
        ChildSlot()
    )

    private val _state = MutableValue(initialState)
    override val state: Value<TotpMainScreenState> = _state

    var setupCalls: Int = 0
    var codeChangedCalls: MutableList<String> = mutableListOf()
    var confirmSetupCalls: Int = 0
    var recoveryCodesCalls: Int = 0
    var disableCalls: Int = 0
    var backCalls: Int = 0

    fun setStep(config: TotpStepConfig) {
        _stack.value = ChildStack(
            configuration = config,
            instance = createChild(config)
        )
    }

    private fun createChild(config: TotpStepConfig): TotpMainComponent.Child = when (config) {
        is TotpStepConfig.Status -> TotpMainComponent.Child.Status
        is TotpStepConfig.Setup -> TotpMainComponent.Child.Setup(config.mfaToken, config.otpAuthUrl, config.secretKey)
    }

    fun updateState(state: TotpMainScreenState) {
        _state.value = state
    }

    override fun onSetupClick() {
        setupCalls++
    }

    override fun onCodeChanged(code: String) {
        codeChangedCalls.add(code)
    }

    override fun onConfirmSetupClick() {
        confirmSetupCalls++
    }

    override fun onRecoveryCodesClick() {
        recoveryCodesCalls++
    }

    override fun onDisableClick() {
        disableCalls++
    }

    override fun onConfirmDisable() {}

    override fun onDismissDialogs() {}

    override fun onBackClick() {
        backCalls++
    }
}
