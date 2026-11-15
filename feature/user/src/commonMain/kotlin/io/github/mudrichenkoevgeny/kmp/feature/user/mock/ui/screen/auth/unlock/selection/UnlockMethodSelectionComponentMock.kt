package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.unlock.selection

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.selection.UnlockMethodSelectionComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.selection.UnlockMethodSelectionScreenState

/**
 * Mock implementation of [UnlockMethodSelectionComponent] for Compose previews and testing.
 */
public open class UnlockMethodSelectionComponentMock(
    initialState: UnlockMethodSelectionScreenState = UnlockMethodSelectionScreenState()
) : UnlockMethodSelectionComponent {

    private val mutableState = MutableValue(initialState)
    public override val state: Value<UnlockMethodSelectionScreenState> = mutableState

    /**
     * Updates the component state value.
     *
     * @param state The new state value.
     */
    public fun updateState(state: UnlockMethodSelectionScreenState) {
        mutableState.value = state
    }

    public override fun onSelectEmailUnlock(): Unit = Unit

    public override fun onSelectPhoneUnlock(): Unit = Unit

    public override fun onSelectGoogleUnlock(): Unit = Unit

    public override fun onSelectAppleUnlock(): Unit = Unit

    public override fun onBackClick(): Unit = Unit
}
