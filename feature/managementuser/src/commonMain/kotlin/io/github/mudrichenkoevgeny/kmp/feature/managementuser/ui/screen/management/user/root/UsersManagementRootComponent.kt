package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.UsersManagementDestination
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.create.CreateUserComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.detail.UserDetailComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.identifier.userlist.UserIdentifierListComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.main.UsersManagementMainComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.userlist.UserSessionListComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.detail.SessionDetailComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.list.SessionListOwner

interface UsersManagementRootComponent {
    val stack: Value<ChildStack<UsersManagementDestination, Child>>

    sealed interface Child {
        class Main(val component: UsersManagementMainComponent) : Child
        class Detail(val component: UserDetailComponent) : Child
        class Create(val component: CreateUserComponent) : Child
        class UserSessionList(val component: UserSessionListComponent) : Child, SessionListOwner by component
        class SessionDetail(val component: SessionDetailComponent) : Child
        class Identifiers(val component: UserIdentifierListComponent) : Child
    }

    fun onBackClick()
}
