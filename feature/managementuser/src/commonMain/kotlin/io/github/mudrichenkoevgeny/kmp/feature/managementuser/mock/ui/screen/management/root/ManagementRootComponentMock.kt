package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.root

import com.arkivanov.decompose.Child
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.main.MainManagementComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.ManagementDestination
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.root.ManagementRootComponent

@InternalApi
class ManagementRootComponentMock(
    initialChild: ManagementRootComponent.Child = ManagementRootComponent.Child.Main(
        MainManagementComponentMock()
    )
) : ManagementRootComponent {
    override val stack: Value<ChildStack<ManagementDestination, ManagementRootComponent.Child>> = MutableValue(
        ChildStack(
            active = Child.Created(
                configuration = ManagementDestination.Main,
                instance = initialChild
            ),
            backStack = emptyList()
        )
    )
}