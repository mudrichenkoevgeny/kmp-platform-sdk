package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.totp.recovery

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp.recovery.TotpRecoveryCodesComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp.recovery.TotpRecoveryCodesScreenState
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totprecoverycodes.TotpRecoveryCodes

@InternalApi
open class TotpRecoveryCodesComponentMock(
    initialState: TotpRecoveryCodesScreenState = TotpRecoveryCodesScreenState.Content(
        recoveryCodes = TotpRecoveryCodes(codes = listOf("1111-2222", "3333-4444"))
    )
) : TotpRecoveryCodesComponent {

    private val _state = MutableValue(initialState)
    override val state: Value<TotpRecoveryCodesScreenState> = _state

    override fun onRegenerateClick() {}

    override fun onConfirmRegenerate() {}

    override fun onDismissDialogs() {}

    override fun onBackClick() {}

    fun updateState(state: TotpRecoveryCodesScreenState) {
        _state.value = state
    }
}
