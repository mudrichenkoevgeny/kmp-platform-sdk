package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.navigate
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackDispatcher
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.toUserSessionIdOrNull
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
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.settings.EditStorageSettingsComponentImpl
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.GetManagementStorageSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.ResetRemoteManagementStorageSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.UpdateManagementStorageSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.create.CreateUserComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.detail.UserDetailComponentImpl
import io.github.mudrichenkoevgeny.kmp.core.storage.di.ManagementStorageDependencies
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.detail.StorageFileDetailComponentImpl
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.detail.datasource.StorageFileDetailDataSource
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list.StorageFileListComponent
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list.factory.createManagementGlobalStorageFileListComponent
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list.factory.createStaffSelfStorageFileListComponent
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.upload.StorageFileUploadScreenComponentImpl
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.navigation.screen.base.detail.StorageFileDetailParams
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.ui.component.fileupload.StorageFileUploadUiConfig
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.navigation.route.management.ManagementSelfStorageNavigation
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.navigation.route.management.ManagementStorageNavigation
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.navigation.route.management.ManagementStorageSettingsNavigation
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.navigation.screen.management.list.ManagementStorageFileListParams
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.globallist.GlobalUserListComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.globallist.notifyUserCreated
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.globallist.notifyUserDeleted
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.list.notifyIdentifierDeleted
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list.notifySessionRevoked
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.DeleteUserIdentifierUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.DeleteSessionUseCase
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
import kotlinx.coroutines.flow.firstOrNull
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.detail.IdentifierDetailComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.detail.SessionDetailComponentImpl
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
    private val authStorage: AuthStorage? = null,
    private val deleteSessionUseCase: DeleteSessionUseCase? = null,
    private val deleteUserIdentifierUseCase: DeleteUserIdentifierUseCase? = null,
    private val deleteIdentifierPasswordUseCase: ManagementDeleteIdentifierPasswordUseCase? = null,
    private val getManagementAuthSettingsUseCase: GetManagementAuthSettingsUseCase,
    private val saveRemoteAuthSettingsUseCase: SaveRemoteAuthSettingsUseCase,
    private val resetRemoteAuthSettingsUseCase: ResetRemoteAuthSettingsUseCase,
    private val getManagementGlobalSettingsUseCase: GetManagementGlobalSettingsUseCase,
    private val saveRemoteGlobalSettingsUseCase: SaveRemoteGlobalSettingsUseCase,
    private val resetRemoteGlobalSettingsUseCase: ResetRemoteGlobalSettingsUseCase,
    private val getManagementSecuritySettingsUseCase: GetManagementSecuritySettingsUseCase,
    private val saveRemoteSecuritySettingsUseCase: SaveRemoteSecuritySettingsUseCase,
    private val resetRemoteSecuritySettingsUseCase: ResetRemoteSecuritySettingsUseCase,
    private val getManagementStorageSettingsUseCase: GetManagementStorageSettingsUseCase,
    private val updateManagementStorageSettingsUseCase: UpdateManagementStorageSettingsUseCase,
    private val resetRemoteManagementStorageSettingsUseCase: ResetRemoteManagementStorageSettingsUseCase,
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
    private val storageDependencies: ManagementStorageDependencies? = null,
    private val onNavigateToUserMain: (() -> Unit)? = null
) : ManagementRootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<NavigationRoute>()
    private var selfStorageFileListComponent: StorageFileListComponent? = null

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

        if (previousConfig != null) {
            navigation.pop()
        } else if (parentDestination != null && parentDestination != stack.value.active.configuration) {
            navigation.navigate { currentStack ->
                val index = currentStack.indexOfLast { it == parentDestination || it::class == parentDestination::class }
                if (index >= 0) {
                    currentStack.take(index + 1)
                } else {
                    currentStack.dropLast(1) + parentDestination
                }
            }
        }
    }

    private fun createChild(
        config: NavigationRoute,
        context: ComponentContext
    ): ManagementRootComponent.Child = when (config) {
        is ManagementMainNavigation.Main -> ManagementRootComponent.Child.Main(
            MainManagementComponentImpl(
                componentContext = context,
                onNavigateToGlobalUserList = { navigation.bringToFront(ManagementUsersNavigation.UserList()) },
                onNavigateToGlobalSessionList = { navigation.bringToFront(ManagementUsersNavigation.GlobalSessionList()) },
                onNavigateToGlobalIdentifierList = { navigation.bringToFront(ManagementUsersNavigation.GlobalIdentifierList()) },
                onNavigateToAuditEventList = { navigation.bringToFront(ManagementAuditNavigation.AuditEventList()) },
                onNavigateToSettings = { navigation.bringToFront(ManagementSettingsNavigation.Main) },
                onNavigateToGlobalStorageFileList = { navigation.bringToFront(ManagementStorageNavigation.Main) }
            )
        )
        is ManagementSettingsNavigation.Main -> ManagementRootComponent.Child.SettingsMain(
            SettingsMainComponentImpl(
                componentContext = context,
                onNavigateToEditAuthSettings = { navigation.bringToFront(ManagementAuthSettingsNavigation.AuthSettings) },
                onNavigateToEditGlobalSettings = { navigation.bringToFront(ManagementSettingsNavigation.GlobalSettings) },
                onNavigateToEditSecuritySettings = { navigation.bringToFront(ManagementSecurityNavigation.SecuritySettings) },
                onNavigateToEditStorageSettings = { navigation.bringToFront(ManagementStorageSettingsNavigation.StorageSettings) },
                onBack = { handleBack() }
            )
        )
        is ManagementAuthSettingsNavigation.AuthSettings -> ManagementRootComponent.Child.EditAuthSettings(
            EditAuthSettingsComponentImpl(
                componentContext = context,
                getManagementAuthSettingsUseCase = getManagementAuthSettingsUseCase,
                saveRemoteAuthSettingsUseCase = saveRemoteAuthSettingsUseCase,
                resetRemoteAuthSettingsUseCase = resetRemoteAuthSettingsUseCase,
                onBack = { handleBack(ManagementSettingsNavigation.Main) }
            )
        )
        is ManagementSettingsNavigation.GlobalSettings -> ManagementRootComponent.Child.EditGlobalSettings(
            EditGlobalSettingsComponentImpl(
                componentContext = context,
                getManagementGlobalSettingsUseCase = getManagementGlobalSettingsUseCase,
                saveRemoteGlobalSettingsUseCase = saveRemoteGlobalSettingsUseCase,
                resetRemoteGlobalSettingsUseCase = resetRemoteGlobalSettingsUseCase,
                onBack = { handleBack(ManagementSettingsNavigation.Main) }
            )
        )
        is ManagementSecurityNavigation.SecuritySettings -> ManagementRootComponent.Child.EditSecuritySettings(
            EditSecuritySettingsComponentImpl(
                componentContext = context,
                getManagementSecuritySettingsUseCase = getManagementSecuritySettingsUseCase,
                saveRemoteSecuritySettingsUseCase = saveRemoteSecuritySettingsUseCase,
                resetRemoteSecuritySettingsUseCase = resetRemoteSecuritySettingsUseCase,
                onBack = { handleBack(ManagementSettingsNavigation.Main) }
            )
        )
        is ManagementStorageSettingsNavigation.StorageSettings -> ManagementRootComponent.Child.EditStorageSettings(
            EditStorageSettingsComponentImpl(
                componentContext = context,
                getSettingsUseCase = getManagementStorageSettingsUseCase,
                updateSettingsUseCase = updateManagementStorageSettingsUseCase,
                resetSettingsUseCase = resetRemoteManagementStorageSettingsUseCase,
                onBack = { handleBack(ManagementSettingsNavigation.Main) }
            )
        )
        is ManagementUsersNavigation.UserList -> ManagementRootComponent.Child.GlobalUserList(
            GlobalUserListComponentImpl(
                componentContext = context,
                params = config.params,
                getUsersUseCase = getUsersUseCase,
                userRepository = userRepository,
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
                userRepository = userRepository,
                onNavigateToSessions = { userId ->
                    navigation.bringToFront(ManagementUsersNavigation.UserSessionList(ManagementUserSessionListParams(userId = userId.asHexDashString())))
                },
                onNavigateToIdentifiers = { userId ->
                    navigation.bringToFront(ManagementUsersNavigation.UserIdentifierList(ManagementUserIdentifierListParams(userId = userId.asHexDashString())))
                },
                onNavigateToFilesList = { userId ->
                    navigation.bringToFront(ManagementStorageNavigation.FileList(
                        params = ManagementStorageFileListParams(ownerUserIds = listOf(userId.asHexDashString()))
                    ))
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
                onNavigateToUserMain = onNavigateToUserMain,
                onUserDeleted = { userId -> stack.value.notifyUserDeleted(userId) },
                onBack = { handleBack(ManagementUsersNavigation.UserList()) }
            )
        )
        is ManagementUsersNavigation.CreateUser -> ManagementRootComponent.Child.CreateUser(
            CreateUserComponentImpl(
                componentContext = context,
                createUserUseCase = createUserUseCase,
                onUserCreated = { stack.value.notifyUserCreated() },
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
                authStorage = authStorage,
                userRepository = userRepository,
                onNavigateToSessionDetail = { session ->
                    navigation.bringToFront(
                        ManagementUsersNavigation.SessionDetail(
                            SessionDetailParams(
                                sessionId = session.id.asHexDashString(),
                                userId = config.params.userId
                            )
                        )
                    )
                },
                onBack = {
                    val parent = if (config.params.userId.isNotBlank()) {
                        ManagementUsersNavigation.UserDetail(UserDetailParams(userId = config.params.userId))
                    } else {
                        ManagementUsersNavigation.UserList()
                    }
                    handleBack(parent)
                }
            )
        )
        is ManagementUsersNavigation.UserIdentifierList -> ManagementRootComponent.Child.UserIdentifierList(
            UserIdentifierListComponentImpl(
                componentContext = context,
                params = config.params,
                managementGetUserIdentifiersUseCase = managementGetUserIdentifiersUseCase,
                userRepository = userRepository,
                onIdentifierSelect = { identifierId ->
                    navigation.bringToFront(
                        ManagementUsersNavigation.IdentifierDetail(
                            IdentifierDetailParams(
                                identifierId = identifierId,
                                userId = config.params.userId
                            )
                        )
                    )
                },
                onBack = {
                    val parent = if (config.params.userId.isNotBlank()) {
                        ManagementUsersNavigation.UserDetail(UserDetailParams(userId = config.params.userId))
                    } else {
                        ManagementUsersNavigation.UserList()
                    }
                    handleBack(parent)
                }
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
                onBack = { handleBack(ManagementAuditNavigation.AuditEventList()) }
            )
        )
        is ManagementUsersNavigation.GlobalSessionList -> ManagementRootComponent.Child.GlobalSessionList(
            GlobalSessionListComponentImpl(
                componentContext = context,
                params = config.params,
                managementGetSessionsUseCase = managementGetSessionsUseCase,
                managementDeleteSessionUseCase = managementDeleteSessionUseCase,
                authStorage = authStorage,
                userRepository = userRepository,
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
                revokeSession = { targetId ->
                    val activeSessionId = authStorage?.getSessionId()?.toUserSessionIdOrNull()
                    if (targetId == activeSessionId) {
                        AppResult.Error(CommonError.ContractViolation(throwable = IllegalStateException("Cannot revoke current active session")))
                    } else {
                        val sessionResult = managementGetSessionUseCase?.invoke(targetId.asHexDashString())
                        if (sessionResult is AppResult.Success) {
                            managementDeleteSessionUseCase.invoke(sessionResult.data.userId, targetId.asHexDashString())
                        } else if (deleteSessionUseCase != null) {
                            deleteSessionUseCase.invoke(targetId)
                        } else {
                            AppResult.Error(CommonError.ContractViolation())
                        }
                    }
                },
                authStorage = authStorage,
                onSessionRevoked = { sessionId -> stack.value.notifySessionRevoked(sessionId) },
                onNavigateToIdentifierDetail = { identifierId ->
                    navigation.bringToFront(ManagementUsersNavigation.IdentifierDetail(IdentifierDetailParams(identifierId = identifierId.asHexDashString())))
                },
                onNavigateToUserDetail = { userId ->
                    navigation.bringToFront(ManagementUsersNavigation.UserDetail(UserDetailParams(userId = userId.asHexDashString())))
                },
                onBack = {
                    val userId = config.params.userId
                    val parent = if (!userId.isNullOrBlank()) {
                        ManagementUsersNavigation.UserSessionList(
                            ManagementUserSessionListParams(userId = userId)
                        )
                    } else {
                        ManagementUsersNavigation.GlobalSessionList()
                    }
                    handleBack(parent)
                }
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
                deleteIdentifier = { targetId ->
                    val identifierResult = managementGetIdentifierUseCase?.invoke(targetId.asHexDashString())
                    if (identifierResult is AppResult.Success) {
                        managementDeleteIdentifierUseCase?.invoke(identifierResult.data.userId, targetId.asHexDashString())
                            ?: AppResult.Error(CommonError.ContractViolation())
                    } else if (deleteUserIdentifierUseCase != null) {
                        deleteUserIdentifierUseCase.invoke(targetId)
                    } else {
                        AppResult.Error(CommonError.ContractViolation())
                    }
                },
                deletePassword = { targetId ->
                    val identifierResult = managementGetIdentifierUseCase?.invoke(targetId.asHexDashString())
                    if (identifierResult is AppResult.Success) {
                        deleteIdentifierPasswordUseCase?.invoke(identifierResult.data.userId, targetId.asHexDashString())
                            ?: AppResult.Error(CommonError.ContractViolation())
                    } else {
                        AppResult.Error(CommonError.ContractViolation())
                    }
                },
                authStorage = authStorage,
                onIdentifierDeleted = { identifierId -> stack.value.notifyIdentifierDeleted(identifierId) },
                onNavigateToUserDetail = { userId ->
                    navigation.bringToFront(ManagementUsersNavigation.UserDetail(UserDetailParams(userId = userId.asHexDashString())))
                },
                onBack = {
                    val userId = config.params.userId
                    val parent = if (!userId.isNullOrBlank()) {
                        ManagementUsersNavigation.UserIdentifierList(
                            ManagementUserIdentifierListParams(userId = userId)
                        )
                    } else {
                        ManagementUsersNavigation.GlobalIdentifierList()
                    }
                    handleBack(parent)
                }
            )
        )
        is ManagementStorageNavigation.FileList -> {
            val dependencies = storageDependencies
            if (dependencies != null) {
                ManagementRootComponent.Child.GlobalStorageFileList(
                    createManagementGlobalStorageFileListComponent(
                        componentContext = context,
                        getFilesUseCase = dependencies.getFilesUseCase,
                        deleteFileUseCase = dependencies.deleteFileUseCase,
                        deleteFilesBatchUseCase = dependencies.deleteFilesBatchUseCase,
                        ownerUserId = config.params.ownerUserIds.firstOrNull(),
                        isSelfView = false,
                        onNavigateToUpload = null,
                        onNavigateToFileDetail = { fileId ->
                            navigation.bringToFront(ManagementStorageNavigation.FileDetail(StorageFileDetailParams(fileId = fileId)))
                        },
                        onNavigateBack = { handleBack() }
                    )
                )
            } else {
                createChild(ManagementMainNavigation.Main, context)
            }
        }
        is ManagementStorageNavigation.FileDetail -> {
            val dependencies = storageDependencies
            if (dependencies != null) {
                ManagementRootComponent.Child.GlobalStorageFileDetail(
                    StorageFileDetailComponentImpl(
                        componentContext = context,
                        params = config.params,
                        dataSource = object : StorageFileDetailDataSource {
                            override val isDeleteAvailable: Boolean = true
                            override suspend fun getFileDetail(fileId: String) = dependencies.getFileDetailUseCase(fileId)
                            override suspend fun deleteFile(fileId: String) = dependencies.deleteFileUseCase(fileId)
                        },
                        onNavigateToUserDetail = { userId ->
                            navigation.bringToFront(ManagementUsersNavigation.UserDetail(UserDetailParams(userId = userId)))
                        },
                        onNavigateBack = { handleBack(ManagementStorageNavigation.FileList()) }
                    )
                )
            } else {
                createChild(ManagementMainNavigation.Main, context)
            }
        }
        is ManagementSelfStorageNavigation.FileList -> {
            val dependencies = storageDependencies
            if (dependencies != null) {
                val listComponent = selfStorageFileListComponent ?: createStaffSelfStorageFileListComponent(
                    componentContext = context,
                    getFilesUseCase = dependencies.getFilesUseCase,
                    deleteFileUseCase = dependencies.deleteFileUseCase,
                    deleteFilesBatchUseCase = dependencies.deleteFilesBatchUseCase,
                    initialUserIdProvider = { userRepository.currentUser.firstOrNull()?.id?.asHexDashString() },
                    onNavigateToUpload = {
                        navigation.bringToFront(ManagementSelfStorageNavigation.FileUpload())
                    },
                    onNavigateToFileDetail = { fileId ->
                        navigation.bringToFront(ManagementSelfStorageNavigation.FileDetail(StorageFileDetailParams(fileId = fileId)))
                    },
                    onNavigateBack = { handleBack() }
                ).also { selfStorageFileListComponent = it }
                ManagementRootComponent.Child.SelfStorageFileList(listComponent)
            } else {
                createChild(ManagementMainNavigation.Main, context)
            }
        }
        is ManagementSelfStorageNavigation.FileDetail -> {
            val dependencies = storageDependencies
            if (dependencies != null) {
                ManagementRootComponent.Child.GlobalStorageFileDetail(
                    StorageFileDetailComponentImpl(
                        componentContext = context,
                        params = config.params,
                        dataSource = object : StorageFileDetailDataSource {
                            override val isDeleteAvailable: Boolean = true
                            override suspend fun getFileDetail(fileId: String) = dependencies.getFileDetailUseCase(fileId)
                            override suspend fun deleteFile(fileId: String) = dependencies.deleteFileUseCase(fileId)
                        },
                        onNavigateToUserDetail = { userId ->
                            navigation.bringToFront(ManagementUsersNavigation.UserDetail(UserDetailParams(userId = userId)))
                        },
                        onNavigateBack = { handleBack(ManagementSelfStorageNavigation.FileList()) }
                    )
                )
            } else {
                createChild(ManagementMainNavigation.Main, context)
            }
        }
        is ManagementSelfStorageNavigation.FileUpload -> {
            val dependencies = storageDependencies
            if (dependencies != null) {
                ManagementRootComponent.Child.GlobalStorageFileUpload(
                    StorageFileUploadScreenComponentImpl(
                        componentContext = context,
                        config = StorageFileUploadUiConfig(),
                        uploadFileUseCase = dependencies.uploadFileUseCase,
                        onUploaded = {
                            selfStorageFileListComponent?.onRefresh()
                        },
                        onNavigateBack = { handleBack(ManagementSelfStorageNavigation.FileList()) }
                    )
                )
            } else {
                createChild(ManagementMainNavigation.Main, context)
            }
        }
        is ManagementStorageNavigation.Main -> {
            val dependencies = storageDependencies
            if (dependencies != null) {
                ManagementRootComponent.Child.GlobalStorageFileList(
                    createManagementGlobalStorageFileListComponent(
                        componentContext = context,
                        getFilesUseCase = dependencies.getFilesUseCase,
                        deleteFileUseCase = dependencies.deleteFileUseCase,
                        deleteFilesBatchUseCase = dependencies.deleteFilesBatchUseCase,
                        ownerUserId = null,
                        isSelfView = false,
                        onNavigateToUpload = null,
                        onNavigateToFileDetail = { fileId ->
                            navigation.bringToFront(ManagementStorageNavigation.FileDetail(StorageFileDetailParams(fileId = fileId)))
                        },
                        onNavigateBack = { handleBack() }
                    )
                )
            } else {
                createChild(ManagementMainNavigation.Main, context)
            }
        }
        is ManagementSelfStorageNavigation.Main -> {
            val dependencies = storageDependencies
            if (dependencies != null) {
                ManagementRootComponent.Child.SelfStorageFileList(
                    createStaffSelfStorageFileListComponent(
                        componentContext = context,
                        getFilesUseCase = dependencies.getFilesUseCase,
                        deleteFileUseCase = dependencies.deleteFileUseCase,
                        deleteFilesBatchUseCase = dependencies.deleteFilesBatchUseCase,
                        initialUserIdProvider = { userRepository.currentUser.firstOrNull()?.id?.asHexDashString() },
                        onNavigateToUpload = {
                            navigation.bringToFront(ManagementSelfStorageNavigation.FileUpload())
                        },
                        onNavigateToFileDetail = { fileId ->
                            navigation.bringToFront(ManagementSelfStorageNavigation.FileDetail(StorageFileDetailParams(fileId = fileId)))
                        },
                        onNavigateBack = { handleBack() }
                    )
                )
            } else {
                createChild(ManagementMainNavigation.Main, context)
            }
        }
        is ManagementAuditNavigation.Main -> createChild(ManagementAuditNavigation.AuditEventList(), context)
        else -> createMainChild(context)
    }

    private fun createMainChild(context: ComponentContext): ManagementRootComponent.Child.Main {
        return ManagementRootComponent.Child.Main(
            MainManagementComponentImpl(
                componentContext = context,
                onNavigateToGlobalUserList = { navigation.bringToFront(ManagementUsersNavigation.UserList()) },
                onNavigateToGlobalSessionList = { navigation.bringToFront(ManagementUsersNavigation.GlobalSessionList()) },
                onNavigateToGlobalIdentifierList = { navigation.bringToFront(ManagementUsersNavigation.GlobalIdentifierList()) },
                onNavigateToGlobalStorageFileList = { navigation.bringToFront(ManagementStorageNavigation.FileList()) },
                onNavigateToAuditEventList = { navigation.bringToFront(ManagementAuditNavigation.AuditEventList()) },
                onNavigateToSettings = { navigation.bringToFront(ManagementSettingsNavigation.Main) }
            )
        )
    }
}
