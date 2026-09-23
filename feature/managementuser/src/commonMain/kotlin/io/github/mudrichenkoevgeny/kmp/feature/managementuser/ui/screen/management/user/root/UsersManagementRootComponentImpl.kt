package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.create.CreateUserComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.detail.UserDetailComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.identifier.userlist.UserIdentifierListComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.main.UsersManagementMainComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.userlist.UserSessionListComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.UsersManagementDestination
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier.ManagementDeleteIdentifierUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier.ManagementDeleteIdentifierPasswordUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier.ManagementGetIdentifiersUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementDeleteAllUserSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementDeleteSessionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementGetSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.CreateUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.DeleteUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.GetUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.GetUsersUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.UpdateUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.security.ManagementDisableTotpUseCase
import com.arkivanov.decompose.DelicateDecomposeApi

@OptIn(DelicateDecomposeApi::class)
class UsersManagementRootComponentImpl(
    componentContext: ComponentContext,
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
        is UsersManagementDestination.Main -> UsersManagementRootComponent.Child.Main(
            UsersManagementMainComponentImpl(
                componentContext = context,
                getUsersUseCase = getUsersUseCase,
                onNavigateToUserDetail = { userId -> navigation.bringToFront(
                    UsersManagementDestination.Detail(userId.asHexDashString())) },
                onNavigateToCreateUser = { navigation.bringToFront(UsersManagementDestination.Create) },
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
                onNavigateToSessions = { userId -> navigation.bringToFront(
                    UsersManagementDestination.UserSessionList(userId.asHexDashString())) },
                onNavigateToIdentifiers = { userId -> navigation.bringToFront(
                    UsersManagementDestination.Identifiers(userId.asHexDashString())) },
                onBack = navigation::pop
            )
        )
        is UsersManagementDestination.Create -> UsersManagementRootComponent.Child.Create(
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
                onBack = navigation::pop
            )
        )
        is UsersManagementDestination.Identifiers -> UsersManagementRootComponent.Child.Identifiers(
            UserIdentifierListComponentImpl(
                componentContext = context,
                userId = config.userId,
                managementGetIdentifiersUseCase = managementGetIdentifiersUseCase,
                managementDeleteIdentifierUseCase = managementDeleteIdentifierUseCase,
                managementDeleteIdentifierPasswordUseCase = managementDeleteIdentifierPasswordUseCase,
                onBack = navigation::pop
            )
        )
    }
}
