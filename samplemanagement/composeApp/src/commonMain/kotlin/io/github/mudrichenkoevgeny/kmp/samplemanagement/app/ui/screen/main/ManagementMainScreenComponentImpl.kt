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
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.auth.login.root.ManagementLoginRootComponent
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.di.ManagementAppComponent
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.ui.screen.home.HomeScreenComponentImpl
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.ui.screen.profile.ProfileScreenComponentImpl

/**
 * Default [MainScreenComponent]: stack navigation for home and profile, slot for [ManagementLoginRootComponent].
 *
 * @param componentContext Decompose lifecycle context.
 * @param managementAppComponent Host graph used to build feature components and dialogs.
 */
class ManagementMainScreenComponentImpl(
    componentContext: ComponentContext,
    private val managementAppComponent: ManagementAppComponent
) : MainScreenComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<MainScreenComponent.Config>()
    private val dialogSlotNavigation = SlotNavigation<MainScreenComponent.DialogConfig>()

    override val stack: Value<ChildStack<MainScreenComponent.Config, MainScreenComponent.Child>> =
        childStack(
            source = navigation,
            serializer = null,
            initialConfiguration = MainScreenComponent.Config.Home,
            handleBackButton = true,
            childFactory = ::createChild
        )

    override val loginDialogSlot: Value<ChildSlot<MainScreenComponent.DialogConfig, ManagementLoginRootComponent>> = childSlot(
        source = dialogSlotNavigation,
        serializer = MainScreenComponent.DialogConfig.serializer(),
        handleBackButton = true,
        childFactory = ::createDialogChild
    )

    private fun createChild(
        config: MainScreenComponent.Config,
        context: ComponentContext
    ): MainScreenComponent.Child =
        when (config) {
            is MainScreenComponent.Config.Home -> {
                MainScreenComponent.Child.HomeChild(
                    HomeScreenComponentImpl(context)
                )
            }
            is MainScreenComponent.Config.Profile -> {
                MainScreenComponent.Child.ProfileChild(
                    ProfileScreenComponentImpl(
                        componentContext = context,
                        userRepository = managementAppComponent.managementUserComponent.userRepository,
                        onLoginDialogRequest = { onShowLogin() }
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
                    onFinished = { onDismissLogin() }
                )
            }
        }

    override fun onTabClick(config: MainScreenComponent.Config) {
        navigation.bringToFront(config)
    }

    override fun onShowLogin() {
        dialogSlotNavigation.activate(MainScreenComponent.DialogConfig.Login)
    }

    override fun onDismissLogin() {
        dialogSlotNavigation.dismiss()
    }
}