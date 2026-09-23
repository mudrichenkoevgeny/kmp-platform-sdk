package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.UsersManagementDestination
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.create.CreateUserComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.detail.UserDetailComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.identifier.userlist.UserIdentifierListComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.main.UsersManagementMainComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.userlist.UserSessionListComponent

interface UsersManagementRootComponent {
    val stack: Value<ChildStack<UsersManagementDestination, Child>>

    sealed interface Child {
        class Main(val component: UsersManagementMainComponent) : Child
        class Detail(val component: UserDetailComponent) : Child
        class Create(val component: CreateUserComponent) : Child
        class UserSessionList(val component: UserSessionListComponent) : Child
        class Identifiers(val component: UserIdentifierListComponent) : Child
    }

    fun onBackClick()
}
