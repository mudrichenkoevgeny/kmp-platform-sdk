package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.user.totp.recovery

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.totp.recovery.TotpRecoveryCodesComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.totp.recovery.TotpRecoveryCodesScreenState
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totprecoverycodes.TotpRecoveryCodes

/**
 * Mock implementation of [TotpRecoveryCodesComponent] for Compose previews and testing.
 */
@InternalApi
public open class TotpRecoveryCodesComponentMock(
    initialState: TotpRecoveryCodesScreenState = TotpRecoveryCodesScreenState.Content(
        recoveryCodes = TotpRecoveryCodes(codes = listOf("1111-2222", "3333-4444"))
    )
) : TotpRecoveryCodesComponent {

    private val _state: MutableValue<TotpRecoveryCodesScreenState> = MutableValue(initialState)
    public override val state: Value<TotpRecoveryCodesScreenState> = _state

    public override fun onRegenerateClick(): Unit = Unit

    public override fun onConfirmRegenerate(): Unit = Unit

    public override fun onDismissDialogs(): Unit = Unit

    public override fun onBackClick(): Unit = Unit

    /**
     * Updates the component state value.
     *
     * @param state The new state value.
     */
    public fun updateState(state: TotpRecoveryCodesScreenState) {
        _state.value = state
    }
}
