package io.github.mudrichenkoevgeny.kmp.sampleclient.app.ui.screen.main

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.asValue
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.root.ClientLoginRootComponent
import io.github.mudrichenkoevgeny.kmp.sampleclient.app.di.ClientAppComponent
import io.github.mudrichenkoevgeny.kmp.sampleclient.app.navigation.deeplink.ClientAppDeepLinkRoute
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

/**
 * Default [MainScreenComponent]: stack navigation for user flow and slot for [ClientLoginRootComponent].
 *
 * @param componentContext Decompose lifecycle context.
 * @param clientAppComponent Host graph used to build feature components and dialogs.
 */
class ClientMainScreenComponentImpl(
    componentContext: ComponentContext,
    private val clientAppComponent: ClientAppComponent
) : MainScreenComponent, ComponentContext by componentContext {

    private val userRepository = clientAppComponent.clientUserComponent.userRepository

    val isAuthorized: Value<Boolean> = userRepository.currentUser
        .map { user -> user != null }
        .asValue(
            initialValue = false,
            lifecycle = lifecycle
        )

    override val stackNavigation = StackNavigation<MainScreenComponent.Config>()
    private val navigation = stackNavigation
    private val dialogSlotNavigation = SlotNavigation<MainScreenComponent.DialogConfig>()

    override val stack: Value<ChildStack<MainScreenComponent.Config, MainScreenComponent.Child>> =
        childStack(
            source = navigation,
            serializer = null,
            initialConfiguration = MainScreenComponent.Config.User,
            handleBackButton = true,
            childFactory = ::createChild
        )

    override val loginDialogSlot: Value<ChildSlot<MainScreenComponent.DialogConfig, ClientLoginRootComponent>> = childSlot(
        source = dialogSlotNavigation,
        serializer = MainScreenComponent.DialogConfig.serializer(),
        handleBackButton = true,
        childFactory = ::createDialogChild
    )

    private var pendingRoute: ClientAppDeepLinkRoute? = null

    init {
        userRepository.currentUser
            .onEach { user ->
                if (user != null) {
                    dialogSlotNavigation.dismiss()
                }
            }
            .launchIn(componentCoroutineScope())
    }

    private fun createChild(
        config: MainScreenComponent.Config,
        context: ComponentContext
    ): MainScreenComponent.Child =
        when (config) {
            is MainScreenComponent.Config.User -> {
                MainScreenComponent.Child.UserChild(
                    clientAppComponent.clientUserComponent.createUserComponent(
                        componentContext = context,
                        onNavigateToLogin = { onShowLogin() }
                    )
                )
            }
        }

    private fun createDialogChild(
        config: MainScreenComponent.DialogConfig,
        context: ComponentContext
    ): ClientLoginRootComponent =
        when (config) {
            is MainScreenComponent.DialogConfig.Login -> {
                clientAppComponent.clientUserComponent.createLoginRootDialogComponent(
                    componentContext = context,
                    onFinished = {
                        onDismissLogin()
                        pendingRoute?.let { route ->
                            pendingRoute = null
                            dispatchRoute(route)
                        }
                    }
                )
            }
        }

    override fun handleDeepLink(route: ClientAppDeepLinkRoute) {
        if (route.requiresAuth && !isAuthorized.value) {
            pendingRoute = route
            onShowLogin()
        } else {
            dispatchRoute(route)
        }
    }

    private fun dispatchRoute(route: ClientAppDeepLinkRoute) {
        navigation.bringToFront(route.tab)
        val targetChild = stack.value.items.find { it.configuration == route.tab }?.instance
        when (route) {
            is ClientAppDeepLinkRoute.Login -> {
                dialogSlotNavigation.activate(MainScreenComponent.DialogConfig.Login())
                loginDialogSlot.value.child?.instance?.navigateTo(route.destination)
            }
            is ClientAppDeepLinkRoute.User -> {
                if (targetChild is MainScreenComponent.Child.UserChild) {
                    targetChild.component.navigateTo(route.destination)
                }
            }
        }
    }

    override fun onTabClick(config: MainScreenComponent.Config) {
        navigation.bringToFront(config)
    }

    override fun onShowLogin() {
        dialogSlotNavigation.activate(MainScreenComponent.DialogConfig.Login())
    }

    override fun onDismissLogin() {
        dialogSlotNavigation.dismiss()
    }
}
