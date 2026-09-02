package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.totp

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp.TotpSettingsComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp.TotpSettingsScreenState

@InternalApi
class TotpSettingsComponentMock(
    initialState: TotpSettingsScreenState = TotpSettingsScreenState.Loading
) : TotpSettingsComponent {

    private val _state = MutableValue(initialState)
    override val state: Value<TotpSettingsScreenState> = _state

    var setupCalls: Int = 0
    var codeChangedCalls: MutableList<String> = mutableListOf()
    var confirmSetupCalls: Int = 0
    var disableCalls: Int = 0
    var regenerateRecoveryCodesCalls: Int = 0
    var backCalls: Int = 0

    fun updateState(state: TotpSettingsScreenState) {
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

    override fun onDisableClick() {
        disableCalls++
    }

    override fun onRegenerateRecoveryCodesClick() {
        regenerateRecoveryCodesCalls++
    }

    override fun onBackClick() {
        backCalls++
    }

    override fun onConfirmDisable() {}

    override fun onConfirmRegenerateRecoveryCodes() {}

    override fun onDismissDialogs() {}
}