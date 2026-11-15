package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.unlock.target

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.target.UnlockTargetInputComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.target.UnlockTargetInputScreenState
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.auth.unlock.UnlockMethod

/**
 * Mock implementation of [UnlockTargetInputComponent] for Compose previews and testing.
 */
public open class UnlockTargetInputComponentMock(
    initialState: UnlockTargetInputScreenState = UnlockTargetInputScreenState(method = UnlockMethod.EMAIL)
) : UnlockTargetInputComponent {

    private val mutableState = MutableValue(initialState)
    public override val state: Value<UnlockTargetInputScreenState> = mutableState

    public override fun onInputChanged(value: String) {
        mutableState.value = mutableState.value.copy(input = value)
    }

    public override fun onSendCodeClick(): Unit = Unit

    public override fun onBackClick(): Unit = Unit
}
