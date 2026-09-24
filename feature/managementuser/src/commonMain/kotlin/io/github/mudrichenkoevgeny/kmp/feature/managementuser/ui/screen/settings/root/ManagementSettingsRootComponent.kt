package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.ui.screen.root.AuditApiRootComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.identifier.globallist.GlobalIdentifierListComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.globallist.GlobalSessionListComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.root.UsersManagementRootComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.ManagementSettingsDestination
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.auth.EditAuthSettingsComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.global.EditGlobalSettingsComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.main.MainManagementSettingsComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.security.EditSecuritySettingsComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.detail.IdentifierDetailComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.list.IdentifierListOwner
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.detail.SessionDetailComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.list.SessionListOwner

/**
 * Root Decompose component for management settings flows.
 */
interface ManagementSettingsRootComponent {
    /** Navigation stack of child screens. */
    val stack: Value<ChildStack<ManagementSettingsDestination, Child>>

    /** Active child component in the navigation stack. */
    sealed interface Child {
        /** Main settings landing screen. */
        class Main(val component: MainManagementSettingsComponent) : Child

        /** Edit authentication settings screen. */
        class EditAuthSettings(val component: EditAuthSettingsComponent) : Child

        /** Edit global settings screen. */
        class EditGlobalSettings(val component: EditGlobalSettingsComponent) : Child

        /** Edit security settings screen. */
        class EditSecuritySettings(val component: EditSecuritySettingsComponent) : Child

        /** Users management screen. */
        class UsersManagement(val component: UsersManagementRootComponent) : Child

        /** Audit logs screen. */
        class AuditLogs(val component: AuditApiRootComponent) : Child

        /** All platform sessions screen. */
        class Sessions(val component: GlobalSessionListComponent) : Child, SessionListOwner by component

        /** Session detail screen. */
        class SessionDetail(val component: SessionDetailComponent) : Child

        /** All platform identifiers screen. */
        class GlobalIdentifiers(val component: GlobalIdentifierListComponent) : Child, IdentifierListOwner by component

        /** Identifier detail screen. */
        class IdentifierDetail(val component: IdentifierDetailComponent) : Child
    }
}
