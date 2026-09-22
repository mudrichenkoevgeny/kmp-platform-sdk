package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.unlock.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.unlock.selection.UnlockMethodSelectionComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.UnlockDestination
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.root.UnlockRootComponent

@InternalApi
class UnlockRootComponentMock(
    initialChild: UnlockRootComponent.Child = UnlockRootComponent.Child.MethodSelection(UnlockMethodSelectionComponentMock())
) : UnlockRootComponent {
    override val stack: Value<ChildStack<UnlockDestination, UnlockRootComponent.Child>> = MutableValue(
        ChildStack(
            configuration = UnlockDestination.MethodSelection,
            instance = initialChild
        )
    )

    override fun onDismiss() {}
}
