package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.unlock.target

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.UnlockMethod
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.target.UnlockTargetInputComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.target.UnlockTargetInputScreenState

open class UnlockTargetInputComponentMock(
    initialState: UnlockTargetInputScreenState = UnlockTargetInputScreenState(method = UnlockMethod.EMAIL)
) : UnlockTargetInputComponent {

    private val mutableState = MutableValue(initialState)
    override val state: Value<UnlockTargetInputScreenState> = mutableState

    override fun onInputChanged(value: String) {
        mutableState.value = mutableState.value.copy(input = value)
    }

    override fun onSendCodeClick() {
    }

    override fun onBackClick() {
    }
}
