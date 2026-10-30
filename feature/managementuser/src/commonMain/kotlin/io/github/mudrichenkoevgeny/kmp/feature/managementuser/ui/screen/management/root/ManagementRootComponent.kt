package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.detail.StorageFileDetailComponent
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list.StorageFileListComponent
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.upload.StorageFileUploadScreenComponent
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.settings.EditStorageSettingsComponent
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

    /**
     * Navigate to an arbitrary destination.
     *
     * @param destination Target navigation route.
     */
    fun navigateTo(destination: NavigationRoute)

    /** Active child component in the navigation stack. */
    sealed interface Child {

        /**
         * Main management landing screen.
         *
         * @property component Decompose controller for main management screen.
         */
        class Main(val component: MainManagementComponent) : Child

        /**
         * System main settings screen.
         *
         * @property component Decompose controller for settings main screen.
         */
        class SettingsMain(val component: SettingsMainComponent) : Child

        /**
         * Edit authentication settings screen.
         *
         * @property component Decompose controller for auth settings screen.
         */
        class EditAuthSettings(val component: EditAuthSettingsComponent) : Child

        /**
         * Edit global settings screen.
         *
         * @property component Decompose controller for global settings screen.
         */
        class EditGlobalSettings(val component: EditGlobalSettingsComponent) : Child

        /**
         * Edit security settings screen.
         *
         * @property component Decompose controller for security settings screen.
         */
        class EditSecuritySettings(val component: EditSecuritySettingsComponent) : Child

        /**
         * Edit storage settings screen.
         *
         * @property component Decompose controller for storage settings screen.
         */
        class EditStorageSettings(val component: EditStorageSettingsComponent) : Child

        /**
         * All platform users screen.
         *
         * @property component Decompose controller for global user list screen.
         */
        class GlobalUserList(val component: GlobalUserListComponent) : Child, UserListOwner by component

        /**
         * Create user screen.
         *
         * @property component Decompose controller for create user screen.
         */
        class CreateUser(val component: CreateUserComponent) : Child

        /**
         * User detail screen.
         *
         * @property component Decompose controller for user detail screen.
         */
        class UserDetail(val component: UserDetailComponent) : Child

        /**
         * User sessions screen.
         *
         * @property component Decompose controller for user sessions list screen.
         */
        class UserSessionList(val component: UserSessionListComponent) : Child, SessionListOwner by component

        /**
         * User identifiers screen.
         *
         * @property component Decompose controller for user identifiers list screen.
         */
        class UserIdentifierList(val component: UserIdentifierListComponent) : Child, IdentifierListOwner by component

        /**
         * Audit logs list screen.
         *
         * @property component Decompose controller for audit event list screen.
         */
        class AuditEventList(val component: AuditEventListComponent) : Child

        /**
         * Audit event detail screen.
         *
         * @property component Decompose controller for audit event detail screen.
         */
        class AuditEventDetail(val component: AuditEventDetailComponent) : Child

        /**
         * All platform sessions screen.
         *
         * @property component Decompose controller for global session list screen.
         */
        class GlobalSessionList(val component: GlobalSessionListComponent) : Child, SessionListOwner by component

        /**
         * Session detail screen.
         *
         * @property component Decompose controller for session detail screen.
         */
        class SessionDetail(val component: SessionDetailComponent) : Child

        /**
         * All platform identifiers screen.
         *
         * @property component Decompose controller for global identifier list screen.
         */
        class GlobalIdentifierList(val component: GlobalIdentifierListComponent) : Child, IdentifierListOwner by component

        /**
         * Identifier detail screen.
         *
         * @property component Decompose controller for identifier detail screen.
         */
        class IdentifierDetail(val component: IdentifierDetailComponent) : Child

        /**
         * All platform storage files screen.
         *
         * @property component Decompose controller for global storage file list screen.
         */
        class GlobalStorageFileList(val component: StorageFileListComponent) : Child

        /**
         * Staff self-service storage files screen.
         *
         * @property component Decompose controller for self-service storage file list screen.
         */
        class SelfStorageFileList(val component: StorageFileListComponent) : Child

        /**
         * Global storage file detail screen.
         *
         * @property component Decompose controller for storage file detail screen.
         */
        class GlobalStorageFileDetail(val component: StorageFileDetailComponent) : Child

        /**
         * Storage file upload utility screen.
         *
         * @property component Decompose controller for storage file upload screen.
         */
        class GlobalStorageFileUpload(val component: StorageFileUploadScreenComponent) : Child
    }
}
