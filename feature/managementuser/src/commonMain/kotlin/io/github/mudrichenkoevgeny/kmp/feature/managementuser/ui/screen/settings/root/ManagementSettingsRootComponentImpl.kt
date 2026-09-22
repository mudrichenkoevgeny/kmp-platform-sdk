package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.ui.screen.root.AuditApiRootComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.usecase.GetAuditEventUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.usecase.GetAuditEventsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.root.UsersManagementRootComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.auth.EditAuthSettingsComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.global.EditGlobalSettingsComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.main.MainManagementSettingsComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.security.EditSecuritySettingsComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings.GetManagementAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings.ResetRemoteAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings.SaveRemoteAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.globalsettings.GetManagementGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.globalsettings.ResetRemoteGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.globalsettings.SaveRemoteGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.security.settings.GetManagementSecuritySettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.security.settings.ResetRemoteSecuritySettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.security.settings.SaveRemoteSecuritySettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier.ManagementDeleteIdentifierUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier.ManagementDeleteIdentifierPasswordUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier.ManagementGetIdentifiersUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementDeleteAllUserSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementDeleteSessionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementGetSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.security.ManagementDisableTotpUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.CreateUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.DeleteUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.GetUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.GetUsersUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.UpdateUserUseCase
import com.arkivanov.decompose.DelicateDecomposeApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.ManagementSettingsDestination

/**
 * Default implementation of [ManagementSettingsRootComponent].
 */
@OptIn(DelicateDecomposeApi::class)
class ManagementSettingsRootComponentImpl(
    componentContext: ComponentContext,
    private val getManagementAuthSettingsUseCase: GetManagementAuthSettingsUseCase,
    private val saveRemoteAuthSettingsUseCase: SaveRemoteAuthSettingsUseCase,
    private val resetRemoteAuthSettingsUseCase: ResetRemoteAuthSettingsUseCase,
    private val getManagementGlobalSettingsUseCase: GetManagementGlobalSettingsUseCase,
    private val saveRemoteGlobalSettingsUseCase: SaveRemoteGlobalSettingsUseCase,
    private val resetRemoteGlobalSettingsUseCase: ResetRemoteGlobalSettingsUseCase,
    private val getManagementSecuritySettingsUseCase: GetManagementSecuritySettingsUseCase,
    private val saveRemoteSecuritySettingsUseCase: SaveRemoteSecuritySettingsUseCase,
    private val resetRemoteSecuritySettingsUseCase: ResetRemoteSecuritySettingsUseCase,
    private val getUsersUseCase: GetUsersUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val createUserUseCase: CreateUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val managementGetSessionsUseCase: ManagementGetSessionsUseCase,
    private val managementGetIdentifiersUseCase: ManagementGetIdentifiersUseCase,
    private val managementDisableTotpUseCase: ManagementDisableTotpUseCase,
    private val managementDeleteSessionUseCase: ManagementDeleteSessionUseCase,
    private val managementDeleteAllUserSessionsUseCase: ManagementDeleteAllUserSessionsUseCase,
    private val managementDeleteIdentifierUseCase: ManagementDeleteIdentifierUseCase,
    private val managementDeleteIdentifierPasswordUseCase: ManagementDeleteIdentifierPasswordUseCase,
    private val getAuditEventsUseCase: GetAuditEventsUseCase,
    private val getAuditEventUseCase: GetAuditEventUseCase
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
                onNavigateToEditAuthSettings = { navigation.bringToFront(
                    ManagementSettingsDestination.EditAuthSettings) },
                onNavigateToEditGlobalSettings = { navigation.bringToFront(
                    ManagementSettingsDestination.EditGlobalSettings) },
                onNavigateToEditSecuritySettings = { navigation.bringToFront(
                    ManagementSettingsDestination.EditSecuritySettings) },
                onNavigateToUsersManagement = { navigation.bringToFront(
                    ManagementSettingsDestination.UsersManagement) },
                onNavigateToAuditLogs = { navigation.bringToFront(ManagementSettingsDestination.AuditLogs) }
            )
        )
        is ManagementSettingsDestination.EditAuthSettings -> ManagementSettingsRootComponent.Child.EditAuthSettings(
            EditAuthSettingsComponentImpl(
                componentContext = context,
                getManagementAuthSettingsUseCase = getManagementAuthSettingsUseCase,
                saveRemoteAuthSettingsUseCase = saveRemoteAuthSettingsUseCase,
                resetRemoteAuthSettingsUseCase = resetRemoteAuthSettingsUseCase,
                onBack = navigation::pop
            )
        )
        is ManagementSettingsDestination.EditGlobalSettings -> ManagementSettingsRootComponent.Child.EditGlobalSettings(
            EditGlobalSettingsComponentImpl(
                componentContext = context,
                getManagementGlobalSettingsUseCase = getManagementGlobalSettingsUseCase,
                saveRemoteGlobalSettingsUseCase = saveRemoteGlobalSettingsUseCase,
                resetRemoteGlobalSettingsUseCase = resetRemoteGlobalSettingsUseCase,
                onBack = navigation::pop
            )
        )
        is ManagementSettingsDestination.EditSecuritySettings -> ManagementSettingsRootComponent.Child.EditSecuritySettings(
            EditSecuritySettingsComponentImpl(
                componentContext = context,
                getManagementSecuritySettingsUseCase = getManagementSecuritySettingsUseCase,
                saveRemoteSecuritySettingsUseCase = saveRemoteSecuritySettingsUseCase,
                resetRemoteSecuritySettingsUseCase = resetRemoteSecuritySettingsUseCase,
                onBack = navigation::pop
            )
        )
        is ManagementSettingsDestination.UsersManagement -> ManagementSettingsRootComponent.Child.UsersManagement(
            UsersManagementRootComponentImpl(
                componentContext = context,
                getUsersUseCase = getUsersUseCase,
                getUserUseCase = getUserUseCase,
                createUserUseCase = createUserUseCase,
                updateUserUseCase = updateUserUseCase,
                deleteUserUseCase = deleteUserUseCase,
                managementGetSessionsUseCase = managementGetSessionsUseCase,
                managementGetIdentifiersUseCase = managementGetIdentifiersUseCase,
                managementDisableTotpUseCase = managementDisableTotpUseCase,
                managementDeleteSessionUseCase = managementDeleteSessionUseCase,
                managementDeleteAllUserSessionsUseCase = managementDeleteAllUserSessionsUseCase,
                managementDeleteIdentifierUseCase = managementDeleteIdentifierUseCase,
                managementDeleteIdentifierPasswordUseCase = managementDeleteIdentifierPasswordUseCase,
                onBack = navigation::pop
            )
        )
        is ManagementSettingsDestination.AuditLogs -> ManagementSettingsRootComponent.Child.AuditLogs(
            AuditApiRootComponentImpl(
                componentContext = context,
                getAuditEventsUseCase = getAuditEventsUseCase,
                getAuditEventUseCase = getAuditEventUseCase,
                onBack = navigation::pop
            )
        )
    }
}
