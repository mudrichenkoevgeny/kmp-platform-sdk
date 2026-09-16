package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.unlock.otp

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.otp.UnlockOtpComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.otp.UnlockOtpScreenState

@InternalApi
open class UnlockOtpComponentMock(
    initialState: UnlockOtpScreenState = UnlockOtpScreenState()
) : UnlockOtpComponent {

    override val state: Value<UnlockOtpScreenState> = MutableValue(initialState)

    override fun onCodeChanged(code: String) {}
    override fun onUnlockClick() {}
    override fun onResendCodeClick() {}
    override fun onBackClick() {}
}
