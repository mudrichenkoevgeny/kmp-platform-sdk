package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.unlock.selection

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.selection.UnlockMethodSelectionComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.selection.UnlockMethodSelectionScreenState

@InternalApi
open class UnlockMethodSelectionComponentMock(
    initialState: UnlockMethodSelectionScreenState = UnlockMethodSelectionScreenState()
) : UnlockMethodSelectionComponent {

    override val state: Value<UnlockMethodSelectionScreenState> = MutableValue(initialState)

    override fun onEmailInputChanged(email: String) {}
    override fun onPhoneInputChanged(phone: String) {}
    override fun onSelectEmailUnlock() {}
    override fun onSelectPhoneUnlock() {}
    override fun onSelectGoogleUnlock() {}
    override fun onSelectAppleUnlock() {}
    override fun onBackClick() {}
}
