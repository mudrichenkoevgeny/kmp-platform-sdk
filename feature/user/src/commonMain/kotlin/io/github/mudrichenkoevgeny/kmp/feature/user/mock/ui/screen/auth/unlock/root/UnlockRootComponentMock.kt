package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.unlock.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.unlock.selection.UnlockMethodSelectionComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.root.UnlockRootComponent
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.route.client.ClientAuthNavigation

/**
 * Mock implementation of [UnlockRootComponent] for Compose previews and testing.
 */
@InternalApi
public class UnlockRootComponentMock(
    initialChild: UnlockRootComponent.Child = UnlockRootComponent.Child.MethodSelection(
        UnlockMethodSelectionComponentMock()
    )
) : UnlockRootComponent {
    public override val stack: Value<ChildStack<ClientAuthNavigation, UnlockRootComponent.Child>> = MutableValue(
        ChildStack(
            configuration = ClientAuthNavigation.UnlockMethodSelection(),
            instance = initialChild
        )
    )

    public override fun onDismiss(): Unit = Unit
}
