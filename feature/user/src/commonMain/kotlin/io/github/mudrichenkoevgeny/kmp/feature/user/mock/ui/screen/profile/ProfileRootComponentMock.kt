package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.ProfileDestination
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.ProfileRootComponent

@InternalApi
class ProfileRootComponentMock(
    initialChild: ProfileRootComponent.Child,
    initialConfiguration: ProfileDestination = ProfileDestination.Main
) : ProfileRootComponent {

    private val _stack = MutableValue(
        ChildStack(
            configuration = initialConfiguration,
            instance = initialChild
        )
    )

    override val stack: Value<ChildStack<ProfileDestination, ProfileRootComponent.Child>> = _stack

    fun setChild(configuration: ProfileDestination, child: ProfileRootComponent.Child) {
        _stack.value = ChildStack(
            configuration = configuration,
            instance = child
        )
    }
}