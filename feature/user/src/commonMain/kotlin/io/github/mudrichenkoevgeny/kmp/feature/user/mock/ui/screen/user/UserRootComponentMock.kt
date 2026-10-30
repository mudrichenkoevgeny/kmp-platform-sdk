package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.user

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.user.main.MainUserComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.root.UserRootComponent
import io.github.mudrichenkoevgeny.shared.foundation.core.common.navigation.route.NavigationRoute
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.route.client.ClientUserNavigation

@InternalApi
class UserRootComponentMock(
    child: UserRootComponent.Child = UserRootComponent.Child.Main(MainUserComponentMock())
) : UserRootComponent {

    private val _stack: MutableValue<ChildStack<NavigationRoute, UserRootComponent.Child>> = MutableValue(
        ChildStack(
            configuration = ClientUserNavigation.Main,
            instance = child
        )
    )

    override val stack: Value<ChildStack<NavigationRoute, UserRootComponent.Child>> = _stack

    fun setChild(configuration: NavigationRoute, child: UserRootComponent.Child) {
        _stack.value = ChildStack(
            configuration = configuration,
            instance = child
        )
    }

    override fun navigateTo(destination: NavigationRoute) { }
}
