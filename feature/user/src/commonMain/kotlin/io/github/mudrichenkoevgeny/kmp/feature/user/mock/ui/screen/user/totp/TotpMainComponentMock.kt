package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.user.totp

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.totp.main.TotpMainComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.totp.main.TotpMainScreenState
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.totp.main.TotpStepConfig
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.totp.main.dialog.TotpDialogChild
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.totp.main.dialog.TotpDialogConfig

/**
 * Mock implementation of [TotpMainComponent] for Compose previews and testing.
 */
@InternalApi
public class TotpMainComponentMock(
    initialState: TotpMainScreenState = TotpMainScreenState()
) : TotpMainComponent {

    public override val stackNavigation: StackNavigation<TotpStepConfig> = StackNavigation<TotpStepConfig>()
    public override val dialogSlotNavigation: SlotNavigation<TotpDialogConfig> = SlotNavigation<TotpDialogConfig>()

    private val defaultStepConfig: TotpStepConfig = TotpStepConfig.Status

    private val _stack: MutableValue<ChildStack<TotpStepConfig, TotpMainComponent.Child>> = MutableValue(
        ChildStack(
            configuration = defaultStepConfig,
            instance = createChild(defaultStepConfig)
        )
    )
    public override val stack: Value<ChildStack<TotpStepConfig, TotpMainComponent.Child>> = _stack

    public override val dialogSlot: Value<ChildSlot<TotpDialogConfig, TotpDialogChild>> = MutableValue(
        ChildSlot()
    )

    private val _state: MutableValue<TotpMainScreenState> = MutableValue(initialState)
    public override val state: Value<TotpMainScreenState> = _state

    public var setupCalls: Int = 0
    public var codeChangedCalls: MutableList<String> = mutableListOf()
    public var confirmSetupCalls: Int = 0
    public var recoveryCodesCalls: Int = 0
    public var disableCalls: Int = 0
    public var backCalls: Int = 0

    /**
     * Updates the current active step configuration in the stack.
     *
     * @param config The step configuration.
     */
    public fun setStep(config: TotpStepConfig) {
        _stack.value = ChildStack(
            configuration = config,
            instance = createChild(config)
        )
    }

    private fun createChild(config: TotpStepConfig): TotpMainComponent.Child = when (config) {
        is TotpStepConfig.Status -> TotpMainComponent.Child.Status
        is TotpStepConfig.Setup -> TotpMainComponent.Child.Setup(config.mfaToken, config.otpAuthUrl, config.secretKey)
    }

    /**
     * Updates the component state value.
     *
     * @param state The new state value.
     */
    public fun updateState(state: TotpMainScreenState) {
        _state.value = state
    }

    public override fun onSetupClick() {
        setupCalls++
    }

    public override fun onCodeChanged(code: String) {
        codeChangedCalls.add(code)
    }

    public override fun onConfirmSetupClick() {
        confirmSetupCalls++
    }

    public override fun onRecoveryCodesClick() {
        recoveryCodesCalls++
    }

    public override fun onDisableClick() {
        disableCalls++
    }

    public override fun onConfirmDisable(): Unit = Unit

    public override fun onDismissDialogs(): Unit = Unit

    public override fun onBackClick() {
        backCalls++
    }
}
