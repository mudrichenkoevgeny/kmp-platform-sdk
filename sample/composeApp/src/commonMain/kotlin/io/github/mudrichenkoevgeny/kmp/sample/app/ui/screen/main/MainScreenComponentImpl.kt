package io.github.mudrichenkoevgeny.kmp.sample.app.ui.screen.main

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
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.root.LoginRootComponent
import io.github.mudrichenkoevgeny.kmp.sample.app.di.AppComponent
import io.github.mudrichenkoevgeny.kmp.sample.app.ui.screen.home.HomeScreenComponentImpl
import io.github.mudrichenkoevgeny.kmp.sample.app.ui.screen.profile.ProfileScreenComponentImpl

/**
 * Default [MainScreenComponent]: stack navigation for home and profile, slot for [LoginRootComponent].
 *
 * @param componentContext Decompose lifecycle context.
 * @param appComponent Host graph used to build feature components and dialogs.
 */
class MainScreenComponentImpl(
    componentContext: ComponentContext,
    private val appComponent: AppComponent
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

    override val loginDialogSlot: Value<ChildSlot<MainScreenComponent.DialogConfig, LoginRootComponent>> = childSlot(
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
                        userRepository = appComponent.userComponent.userRepository,
                        onLoginDialogRequest = { onShowLogin() }
                    )
                )
            }
        }

    private fun createDialogChild(
        config: MainScreenComponent.DialogConfig,
        context: ComponentContext
    ): LoginRootComponent =
        when (config) {
            is MainScreenComponent.DialogConfig.Login -> {
                appComponent.userComponent.createLoginRootDialogComponent(
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