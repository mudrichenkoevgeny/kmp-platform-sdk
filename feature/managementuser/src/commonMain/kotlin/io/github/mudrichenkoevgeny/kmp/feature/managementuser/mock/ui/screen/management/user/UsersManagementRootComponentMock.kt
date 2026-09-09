package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.user

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.UsersManagementDestination
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.UsersManagementRootComponent

@InternalApi
class UsersManagementRootComponentMock(
    initialChild: UsersManagementRootComponent.Child,
    initialConfiguration: UsersManagementDestination = UsersManagementDestination.Main,
) : UsersManagementRootComponent {

    private val _stack = MutableValue(
        ChildStack(
            configuration = initialConfiguration,
            instance = initialChild,
        )
    )

    override val stack: Value<ChildStack<UsersManagementDestination, UsersManagementRootComponent.Child>> = _stack

    var backCalls = 0

    fun setChild(configuration: UsersManagementDestination, child: UsersManagementRootComponent.Child) {
        _stack.value = ChildStack(
            configuration = configuration,
            instance = child,
        )
    }

    override fun onBackClick() {
        backCalls++
    }
}
