package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.unlock.selection

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.selection.UnlockMethodSelectionComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.selection.UnlockMethodSelectionScreenState

open class UnlockMethodSelectionComponentMock(
    initialState: UnlockMethodSelectionScreenState = UnlockMethodSelectionScreenState()
) : UnlockMethodSelectionComponent {

    private val mutableState = MutableValue(initialState)
    override val state: Value<UnlockMethodSelectionScreenState> = mutableState

    fun updateState(state: UnlockMethodSelectionScreenState) {
        mutableState.value = state
    }

    override fun onSelectEmailUnlock() {
    }

    override fun onSelectPhoneUnlock() {
    }

    override fun onSelectGoogleUnlock() {
    }

    override fun onSelectAppleUnlock() {
    }

    override fun onBackClick() {
    }
}
