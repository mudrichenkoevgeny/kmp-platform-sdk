package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.unlock.otp

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.otp.UnlockOtpComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.otp.UnlockOtpScreenState

/**
 * Mock implementation of [UnlockOtpComponent] for Compose previews and testing.
 */
@InternalApi
public open class UnlockOtpComponentMock(
    initialState: UnlockOtpScreenState = UnlockOtpScreenState()
) : UnlockOtpComponent {

    private val mutableState = MutableValue(initialState)
    public override val state: Value<UnlockOtpScreenState> = mutableState

    /**
     * Updates the component state value.
     *
     * @param state The new state value.
     */
    public fun updateState(state: UnlockOtpScreenState) {
        mutableState.value = state
    }

    public override fun onCodeChanged(code: String) {
        mutableState.value = mutableState.value.copy(codeInput = code)
    }

    public override fun onUnlockClick(): Unit = Unit
    public override fun onResendCodeClick(): Unit = Unit
    public override fun onBackClick(): Unit = Unit
}
