package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.detail.AuditEventDetailComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.list.AuditEventListComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.identifier.globallist.GlobalIdentifierListComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.identifier.userlist.UserIdentifierListComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.main.MainManagementComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.globallist.GlobalSessionListComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.userlist.UserSessionListComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.auth.EditAuthSettingsComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.global.EditGlobalSettingsComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.main.SettingsMainComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.security.EditSecuritySettingsComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.create.CreateUserComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.detail.UserDetailComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.globallist.GlobalUserListComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.globallist.UserListOwner
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.detail.IdentifierDetailComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.list.IdentifierListOwner
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.detail.SessionDetailComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list.SessionListOwner
import io.github.mudrichenkoevgeny.shared.foundation.core.common.navigation.route.NavigationRoute

/**
 * Root Decompose component for management flows.
 */
interface ManagementRootComponent {
    /** Navigation stack of child screens. */
    val stack: Value<ChildStack<NavigationRoute, Child>>

    /** Navigate to an arbitrary destination (useful for deep links). */
    fun navigateTo(destination: NavigationRoute)

    /** Active child component in the navigation stack. */
    sealed interface Child {
        /** Main management landing screen. */
        class Main(val component: MainManagementComponent) : Child

        /** System main settings screen. */
        class SettingsMain(val component: SettingsMainComponent) : Child

        /** Edit authentication settings screen. */
        class EditAuthSettings(val component: EditAuthSettingsComponent) : Child

        /** Edit global settings screen. */
        class EditGlobalSettings(val component: EditGlobalSettingsComponent) : Child

        /** Edit security settings screen. */
        class EditSecuritySettings(val component: EditSecuritySettingsComponent) : Child

        /** All platform users screen. */
        class GlobalUserList(val component: GlobalUserListComponent) : Child, UserListOwner by component

        /** Create user screen. */
        class CreateUser(val component: CreateUserComponent) : Child

        /** User detail screen. */
        class UserDetail(val component: UserDetailComponent) : Child

        /** User sessions screen. */
        class UserSessionList(val component: UserSessionListComponent) : Child, io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list.SessionListOwner by component

        /** User identifiers screen. */
        class UserIdentifierList(val component: UserIdentifierListComponent) : Child, io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.list.IdentifierListOwner by component

        /** Audit logs list screen. */
        class AuditEventList(val component: AuditEventListComponent) : Child

        /** Audit event detail screen. */
        class AuditEventDetail(val component: AuditEventDetailComponent) : Child

        /** All platform sessions screen. */
        class GlobalSessionList(val component: GlobalSessionListComponent) : Child, io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list.SessionListOwner by component

        /** Session detail screen. */
        class SessionDetail(val component: io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.detail.SessionDetailComponent) : Child

        /** All platform identifiers screen. */
        class GlobalIdentifierList(val component: GlobalIdentifierListComponent) : Child, io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.list.IdentifierListOwner by component

        /** Identifier detail screen. */
        class IdentifierDetail(val component: io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.detail.IdentifierDetailComponent) : Child
    }
}
