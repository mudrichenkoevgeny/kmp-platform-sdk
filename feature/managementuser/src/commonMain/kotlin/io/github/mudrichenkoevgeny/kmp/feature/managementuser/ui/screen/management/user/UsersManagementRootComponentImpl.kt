package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.create.CreateUserComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.detail.UserDetailComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.identifiers.UserIdentifiersComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.main.UsersManagementMainComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.sessions.UserSessionsComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier.ManagementGetIdentifiersUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementGetSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.CreateUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.DeleteUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.GetUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.GetUsersUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.UpdateUserUseCase
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
                onNavigateToUserDetail = { userId -> navigation.push(UsersManagementDestination.Detail(userId.value.toString())) },
                onNavigateToCreateUser = { navigation.push(UsersManagementDestination.Create) },
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
                onNavigateToSessions = { userId -> navigation.push(UsersManagementDestination.Sessions(userId.value.toString())) },
                onNavigateToIdentifiers = { userId -> navigation.push(UsersManagementDestination.Identifiers(userId.value.toString())) },
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
        is UsersManagementDestination.Sessions -> UsersManagementRootComponent.Child.Sessions(
            UserSessionsComponentImpl(
                componentContext = context,
                userId = config.userId,
                managementGetSessionsUseCase = managementGetSessionsUseCase,
                onBack = navigation::pop
            )
        )
        is UsersManagementDestination.Identifiers -> UsersManagementRootComponent.Child.Identifiers(
            UserIdentifiersComponentImpl(
                componentContext = context,
                userId = config.userId,
                managementGetIdentifiersUseCase = managementGetIdentifiersUseCase,
                onBack = navigation::pop
            )
        )
    }
}
