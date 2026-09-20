package io.github.mudrichenkoevgeny.kmp.feature.clientuser.mock.ui.screen.auth.login.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.ClientLoginDestination
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.root.ClientLoginRootComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.login.welcome.LoginWelcomeComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.welcome.LoginWelcomeScreenState
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.AvailableAuthProviders
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider

@InternalApi
open class ClientLoginRootComponentMock(
    initialChild: ClientLoginRootComponent.Child = ClientLoginRootComponent.Child.Welcome(
        LoginWelcomeComponentMock(
            initialState = LoginWelcomeScreenState.Content(
                availableAuthProviders = AvailableAuthProviders(
                    primary = listOf(UserAuthProvider.EMAIL),
                    secondary = emptyList()
                )
            )
        )
    )
) : ClientLoginRootComponent {

    override val stack: Value<ChildStack<ClientLoginDestination, ClientLoginRootComponent.Child>> = MutableValue(
        ChildStack(
            configuration = ClientLoginDestination.Welcome,
            instance = initialChild
        )
    )

    override fun onDismiss() {}
}
