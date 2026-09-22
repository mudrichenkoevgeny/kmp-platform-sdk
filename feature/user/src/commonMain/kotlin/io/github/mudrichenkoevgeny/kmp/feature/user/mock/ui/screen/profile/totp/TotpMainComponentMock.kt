package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.totp

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp.main.TotpMainComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp.main.TotpMainScreenState

@InternalApi
class TotpMainComponentMock(
    initialState: TotpMainScreenState = TotpMainScreenState.Loading
) : TotpMainComponent {

    private val _state = MutableValue(initialState)
    override val state: Value<TotpMainScreenState> = _state

    var setupCalls: Int = 0
    var codeChangedCalls: MutableList<String> = mutableListOf()
    var confirmSetupCalls: Int = 0
    var recoveryCodesCalls: Int = 0
    var disableCalls: Int = 0
    var backCalls: Int = 0

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
