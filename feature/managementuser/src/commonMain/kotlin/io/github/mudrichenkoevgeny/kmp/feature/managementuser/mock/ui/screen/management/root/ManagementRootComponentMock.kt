package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.root

import com.arkivanov.decompose.Child
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.main.MainManagementComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.root.ManagementRootComponent
import io.github.mudrichenkoevgeny.shared.foundation.core.common.navigation.route.NavigationRoute
import io.github.mudrichenkoevgeny.shared.foundation.core.common.navigation.route.management.ManagementMainNavigation

/**
 * Mock implementation of [ManagementRootComponent] for testing.
 */
@InternalApi
public class ManagementRootComponentMock : ManagementRootComponent {
    public override val stack: Value<ChildStack<NavigationRoute, ManagementRootComponent.Child>> = MutableValue(
        ChildStack(
            active = Child.Created(
                configuration = ManagementMainNavigation.Main,
                instance = ManagementRootComponent.Child.Main(MainManagementComponentMock())
            ),
            backStack = emptyList()
        )
    )

    public override fun navigateTo(destination: NavigationRoute): Unit = Unit
}
