package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewFontScales
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewScreenSizes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewThemes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.detail.StorageFileDetailScreen
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list.StorageFileListScreen
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.settings.EditStorageSettingsScreen
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.upload.StorageFileUploadScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.root.ManagementRootComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.detail.AuditEventDetailScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.list.AuditEventListScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.main.MainManagementScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.auth.EditAuthSettingsScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.global.EditGlobalSettingsScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.main.SettingsMainScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.security.EditSecuritySettingsScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.create.CreateUserScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.detail.UserDetailScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.globallist.GlobalUserListScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.detail.IdentifierDetailScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.list.IdentifierListScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.detail.SessionDetailScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list.SessionListScreen

/**
 * Root Composable for management stack.
 *
 * @param component Root Decompose stack component.
 */
@Composable
public fun ManagementRootScreen(component: ManagementRootComponent) {
    Children(
        stack = component.stack,
        animation = stackAnimation(fade())
    ) { child ->
        RenderManagementChild(child.instance)
    }
}

@Composable
private fun RenderManagementChild(child: ManagementRootComponent.Child) {
    when (child) {
        is ManagementRootComponent.Child.Main -> MainManagementScreen(child.component)
        is ManagementRootComponent.Child.SettingsMain,
        is ManagementRootComponent.Child.EditAuthSettings,
        is ManagementRootComponent.Child.EditGlobalSettings,
        is ManagementRootComponent.Child.EditSecuritySettings,
        is ManagementRootComponent.Child.EditStorageSettings -> RenderSettingsChild(child)

        is ManagementRootComponent.Child.GlobalUserList,
        is ManagementRootComponent.Child.CreateUser,
        is ManagementRootComponent.Child.UserDetail,
        is ManagementRootComponent.Child.UserSessionList,
        is ManagementRootComponent.Child.UserIdentifierList,
        is ManagementRootComponent.Child.GlobalSessionList,
        is ManagementRootComponent.Child.SessionDetail,
        is ManagementRootComponent.Child.GlobalIdentifierList,
        is ManagementRootComponent.Child.IdentifierDetail -> RenderUserChild(child)

        is ManagementRootComponent.Child.AuditEventList,
        is ManagementRootComponent.Child.AuditEventDetail -> RenderAuditChild(child)

        is ManagementRootComponent.Child.GlobalStorageFileList,
        is ManagementRootComponent.Child.SelfStorageFileList,
        is ManagementRootComponent.Child.GlobalStorageFileDetail,
        is ManagementRootComponent.Child.GlobalStorageFileUpload -> RenderStorageChild(child)
    }
}

@Composable
private fun RenderSettingsChild(child: ManagementRootComponent.Child) {
    when (child) {
        is ManagementRootComponent.Child.SettingsMain -> SettingsMainScreen(child.component)
        is ManagementRootComponent.Child.EditAuthSettings -> EditAuthSettingsScreen(child.component)
        is ManagementRootComponent.Child.EditGlobalSettings -> EditGlobalSettingsScreen(child.component)
        is ManagementRootComponent.Child.EditSecuritySettings -> EditSecuritySettingsScreen(child.component)
        is ManagementRootComponent.Child.EditStorageSettings -> EditStorageSettingsScreen(child.component)
        else -> Unit
    }
}

@Composable
private fun RenderUserChild(child: ManagementRootComponent.Child) {
    when (child) {
        is ManagementRootComponent.Child.GlobalUserList -> GlobalUserListScreen(child.component)
        is ManagementRootComponent.Child.CreateUser -> CreateUserScreen(child.component)
        is ManagementRootComponent.Child.UserDetail -> UserDetailScreen(child.component)
        is ManagementRootComponent.Child.UserSessionList -> SessionListScreen(child.component)
        is ManagementRootComponent.Child.UserIdentifierList -> IdentifierListScreen(child.component)
        is ManagementRootComponent.Child.GlobalSessionList -> SessionListScreen(child.component)
        is ManagementRootComponent.Child.SessionDetail -> SessionDetailScreen(child.component)
        is ManagementRootComponent.Child.GlobalIdentifierList -> IdentifierListScreen(child.component)
        is ManagementRootComponent.Child.IdentifierDetail -> IdentifierDetailScreen(child.component)
        else -> Unit
    }
}

@Composable
private fun RenderAuditChild(child: ManagementRootComponent.Child) {
    when (child) {
        is ManagementRootComponent.Child.AuditEventList -> AuditEventListScreen(child.component)
        is ManagementRootComponent.Child.AuditEventDetail -> AuditEventDetailScreen(child.component)
        else -> Unit
    }
}

@Composable
private fun RenderStorageChild(child: ManagementRootComponent.Child) {
    when (child) {
        is ManagementRootComponent.Child.GlobalStorageFileList -> StorageFileListScreen(child.component)
        is ManagementRootComponent.Child.SelfStorageFileList -> StorageFileListScreen(child.component)
        is ManagementRootComponent.Child.GlobalStorageFileDetail -> StorageFileDetailScreen(child.component)
        is ManagementRootComponent.Child.GlobalStorageFileUpload -> StorageFileUploadScreen(child.component)
        else -> Unit
    }
}

@InternalApi
@Composable
private fun ManagementRootScreenPreviewContent() {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        ManagementRootScreen(
            component = ManagementRootComponentMock()
        )
    }
}

@InternalApi
@Preview(showBackground = true)
@Composable
private fun ManagementRootScreenPreview() {
    ScreenPreviewContainer {
        ManagementRootScreenPreviewContent()
    }
}

@InternalApi
@PreviewScreenSizes
@Composable
private fun ScreenSizePreview() {
    ScreenPreviewContainer {
        ManagementRootScreenPreviewContent()
    }
}

@InternalApi
@PreviewThemes
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        ManagementRootScreenPreviewContent()
    }
}

@InternalApi
@PreviewFontScales
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        ManagementRootScreenPreviewContent()
    }
}
