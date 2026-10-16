package io.github.mudrichenkoevgeny.kmp.samplemanagement.app.ui.screen.main

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
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.auth.login.root.ManagementLoginRootComponent
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.di.ManagementAppComponent
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.navigation.deeplink.ManagementAppDeepLinkRoute
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

/**
 * Default [MainScreenComponent]: stack navigation for user and management, slot for [ManagementLoginRootComponent].
 *
 * @param componentContext Decompose lifecycle context.
 * @param managementAppComponent Host graph used to build feature components and dialogs.
 */
class ManagementMainScreenComponentImpl(
    componentContext: ComponentContext,
    private val managementAppComponent: ManagementAppComponent
) : MainScreenComponent, ComponentContext by componentContext {

    private val userRepository = managementAppComponent.managementUserComponent.userRepository

    override val isAuthorized: Value<Boolean> = userRepository.currentUser
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
            initialConfiguration = MainScreenComponent.Config.Management,
            handleBackButton = false,
            childFactory = ::createChild
        )

    override val loginDialogSlot: Value<ChildSlot<MainScreenComponent.DialogConfig, ManagementLoginRootComponent>> = childSlot(
        source = dialogSlotNavigation,
        serializer = MainScreenComponent.DialogConfig.serializer(),
        handleBackButton = true,
        childFactory = ::createDialogChild
    )

    private var pendingRoute: ManagementAppDeepLinkRoute? = null

    init {
        userRepository.currentUser
            .onEach { user ->
                if (user != null) {
                    dialogSlotNavigation.dismiss()
                } else {
                    if (loginDialogSlot.value.child?.instance == null) {
                        dialogSlotNavigation.activate(MainScreenComponent.DialogConfig.Login(isDismissible = false))
                    }
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
                    managementAppComponent.managementUserComponent.createUserComponent(
                        componentContext = context,
                        onNavigateToLogin = ::onShowLogin
                    )
                )
            }
            is MainScreenComponent.Config.Management -> {
                MainScreenComponent.Child.ManagementChild(
                    managementAppComponent.managementUserComponent.createManagementComponent(
                        componentContext = context,
                        onNavigateToProfile = {
                            navigation.bringToFront(MainScreenComponent.Config.User)
                        }
                    )
                )
            }
        }

    private fun createDialogChild(
        config: MainScreenComponent.DialogConfig,
        context: ComponentContext
    ): ManagementLoginRootComponent =
        when (config) {
            is MainScreenComponent.DialogConfig.Login -> {
                managementAppComponent.managementUserComponent.createLoginRootDialogComponent(
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

    override fun handleDeepLink(route: ManagementAppDeepLinkRoute) {
        if (route.requiresAuth && !isAuthorized.value) {
            pendingRoute = route
            dialogSlotNavigation.activate(
                MainScreenComponent.DialogConfig.Login(
                    isDismissible = false
                )
            )
        } else {
            dispatchRoute(route)
        }
    }

    private fun dispatchRoute(route: ManagementAppDeepLinkRoute) {
        navigation.bringToFront(route.tab)
        val targetChild = stack.value.items.find { it.configuration == route.tab }?.instance
        when (route) {
            is ManagementAppDeepLinkRoute.Management -> {
                if (targetChild is MainScreenComponent.Child.ManagementChild) {
                    targetChild.component.navigateTo(route.destination)
                }
            }
            is ManagementAppDeepLinkRoute.User -> {
                if (targetChild is MainScreenComponent.Child.UserChild) {
                    targetChild.component.navigateTo(route.destination)
                }
            }
        }
    }

    override fun onTabClick(config: MainScreenComponent.Config) {
        if ((config is MainScreenComponent.Config.Management) && (!isAuthorized.value)) {
            dialogSlotNavigation.activate(MainScreenComponent.DialogConfig.Login(isDismissible = false))
            return
        }
        navigation.bringToFront(config)
    }

    override fun onShowLogin() {
        dialogSlotNavigation.activate(MainScreenComponent.DialogConfig.Login(isDismissible = true))
    }

    override fun onDismissLogin() {
        val currentDialog = loginDialogSlot.value.child?.configuration as? MainScreenComponent.DialogConfig.Login
        if (currentDialog?.isDismissible == false && !isAuthorized.value) {
            return
        }
        dialogSlotNavigation.dismiss()
    }
}
