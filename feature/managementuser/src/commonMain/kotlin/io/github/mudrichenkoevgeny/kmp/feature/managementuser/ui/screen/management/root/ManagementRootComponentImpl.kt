package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackDispatcher
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.navigation.deeplink.ManagementDeepLinkConstants
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.ManagementDestination
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.detail.AuditEventDetailComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.detail.AuditEventDetailParams
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.list.AuditEventListComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.list.AuditEventListParams
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.identifier.globallist.GlobalIdentifierListComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.identifier.userlist.UserIdentifierListComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.identifier.userlist.UserIdentifierListParams
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.main.MainManagementComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.globallist.GlobalSessionListComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.userlist.UserSessionListComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.userlist.UserSessionListParams
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.SettingsDialogConfig
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.auth.EditAuthSettingsComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.global.EditGlobalSettingsComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.security.EditSecuritySettingsComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.create.CreateUserComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.detail.UserDetailComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.detail.UserDetailParams
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.detail.IdentifierDetailParams
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.detail.SessionDetailParams
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.globallist.GlobalUserListComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.audit.GetAuditEventUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.audit.GetAuditEventsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings.GetManagementAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings.ResetRemoteAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings.SaveRemoteAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.globalsettings.GetManagementGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.globalsettings.ResetRemoteGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.globalsettings.SaveRemoteGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier.ManagementDeleteIdentifierPasswordUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier.ManagementDeleteIdentifierUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier.ManagementGetIdentifierUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier.ManagementGetIdentifiersUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.security.settings.GetManagementSecuritySettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.security.settings.ResetRemoteSecuritySettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.security.settings.SaveRemoteSecuritySettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementDeleteAllUserSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementDeleteSessionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementGetSessionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementGetSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.CreateUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.DeleteUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.GetUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.GetUsersUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.UpdateUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.security.ManagementDisableTotpUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.UserRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.detail.IdentifierDetailComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.list.notifyIdentifierDeleted
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.detail.SessionDetailComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.list.notifySessionRevoked

/**
 * Default implementation of [ManagementRootComponent].
 */
@OptIn(DelicateDecomposeApi::class)
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
    private val managementGetSessionsUseCase: ManagementGetSessionsUseCase,
    private val managementGetSessionUseCase: ManagementGetSessionUseCase? = null,
    private val managementGetIdentifiersUseCase: ManagementGetIdentifiersUseCase,
    private val managementGetIdentifierUseCase: ManagementGetIdentifierUseCase? = null,
    private val managementDisableTotpUseCase: ManagementDisableTotpUseCase,
    private val managementDeleteSessionUseCase: ManagementDeleteSessionUseCase,
    private val managementDeleteAllUserSessionsUseCase: ManagementDeleteAllUserSessionsUseCase,
    private val managementDeleteIdentifierUseCase: ManagementDeleteIdentifierUseCase,
    private val managementDeleteIdentifierPasswordUseCase: ManagementDeleteIdentifierPasswordUseCase,
    private val getAuditEventsUseCase: GetAuditEventsUseCase,
    private val getAuditEventUseCase: GetAuditEventUseCase,
    private val onNavigateToProfile: () -> Unit = {}
) : ManagementRootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<ManagementDestination>()

    override val stack: Value<ChildStack<ManagementDestination, ManagementRootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = ManagementDestination.serializer(),
            initialConfiguration = ManagementDestination.Main,
            handleBackButton = true,
            childFactory = ::createChild
        )

    override fun navigateTo(destination: ManagementDestination) {
        navigation.bringToFront(destination)
    }

    private fun createChild(
        config: ManagementDestination,
        context: ComponentContext
    ): ManagementRootComponent.Child = when (config) {
        is ManagementDestination.Main -> ManagementRootComponent.Child.Main(
            MainManagementComponentImpl(
                componentContext = context,
                onNavigateToEditAuthSettings = { navigation.bringToFront(ManagementDestination.EditAuthSettings()) },
                onNavigateToEditGlobalSettings = { navigation.bringToFront(ManagementDestination.EditGlobalSettings()) },
                onNavigateToEditSecuritySettings = { navigation.bringToFront(ManagementDestination.EditSecuritySettings()) },
                onNavigateToGlobalUserList = { navigation.bringToFront(ManagementDestination.GlobalUserList) },
                onNavigateToAuditEventList = { navigation.bringToFront(ManagementDestination.AuditEventList()) },
                onNavigateToGlobalSessionList = { navigation.bringToFront(ManagementDestination.GlobalSessionList) },
                onNavigateToGlobalIdentifierList = { navigation.bringToFront(ManagementDestination.GlobalIdentifierList) }
            )
        )
        is ManagementDestination.EditAuthSettings -> {
            val initialDialogConfig = when (config.params.dialog) {
                ManagementDeepLinkConstants.Value.DIALOG_RESET_CONFIRM -> SettingsDialogConfig.ConfirmReset
                else -> null
            }
            ManagementRootComponent.Child.EditAuthSettings(
                EditAuthSettingsComponentImpl(
                    componentContext = context,
                    initialDialogConfig = initialDialogConfig,
                    getManagementAuthSettingsUseCase = getManagementAuthSettingsUseCase,
                    saveRemoteAuthSettingsUseCase = saveRemoteAuthSettingsUseCase,
                    resetRemoteAuthSettingsUseCase = resetRemoteAuthSettingsUseCase,
                    onBack = { if ((backHandler as? BackDispatcher)?.back() != true) navigation.pop() }
                )
            )
        }
        is ManagementDestination.EditGlobalSettings -> {
            val initialDialogConfig = when (config.params.dialog) {
                ManagementDeepLinkConstants.Value.DIALOG_RESET_CONFIRM -> SettingsDialogConfig.ConfirmReset
                else -> null
            }
            ManagementRootComponent.Child.EditGlobalSettings(
                EditGlobalSettingsComponentImpl(
                    componentContext = context,
                    initialDialogConfig = initialDialogConfig,
                    getManagementGlobalSettingsUseCase = getManagementGlobalSettingsUseCase,
                    saveRemoteGlobalSettingsUseCase = saveRemoteGlobalSettingsUseCase,
                    resetRemoteGlobalSettingsUseCase = resetRemoteGlobalSettingsUseCase,
                    onBack = { if ((backHandler as? BackDispatcher)?.back() != true) navigation.pop() }
                )
            )
        }
        is ManagementDestination.EditSecuritySettings -> {
            val initialDialogConfig = when (config.params.dialog) {
                ManagementDeepLinkConstants.Value.DIALOG_RESET_CONFIRM -> SettingsDialogConfig.ConfirmReset
                else -> null
            }
            ManagementRootComponent.Child.EditSecuritySettings(
                EditSecuritySettingsComponentImpl(
                    componentContext = context,
                    initialDialogConfig = initialDialogConfig,
                    getManagementSecuritySettingsUseCase = getManagementSecuritySettingsUseCase,
                    saveRemoteSecuritySettingsUseCase = saveRemoteSecuritySettingsUseCase,
                    resetRemoteSecuritySettingsUseCase = resetRemoteSecuritySettingsUseCase,
                    onBack = { if ((backHandler as? BackDispatcher)?.back() != true) navigation.pop() }
                )
            )
        }
        is ManagementDestination.GlobalUserList -> ManagementRootComponent.Child.GlobalUserList(
            GlobalUserListComponentImpl(
                componentContext = context,
                getUsersUseCase = getUsersUseCase,
                onNavigateToUserDetail = { userId ->
                    navigation.bringToFront(ManagementDestination.UserDetail(UserDetailParams(userId.asHexDashString())))
                },
                onNavigateToCreateUser = {
                    navigation.bringToFront(ManagementDestination.CreateUser)
                },
                onBack = { if ((backHandler as? BackDispatcher)?.back() != true) navigation.pop() }
            )
        )
        is ManagementDestination.UserDetail -> ManagementRootComponent.Child.UserDetail(
            UserDetailComponentImpl(
                componentContext = context,
                params = config.params,
                getUserUseCase = getUserUseCase,
                updateUserUseCase = updateUserUseCase,
                deleteUserUseCase = deleteUserUseCase,
                managementDisableTotpUseCase = managementDisableTotpUseCase,
                onNavigateToSessions = { userId ->
                    navigation.bringToFront(ManagementDestination.UserSessionList(UserSessionListParams(userId.asHexDashString())))
                },
                onNavigateToIdentifiers = { userId ->
                    navigation.bringToFront(ManagementDestination.UserIdentifierList(UserIdentifierListParams(userId.asHexDashString())))
                },
                onNavigateToAuditEvents = { userId ->
                    navigation.bringToFront(
                        ManagementDestination.AuditEventList(
                            params = AuditEventListParams(
                                initialFilters = AuditEventListParams.Filters(
                                    actorIds = listOf(userId.value.toHexDashString())
                                )
                            )
                        )
                    )
                },
                onBack = { if ((backHandler as? BackDispatcher)?.back() != true) navigation.pop() }
            )
        )
        ManagementDestination.CreateUser -> ManagementRootComponent.Child.CreateUser(
            CreateUserComponentImpl(
                componentContext = context,
                createUserUseCase = createUserUseCase,
                onSuccess = navigation::pop,
                onBack = { if ((backHandler as? BackDispatcher)?.back() != true) navigation.pop() }
            )
        )
        is ManagementDestination.UserSessionList -> ManagementRootComponent.Child.UserSessionList(
            UserSessionListComponentImpl(
                componentContext = context,
                params = config.params,
                managementGetSessionsUseCase = managementGetSessionsUseCase,
                managementDeleteSessionUseCase = managementDeleteSessionUseCase,
                managementDeleteAllUserSessionsUseCase = managementDeleteAllUserSessionsUseCase,
                onNavigateToSessionDetail = { session ->
                    navigation.bringToFront(ManagementDestination.SessionDetail(SessionDetailParams(session.id.asHexDashString())))
                },
                onBack = { if ((backHandler as? BackDispatcher)?.back() != true) navigation.pop() }
            )
        )
        is ManagementDestination.UserIdentifierList -> ManagementRootComponent.Child.UserIdentifierList(
            UserIdentifierListComponentImpl(
                componentContext = context,
                params = config.params,
                managementGetIdentifiersUseCase = managementGetIdentifiersUseCase,
                onIdentifierSelect = { identifierId ->
                    navigation.bringToFront(ManagementDestination.IdentifierDetail(IdentifierDetailParams(identifierId)))
                },
                onBack = { if ((backHandler as? BackDispatcher)?.back() != true) navigation.pop() }
            )
        )
        is ManagementDestination.AuditEventList -> ManagementRootComponent.Child.AuditEventList(
            AuditEventListComponentImpl(
                componentContext = context,
                params = config.params,
                getAuditEventsUseCase = getAuditEventsUseCase,
                onNavigateToEventDetail = { eventId ->
                    navigation.bringToFront(ManagementDestination.AuditEventDetail(AuditEventDetailParams(eventId.asHexDashString())))
                },
                onBack = { if ((backHandler as? BackDispatcher)?.back() != true) navigation.pop() }
            )
        )
        is ManagementDestination.AuditEventDetail -> ManagementRootComponent.Child.AuditEventDetail(
            AuditEventDetailComponentImpl(
                componentContext = context,
                params = config.params,
                userRepository = userRepository,
                getAuditEventUseCase = getAuditEventUseCase,
                onNavigateToUserDetail = { userId ->
                    navigation.bringToFront(ManagementDestination.UserDetail(UserDetailParams(userId.asHexDashString())))
                },
                onNavigateToSessionDetail = { sessionId ->
                    navigation.bringToFront(ManagementDestination.SessionDetail(SessionDetailParams(sessionId.asHexDashString())))
                },
                onNavigateToIdentifierDetail = { identifierId ->
                    navigation.bringToFront(ManagementDestination.IdentifierDetail(IdentifierDetailParams(identifierId.asHexDashString())))
                },
                onNavigateToProfile = onNavigateToProfile,
                onBack = { if ((backHandler as? BackDispatcher)?.back() != true) navigation.pop() }
            )
        )
        is ManagementDestination.GlobalSessionList -> ManagementRootComponent.Child.GlobalSessionList(
            GlobalSessionListComponentImpl(
                componentContext = context,
                managementGetSessionsUseCase = managementGetSessionsUseCase,
                managementDeleteSessionUseCase = managementDeleteSessionUseCase,
                onNavigateToSessionDetail = { session ->
                    navigation.bringToFront(ManagementDestination.SessionDetail(SessionDetailParams(session.id.asHexDashString())))
                },
                onBack = { if ((backHandler as? BackDispatcher)?.back() != true) navigation.pop() }
            )
        )
        is ManagementDestination.SessionDetail -> ManagementRootComponent.Child.SessionDetail(
            SessionDetailComponentImpl(
                componentContext = context,
                params = config.params,
                userRepository = userRepository,
                fetchSession = managementGetSessionUseCase?.let { useCase ->
                    { targetId -> useCase(targetId.asHexDashString()) }
                },
                revokeSession = { targetId ->
                    if (managementGetSessionUseCase == null) return@SessionDetailComponentImpl AppResult.Error(CommonError.Unknown())

                    when (val sessionRes = managementGetSessionUseCase(targetId.asHexDashString())) {
                        is AppResult.Success -> {
                            managementDeleteSessionUseCase(sessionRes.data.userId, targetId.asHexDashString())
                        }
                        is AppResult.Error -> AppResult.Error(sessionRes.error)
                    }
                },
                onSessionRevoked = { stack.value.notifySessionRevoked(it) },
                onNavigateToIdentifierDetail = { identifierId ->
                    navigation.bringToFront(ManagementDestination.IdentifierDetail(IdentifierDetailParams(identifierId.asHexDashString())))
                },
                onNavigateToUserDetail = { userId ->
                    navigation.bringToFront(ManagementDestination.UserDetail(UserDetailParams(userId.asHexDashString())))
                },
                onNavigateToProfile = onNavigateToProfile,
                onBack = { if ((backHandler as? BackDispatcher)?.back() != true) navigation.pop() }
            )
        )
        is ManagementDestination.GlobalIdentifierList -> ManagementRootComponent.Child.GlobalIdentifierList(
            GlobalIdentifierListComponentImpl(
                componentContext = context,
                managementGetIdentifiersUseCase = managementGetIdentifiersUseCase,
                onIdentifierSelect = { identifierId ->
                    navigation.bringToFront(ManagementDestination.IdentifierDetail(IdentifierDetailParams(identifierId)))
                },
                onBack = { if ((backHandler as? BackDispatcher)?.back() != true) navigation.pop() }
            )
        )
        is ManagementDestination.IdentifierDetail -> ManagementRootComponent.Child.IdentifierDetail(
            IdentifierDetailComponentImpl(
                componentContext = context,
                params = config.params,
                userRepository = userRepository,
                fetchIdentifier = managementGetIdentifierUseCase?.let { useCase ->
                    { targetId -> useCase(targetId.asHexDashString()) }
                },
                deleteIdentifier = { targetId ->
                    if (managementGetIdentifierUseCase == null) return@IdentifierDetailComponentImpl AppResult.Error(CommonError.Unknown())

                    when (val idRes = managementGetIdentifierUseCase(targetId.asHexDashString())) {
                        is AppResult.Success -> managementDeleteIdentifierUseCase(idRes.data.userId, targetId.asHexDashString())
                        is AppResult.Error -> AppResult.Error(idRes.error)
                    }
                },
                deletePassword = { targetId ->
                    if (managementGetIdentifierUseCase == null) return@IdentifierDetailComponentImpl AppResult.Error(CommonError.Unknown())

                    when (val idRes = managementGetIdentifierUseCase(targetId.asHexDashString())) {
                        is AppResult.Success -> managementDeleteIdentifierPasswordUseCase(idRes.data.userId, targetId.asHexDashString())
                        is AppResult.Error -> AppResult.Error(idRes.error)
                    }
                },
                onIdentifierDeleted = { stack.value.notifyIdentifierDeleted(it) },
                onNavigateToUserDetail = { userId ->
                    navigation.bringToFront(ManagementDestination.UserDetail(UserDetailParams(userId.asHexDashString())))
                },
                onNavigateToProfile = onNavigateToProfile,
                onBack = { if ((backHandler as? BackDispatcher)?.back() != true) navigation.pop() }
            )
        )
    }
}