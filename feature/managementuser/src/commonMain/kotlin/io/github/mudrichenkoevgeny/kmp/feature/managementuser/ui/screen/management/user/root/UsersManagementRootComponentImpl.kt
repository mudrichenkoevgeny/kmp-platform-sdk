package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.identifier.userlist.UserIdentifierListComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.userlist.UserSessionListComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.UsersManagementDestination
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.create.CreateUserComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.detail.UserDetailComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.main.UsersManagementMainComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier.ManagementDeleteIdentifierPasswordUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier.ManagementDeleteIdentifierUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier.ManagementGetIdentifierUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier.ManagementGetIdentifiersUseCase
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
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.detail.IdentifierDetailComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.list.notifyIdentifierDeleted
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.detail.SessionDetailComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.list.notifySessionRevoked

@OptIn(DelicateDecomposeApi::class)
class UsersManagementRootComponentImpl(
    componentContext: ComponentContext,
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
    private val onBack: () -> Unit
) : UsersManagementRootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<UsersManagementDestination>()

    override val stack: Value<ChildStack<UsersManagementDestination, UsersManagementRootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = UsersManagementDestination.serializer(),
            initialConfiguration = UsersManagementDestination.Main,
            handleBackButton = true,
            childFactory = ::createChild
        )

    override fun onBackClick() {
        onBack()
    }

    private fun createChild(
        config: UsersManagementDestination,
        context: ComponentContext
    ): UsersManagementRootComponent.Child = when (config) {
        UsersManagementDestination.Main -> UsersManagementRootComponent.Child.Main(
            UsersManagementMainComponentImpl(
                componentContext = context,
                getUsersUseCase = getUsersUseCase,
                onNavigateToUserDetail = { userId ->
                    navigation.bringToFront(UsersManagementDestination.Detail(userId.asHexDashString()))
                },
                onNavigateToCreateUser = {
                    navigation.bringToFront(UsersManagementDestination.Create)
                },
                onBack = onBack
            )
        )
        is UsersManagementDestination.Detail -> UsersManagementRootComponent.Child.Detail(
            UserDetailComponentImpl(
                componentContext = context,
                userId = config.userId,
                getUserUseCase = getUserUseCase,
                updateUserUseCase = updateUserUseCase,
                deleteUserUseCase = deleteUserUseCase,
                managementDisableTotpUseCase = managementDisableTotpUseCase,
                onNavigateToSessions = { userId ->
                    navigation.bringToFront(UsersManagementDestination.UserSessionList(userId.asHexDashString()))
                },
                onNavigateToIdentifiers = { userId ->
                    navigation.bringToFront(UsersManagementDestination.Identifiers(userId.asHexDashString()))
                },
                onBack = navigation::pop
            )
        )
        UsersManagementDestination.Create -> UsersManagementRootComponent.Child.Create(
            CreateUserComponentImpl(
                componentContext = context,
                createUserUseCase = createUserUseCase,
                onSuccess = navigation::pop,
                onBack = navigation::pop
            )
        )
        is UsersManagementDestination.UserSessionList -> UsersManagementRootComponent.Child.UserSessionList(
            UserSessionListComponentImpl(
                componentContext = context,
                userId = config.userId,
                managementGetSessionsUseCase = managementGetSessionsUseCase,
                managementDeleteSessionUseCase = managementDeleteSessionUseCase,
                managementDeleteAllUserSessionsUseCase = managementDeleteAllUserSessionsUseCase,
                onNavigateToSessionDetail = { session ->
                    navigation.bringToFront(
                        UsersManagementDestination.SessionDetail(
                            userIdValue = config.userId.asHexDashString(),
                            sessionIdValue = session.id.asHexDashString()
                        )
                    )
                },
                onBack = navigation::pop
            )
        )
        is UsersManagementDestination.SessionDetail -> UsersManagementRootComponent.Child.SessionDetail(
            SessionDetailComponentImpl(
                componentContext = context,
                sessionId = config.sessionId,
                fetchSession = managementGetSessionUseCase?.let { useCase ->
                    { targetId -> useCase(targetId.asHexDashString()) }
                },
                revokeSession = { targetId ->
                    managementDeleteSessionUseCase(config.userId, targetId.asHexDashString())
                },
                onSessionRevoked = { stack.value.notifySessionRevoked(it) },
                onNavigateToIdentifierDetail = { identifierId ->
                    navigation.bringToFront(
                        UsersManagementDestination.IdentifierDetail(
                            userIdValue = config.userId.asHexDashString(),
                            identifierIdValue = identifierId.asHexDashString()
                        )
                    )
                },
                onBack = navigation::pop
            )
        )
        is UsersManagementDestination.Identifiers -> UsersManagementRootComponent.Child.Identifiers(
            UserIdentifierListComponentImpl(
                componentContext = context,
                userId = config.userId,
                managementGetIdentifiersUseCase = managementGetIdentifiersUseCase,
                onIdentifierSelect = { identifierId ->
                    navigation.bringToFront(
                        UsersManagementDestination.IdentifierDetail(
                            userIdValue = config.userId.asHexDashString(),
                            identifierIdValue = identifierId
                        )
                    )
                },
                onBack = navigation::pop
            )
        )
        is UsersManagementDestination.IdentifierDetail -> UsersManagementRootComponent.Child.IdentifierDetail(
            IdentifierDetailComponentImpl(
                componentContext = context,
                identifierId = config.identifierId,
                fetchIdentifier = managementGetIdentifierUseCase?.let { useCase ->
                    { targetId -> useCase(targetId.asHexDashString()) }
                },
                deleteIdentifier = { targetId ->
                    managementDeleteIdentifierUseCase(config.userId, targetId.asHexDashString())
                },
                deletePassword = { targetId ->
                    managementDeleteIdentifierPasswordUseCase(config.userId, targetId.asHexDashString())
                },
                onIdentifierDeleted = { stack.value.notifyIdentifierDeleted(it) },
                onBack = navigation::pop
            )
        )
    }
}
