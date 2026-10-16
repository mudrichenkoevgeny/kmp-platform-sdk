package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.navigate
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.popTo
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackDispatcher
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.detail.AuditEventDetailComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.list.AuditEventListComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.identifier.globallist.GlobalIdentifierListComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.identifier.userlist.UserIdentifierListComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.main.MainManagementComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.globallist.GlobalSessionListComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.userlist.UserSessionListComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.auth.EditAuthSettingsComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.global.EditGlobalSettingsComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.main.SettingsMainComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.security.EditSecuritySettingsComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.create.CreateUserComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.detail.UserDetailComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.globallist.GlobalUserListComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.audit.GetAuditEventUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.audit.GetAuditEventsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings.GetManagementAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings.ResetRemoteAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings.SaveRemoteAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.globalsettings.GetManagementGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.globalsettings.ResetRemoteGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.globalsettings.SaveRemoteGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier.ManagementDeleteIdentifierUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier.ManagementGetIdentifierUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier.ManagementGetIdentifiersUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier.ManagementGetUserIdentifiersUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.security.settings.GetManagementSecuritySettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.security.settings.ResetRemoteSecuritySettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.security.settings.SaveRemoteSecuritySettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementDeleteAllUserSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementDeleteSessionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementGetSessionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementGetSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementGetUserSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.CreateUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.DeleteUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.GetUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.GetUsersUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.UpdateUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.security.ManagementDisableTotpUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.UserRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.detail.IdentifierDetailComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.detail.SessionDetailComponentImpl
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.navigation.route.management.ManagementAuditNavigation
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.navigation.screen.management.audit.detail.ManagementAuditEventDetailParams
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.navigation.screen.management.audit.list.ManagementAuditEventListParams
import io.github.mudrichenkoevgeny.shared.foundation.core.common.navigation.route.NavigationRoute
import io.github.mudrichenkoevgeny.shared.foundation.core.common.navigation.route.management.ManagementMainNavigation
import io.github.mudrichenkoevgeny.shared.foundation.core.security.navigation.route.management.ManagementSecurityNavigation
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.navigation.route.management.ManagementSettingsNavigation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.route.management.ManagementAuthSettingsNavigation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.route.management.ManagementUsersNavigation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.user.identifier.detail.IdentifierDetailParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.user.session.detail.SessionDetailParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.identifier.userlist.ManagementUserIdentifierListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.session.userlist.ManagementUserSessionListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.user.detail.UserDetailParams
import kotlinx.serialization.PolymorphicSerializer

/**
 * Default implementation of [ManagementRootComponent].
 */
class ManagementRootComponentImpl(
    componentContext: ComponentContext,
    private val userRepository: UserRepository,
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
    private val managementDisableTotpUseCase: ManagementDisableTotpUseCase,
    private val managementGetSessionsUseCase: ManagementGetSessionsUseCase,
    private val managementGetUserSessionsUseCase: ManagementGetUserSessionsUseCase,
    private val managementDeleteSessionUseCase: ManagementDeleteSessionUseCase,
    private val managementDeleteAllUserSessionsUseCase: ManagementDeleteAllUserSessionsUseCase,
    private val managementGetIdentifiersUseCase: ManagementGetIdentifiersUseCase,
    private val managementGetUserIdentifiersUseCase: ManagementGetUserIdentifiersUseCase,
    private val getAuditEventsUseCase: GetAuditEventsUseCase,
    private val getAuditEventUseCase: GetAuditEventUseCase,
    private val managementGetSessionUseCase: ManagementGetSessionUseCase? = null,
    private val managementGetIdentifierUseCase: ManagementGetIdentifierUseCase? = null,
    private val managementDeleteIdentifierUseCase: ManagementDeleteIdentifierUseCase? = null,
    private val onNavigateToProfile: () -> Unit = {}
) : ManagementRootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<NavigationRoute>()

    override val stack: Value<ChildStack<NavigationRoute, ManagementRootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = PolymorphicSerializer(NavigationRoute::class),
            initialConfiguration = ManagementMainNavigation.Main,
            handleBackButton = true,
            childFactory = ::createChild
        )

    override fun navigateTo(destination: NavigationRoute) {
        navigation.bringToFront(destination)
    }

    private fun handleBack(parentDestination: NavigationRoute? = ManagementMainNavigation.Main) {
        if ((backHandler as? BackDispatcher)?.back() == true) {
            return
        }
        val items = stack.value.items
        val previousConfig = if (items.size >= 2) items[items.size - 2].configuration else null

        if (previousConfig != null && (parentDestination == null || previousConfig == parentDestination)) {
            navigation.pop()
        } else if (parentDestination != null && parentDestination != stack.value.active.configuration) {
            navigation.navigate { currentStack ->
                currentStack.dropLast(1) + parentDestination
            }
        }
    }

    @OptIn(DelicateDecomposeApi::class)
    private fun createChild(
        config: NavigationRoute,
        context: ComponentContext
    ): ManagementRootComponent.Child = when (config) {
        ManagementMainNavigation.Main -> ManagementRootComponent.Child.Main(
            MainManagementComponentImpl(
                componentContext = context,
                onNavigateToGlobalUserList = { navigation.bringToFront(ManagementUsersNavigation.UserList()) },
                onNavigateToGlobalSessionList = { navigation.bringToFront(ManagementUsersNavigation.GlobalSessionList()) },
                onNavigateToGlobalIdentifierList = { navigation.bringToFront(ManagementUsersNavigation.GlobalIdentifierList()) },
                onNavigateToAuditEventList = { navigation.bringToFront(ManagementAuditNavigation.AuditEventList()) },
                onNavigateToSettings = { navigation.bringToFront(ManagementSettingsNavigation.Main) }
            )
        )
        ManagementSettingsNavigation.Main -> ManagementRootComponent.Child.SettingsMain(
            SettingsMainComponentImpl(
                componentContext = context,
                onNavigateToEditAuthSettings = { navigation.bringToFront(ManagementAuthSettingsNavigation.AuthSettings) },
                onNavigateToEditGlobalSettings = { navigation.bringToFront(ManagementSettingsNavigation.GlobalSettings) },
                onNavigateToEditSecuritySettings = { navigation.bringToFront(ManagementSecurityNavigation.SecuritySettings) },
                onBack = { handleBack() }
            )
        )
        ManagementAuthSettingsNavigation.AuthSettings -> ManagementRootComponent.Child.EditAuthSettings(
            EditAuthSettingsComponentImpl(
                componentContext = context,
                getManagementAuthSettingsUseCase = getManagementAuthSettingsUseCase,
                saveRemoteAuthSettingsUseCase = saveRemoteAuthSettingsUseCase,
                resetRemoteAuthSettingsUseCase = resetRemoteAuthSettingsUseCase,
                onBack = { handleBack(ManagementSettingsNavigation.Main) }
            )
        )
        ManagementSettingsNavigation.GlobalSettings -> ManagementRootComponent.Child.EditGlobalSettings(
            EditGlobalSettingsComponentImpl(
                componentContext = context,
                getManagementGlobalSettingsUseCase = getManagementGlobalSettingsUseCase,
                saveRemoteGlobalSettingsUseCase = saveRemoteGlobalSettingsUseCase,
                resetRemoteGlobalSettingsUseCase = resetRemoteGlobalSettingsUseCase,
                onBack = { handleBack(ManagementSettingsNavigation.Main) }
            )
        )
        ManagementSecurityNavigation.SecuritySettings -> ManagementRootComponent.Child.EditSecuritySettings(
            EditSecuritySettingsComponentImpl(
                componentContext = context,
                getManagementSecuritySettingsUseCase = getManagementSecuritySettingsUseCase,
                saveRemoteSecuritySettingsUseCase = saveRemoteSecuritySettingsUseCase,
                resetRemoteSecuritySettingsUseCase = resetRemoteSecuritySettingsUseCase,
                onBack = { handleBack(ManagementSettingsNavigation.Main) }
            )
        )
        is ManagementUsersNavigation.UserList -> ManagementRootComponent.Child.GlobalUserList(
            GlobalUserListComponentImpl(
                componentContext = context,
                params = config.params,
                getUsersUseCase = getUsersUseCase,
                onNavigateToUserDetail = { userId ->
                    navigation.bringToFront(ManagementUsersNavigation.UserDetail(UserDetailParams(userId = userId.asHexDashString())))
                },
                onNavigateToCreateUser = {
                    navigation.bringToFront(ManagementUsersNavigation.CreateUser)
                },
                onBack = { handleBack() }
            )
        )
        is ManagementUsersNavigation.UserDetail -> ManagementRootComponent.Child.UserDetail(
            UserDetailComponentImpl(
                componentContext = context,
                params = config.params,
                getUserUseCase = getUserUseCase,
                updateUserUseCase = updateUserUseCase,
                deleteUserUseCase = deleteUserUseCase,
                managementDisableTotpUseCase = managementDisableTotpUseCase,
                onNavigateToSessions = { userId ->
                    navigation.bringToFront(ManagementUsersNavigation.UserSessionList(ManagementUserSessionListParams(userId = userId.asHexDashString())))
                },
                onNavigateToIdentifiers = { userId ->
                    navigation.bringToFront(ManagementUsersNavigation.UserIdentifierList(ManagementUserIdentifierListParams(userId = userId.asHexDashString())))
                },
                onNavigateToAuditEvents = { userId ->
                    navigation.bringToFront(
                        ManagementAuditNavigation.AuditEventList(
                            params = ManagementAuditEventListParams(
                                actorIds = listOf(userId.asHexDashString())
                            )
                        )
                    )
                },
                onBack = { handleBack(ManagementUsersNavigation.UserList()) }
            )
        )
        ManagementUsersNavigation.CreateUser -> ManagementRootComponent.Child.CreateUser(
            CreateUserComponentImpl(
                componentContext = context,
                createUserUseCase = createUserUseCase,
                onSuccess = navigation::pop,
                onBack = { handleBack(ManagementUsersNavigation.UserList()) }
            )
        )
        is ManagementUsersNavigation.UserSessionList -> ManagementRootComponent.Child.UserSessionList(
            UserSessionListComponentImpl(
                componentContext = context,
                params = config.params,
                managementGetUserSessionsUseCase = managementGetUserSessionsUseCase,
                managementDeleteSessionUseCase = managementDeleteSessionUseCase,
                managementDeleteAllUserSessionsUseCase = managementDeleteAllUserSessionsUseCase,
                onNavigateToSessionDetail = { session ->
                    navigation.bringToFront(ManagementUsersNavigation.SessionDetail(SessionDetailParams(sessionId = session.id.asHexDashString())))
                },
                onBack = { handleBack(ManagementUsersNavigation.UserDetail(UserDetailParams(userId = config.params.userId))) }
            )
        )
        is ManagementUsersNavigation.UserIdentifierList -> ManagementRootComponent.Child.UserIdentifierList(
            UserIdentifierListComponentImpl(
                componentContext = context,
                params = config.params,
                managementGetUserIdentifiersUseCase = managementGetUserIdentifiersUseCase,
                onIdentifierSelect = { identifierId ->
                    navigation.bringToFront(ManagementUsersNavigation.IdentifierDetail(IdentifierDetailParams(identifierId = identifierId)))
                },
                onBack = { handleBack(ManagementUsersNavigation.UserDetail(UserDetailParams(userId = config.params.userId))) }
            )
        )
        is ManagementAuditNavigation.AuditEventList -> ManagementRootComponent.Child.AuditEventList(
            AuditEventListComponentImpl(
                componentContext = context,
                params = config.params,
                getAuditEventsUseCase = getAuditEventsUseCase,
                onNavigateToEventDetail = { eventId ->
                    navigation.bringToFront(ManagementAuditNavigation.AuditEventDetail(ManagementAuditEventDetailParams(eventId = eventId.asHexDashString())))
                },
                onBack = { handleBack() }
            )
        )
        is ManagementAuditNavigation.AuditEventDetail -> ManagementRootComponent.Child.AuditEventDetail(
            AuditEventDetailComponentImpl(
                componentContext = context,
                params = config.params,
                userRepository = userRepository,
                getAuditEventUseCase = getAuditEventUseCase,
                onNavigateToUserDetail = { userId ->
                    navigation.bringToFront(ManagementUsersNavigation.UserDetail(UserDetailParams(userId = userId.asHexDashString())))
                },
                onNavigateToSessionDetail = { sessionId ->
                    navigation.bringToFront(ManagementUsersNavigation.SessionDetail(SessionDetailParams(sessionId = sessionId.asHexDashString())))
                },
                onNavigateToIdentifierDetail = { identifierId ->
                    navigation.bringToFront(ManagementUsersNavigation.IdentifierDetail(IdentifierDetailParams(identifierId = identifierId.asHexDashString())))
                },
                onNavigateToProfile = onNavigateToProfile,
                onBack = { handleBack(ManagementAuditNavigation.AuditEventList()) }
            )
        )
        is ManagementUsersNavigation.GlobalSessionList -> ManagementRootComponent.Child.GlobalSessionList(
            GlobalSessionListComponentImpl(
                componentContext = context,
                params = config.params,
                managementGetSessionsUseCase = managementGetSessionsUseCase,
                managementDeleteSessionUseCase = managementDeleteSessionUseCase,
                onNavigateToSessionDetail = { session ->
                    navigation.bringToFront(ManagementUsersNavigation.SessionDetail(SessionDetailParams(sessionId = session.id.asHexDashString())))
                },
                onBack = { handleBack() }
            )
        )
        is ManagementUsersNavigation.SessionDetail -> ManagementRootComponent.Child.SessionDetail(
            SessionDetailComponentImpl(
                componentContext = context,
                params = config.params,
                userRepository = userRepository,
                fetchSession = managementGetSessionUseCase?.let { useCase ->
                    { targetId -> useCase(targetId.asHexDashString()) }
                },
                revokeSession = if (managementGetSessionUseCase != null) {
                    { targetId ->
                        when (val sessionRes = managementGetSessionUseCase.invoke(targetId.asHexDashString())) {
                            is AppResult.Success -> managementDeleteSessionUseCase.invoke(sessionRes.data.userId, targetId.asHexDashString())
                            is AppResult.Error -> sessionRes
                        }
                    }
                } else null,
                onSessionRevoked = { },
                onNavigateToIdentifierDetail = { identifierId ->
                    navigation.bringToFront(ManagementUsersNavigation.IdentifierDetail(IdentifierDetailParams(identifierId = identifierId.asHexDashString())))
                },
                onNavigateToUserDetail = { userId ->
                    navigation.bringToFront(ManagementUsersNavigation.UserDetail(UserDetailParams(userId = userId.asHexDashString())))
                },
                onNavigateToProfile = { navigation.popTo(0) },
                onBack = { handleBack(ManagementUsersNavigation.GlobalSessionList()) }
            )
        )
        is ManagementUsersNavigation.GlobalIdentifierList -> ManagementRootComponent.Child.GlobalIdentifierList(
            GlobalIdentifierListComponentImpl(
                componentContext = context,
                params = config.params,
                managementGetIdentifiersUseCase = managementGetIdentifiersUseCase,
                onIdentifierSelect = { identifierId ->
                    navigation.bringToFront(ManagementUsersNavigation.IdentifierDetail(IdentifierDetailParams(identifierId = identifierId)))
                },
                onBack = { handleBack() }
            )
        )
        is ManagementUsersNavigation.IdentifierDetail -> ManagementRootComponent.Child.IdentifierDetail(
            IdentifierDetailComponentImpl(
                componentContext = context,
                params = config.params,
                userRepository = userRepository,
                fetchIdentifier = managementGetIdentifierUseCase?.let { useCase ->
                    { targetId -> useCase(targetId.asHexDashString()) }
                },
                deleteIdentifier = if (managementGetIdentifierUseCase != null && managementDeleteIdentifierUseCase != null) {
                    { targetId ->
                        when (val idRes = managementGetIdentifierUseCase.invoke(targetId.asHexDashString())) {
                            is AppResult.Success -> managementDeleteIdentifierUseCase.invoke(idRes.data.userId, targetId.asHexDashString())
                            is AppResult.Error -> idRes
                        }
                    }
                } else null,
                onIdentifierDeleted = { },
                onNavigateToUserDetail = { userId ->
                    navigation.bringToFront(ManagementUsersNavigation.UserDetail(UserDetailParams(userId = userId.asHexDashString())))
                },
                onNavigateToProfile = { navigation.popTo(0) },
                onBack = { handleBack(ManagementUsersNavigation.GlobalIdentifierList()) }
            )
        )
        else -> ManagementRootComponent.Child.Main(
            MainManagementComponentImpl(
                componentContext = context,
                onNavigateToGlobalUserList = { navigation.bringToFront(ManagementUsersNavigation.UserList()) },
                onNavigateToGlobalSessionList = { navigation.bringToFront(ManagementUsersNavigation.GlobalSessionList()) },
                onNavigateToGlobalIdentifierList = { navigation.bringToFront(ManagementUsersNavigation.GlobalIdentifierList()) },
                onNavigateToAuditEventList = { navigation.bringToFront(ManagementAuditNavigation.AuditEventList()) },
                onNavigateToSettings = { navigation.bringToFront(ManagementSettingsNavigation.Main) }
            )
        )
    }
}
