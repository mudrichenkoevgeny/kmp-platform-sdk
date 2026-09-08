package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.settings

import com.arkivanov.decompose.Child
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.settings.main.MainManagementSettingsComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.ManagementSettingsDestination
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.ManagementSettingsRootComponent

@InternalApi
class ManagementSettingsRootComponentMock(
    initialChild: ManagementSettingsRootComponent.Child = ManagementSettingsRootComponent.Child.Main(
        MainManagementSettingsComponentMock()
    )
) : ManagementSettingsRootComponent {
    override val stack: Value<ChildStack<ManagementSettingsDestination, ManagementSettingsRootComponent.Child>> = MutableValue(
        ChildStack(
            active = Child.Created(
                configuration = ManagementSettingsDestination.Main,
                instance = initialChild
            ),
            backStack = emptyList()
        )
    )
}
