package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.ui.screen.root.AuditApiRootComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.UsersManagementRootComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.auth.EditAuthSettingsComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.global.EditGlobalSettingsComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.main.MainManagementSettingsComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.security.EditSecuritySettingsComponent

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
    }
}
