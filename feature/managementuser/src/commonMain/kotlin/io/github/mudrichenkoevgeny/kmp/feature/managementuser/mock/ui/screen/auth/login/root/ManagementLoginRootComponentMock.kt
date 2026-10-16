package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.auth.login.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.auth.login.root.ManagementLoginRootComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.login.welcome.LoginWelcomeComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.welcome.LoginWelcomeScreenState
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.AvailableAuthProviders
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.route.management.ManagementAuthNavigation

@InternalApi
open class ManagementLoginRootComponentMock : ManagementLoginRootComponent {

    override val stack: Value<ChildStack<ManagementAuthNavigation, ManagementLoginRootComponent.Child>> = MutableValue(
        ChildStack(
            configuration = ManagementAuthNavigation.Main,
            instance = ManagementLoginRootComponent.Child.Welcome(
                LoginWelcomeComponentMock(
                    initialState = LoginWelcomeScreenState.Content(
                        availableAuthProviders = AvailableAuthProviders(
                            primary = listOf(UserAuthProvider.EMAIL),
                            secondary = emptyList()
                        )
                    )
                )
            )
        )
    )

    override fun navigateTo(destination: ManagementAuthNavigation) {}

    override fun onDismiss() {}
}
