package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.create.CreateUserScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.detail.UserDetailScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.identifiers.UserIdentifiersScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.main.UsersManagementMainScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.sessions.UserSessionsScreen

@Composable
fun UsersManagementRootScreen(component: UsersManagementRootComponent) {
    Children(
        stack = component.stack,
        animation = stackAnimation(slide())
    ) { child ->
        when (val instance = child.instance) {
            is UsersManagementRootComponent.Child.Main -> UsersManagementMainScreen(instance.component)
            is UsersManagementRootComponent.Child.Detail -> UserDetailScreen(instance.component)
            is UsersManagementRootComponent.Child.Create -> CreateUserScreen(instance.component)
            is UsersManagementRootComponent.Child.Sessions -> UserSessionsScreen(instance.component)
            is UsersManagementRootComponent.Child.Identifiers -> UserIdentifiersScreen(instance.component)
        }
    }
}
