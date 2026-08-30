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
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.root.ClientLoginRootComponent
import io.github.mudrichenkoevgeny.kmp.sampleclient.app.di.ClientAppComponent
import io.github.mudrichenkoevgeny.kmp.sampleclient.app.ui.screen.home.HomeScreenComponentImpl
import io.github.mudrichenkoevgeny.kmp.sampleclient.app.ui.screen.profile.ProfileScreenComponentImpl

/**
 * Default [MainScreenComponent]: stack navigation for home and profile, slot for [ClientLoginRootComponent].
 *
 * @param componentContext Decompose lifecycle context.
 * @param clientAppComponent Host graph used to build feature components and dialogs.
 */
class ClientMainScreenComponentImpl(
    componentContext: ComponentContext,
    private val clientAppComponent: ClientAppComponent
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

    override val loginDialogSlot: Value<ChildSlot<MainScreenComponent.DialogConfig, ClientLoginRootComponent>> = childSlot(
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
                        userRepository = clientAppComponent.clientUserComponent.userRepository,
                        onLoginDialogRequest = { onShowLogin() }
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