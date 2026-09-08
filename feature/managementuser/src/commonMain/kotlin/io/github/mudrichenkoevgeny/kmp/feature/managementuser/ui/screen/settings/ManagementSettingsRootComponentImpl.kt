package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.auth.EditAuthSettingsComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.global.EditGlobalSettingsComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.main.MainManagementSettingsComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.security.EditSecuritySettingsComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings.GetManagementAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings.SaveRemoteAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.globalsettings.GetManagementGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.globalsettings.SaveRemoteGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.security.settings.GetManagementSecuritySettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.security.settings.SaveRemoteSecuritySettingsUseCase

import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.pushToFront

/**
 * Default implementation of [ManagementSettingsRootComponent].
 */
@OptIn(DelicateDecomposeApi::class)
class ManagementSettingsRootComponentImpl(
    componentContext: ComponentContext,
    private val getManagementAuthSettingsUseCase: GetManagementAuthSettingsUseCase,
    private val saveRemoteAuthSettingsUseCase: SaveRemoteAuthSettingsUseCase,
    private val getManagementGlobalSettingsUseCase: GetManagementGlobalSettingsUseCase,
    private val saveRemoteGlobalSettingsUseCase: SaveRemoteGlobalSettingsUseCase,
    private val getManagementSecuritySettingsUseCase: GetManagementSecuritySettingsUseCase,
    private val saveRemoteSecuritySettingsUseCase: SaveRemoteSecuritySettingsUseCase
) : ManagementSettingsRootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<ManagementSettingsDestination>()

    override val stack: Value<ChildStack<ManagementSettingsDestination, ManagementSettingsRootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = ManagementSettingsDestination.serializer(),
            initialConfiguration = ManagementSettingsDestination.Main,
            handleBackButton = true,
            childFactory = ::createChild
        )

    private fun createChild(
        config: ManagementSettingsDestination,
        context: ComponentContext
    ): ManagementSettingsRootComponent.Child = when (config) {
        is ManagementSettingsDestination.Main -> ManagementSettingsRootComponent.Child.Main(
            MainManagementSettingsComponentImpl(
                componentContext = context,
                onNavigateToEditAuthSettings = { navigation.push(ManagementSettingsDestination.EditAuthSettings) },
                onNavigateToEditGlobalSettings = { navigation.push(ManagementSettingsDestination.EditGlobalSettings) },
                onNavigateToEditSecuritySettings = { navigation.push(ManagementSettingsDestination.EditSecuritySettings) }
            )
        )
        is ManagementSettingsDestination.EditAuthSettings -> ManagementSettingsRootComponent.Child.EditAuthSettings(
            EditAuthSettingsComponentImpl(
                componentContext = context,
                getManagementAuthSettingsUseCase = getManagementAuthSettingsUseCase,
                saveRemoteAuthSettingsUseCase = saveRemoteAuthSettingsUseCase,
                onBack = navigation::pop
            )
        )
        is ManagementSettingsDestination.EditGlobalSettings -> ManagementSettingsRootComponent.Child.EditGlobalSettings(
            EditGlobalSettingsComponentImpl(
                componentContext = context,
                getManagementGlobalSettingsUseCase = getManagementGlobalSettingsUseCase,
                saveRemoteGlobalSettingsUseCase = saveRemoteGlobalSettingsUseCase,
                onBack = navigation::pop
            )
        )
        is ManagementSettingsDestination.EditSecuritySettings -> ManagementSettingsRootComponent.Child.EditSecuritySettings(
            EditSecuritySettingsComponentImpl(
                componentContext = context,
                getManagementSecuritySettingsUseCase = getManagementSecuritySettingsUseCase,
                saveRemoteSecuritySettingsUseCase = saveRemoteSecuritySettingsUseCase,
                onBack = navigation::pop
            )
        )
    }
}
