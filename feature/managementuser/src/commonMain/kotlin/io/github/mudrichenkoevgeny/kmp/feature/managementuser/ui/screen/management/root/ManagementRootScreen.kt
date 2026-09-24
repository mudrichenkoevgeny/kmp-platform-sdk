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
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.root.ManagementRootComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.detail.AuditEventDetailScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.list.AuditEventListScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.identifier.globallist.GlobalIdentifierListScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.identifier.userlist.UserIdentifierListScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.main.MainManagementScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.globallist.GlobalSessionListScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.userlist.UserSessionListScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.auth.EditAuthSettingsScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.global.EditGlobalSettingsScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.security.EditSecuritySettingsScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.create.CreateUserScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.detail.UserDetailScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.globallist.GlobalUserListScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.detail.IdentifierDetailScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.detail.SessionDetailScreen

/**
 * Root Composable for management stack.
 *
 * @param component Root Decompose stack component.
 */
@Composable
fun ManagementRootScreen(component: ManagementRootComponent) {
    Children(
        stack = component.stack,
        animation = stackAnimation(fade())
    ) { child ->
        when (val instance = child.instance) {
            is ManagementRootComponent.Child.Main -> MainManagementScreen(instance.component)
            is ManagementRootComponent.Child.EditAuthSettings -> EditAuthSettingsScreen(instance.component)
            is ManagementRootComponent.Child.EditGlobalSettings -> EditGlobalSettingsScreen(instance.component)
            is ManagementRootComponent.Child.EditSecuritySettings -> EditSecuritySettingsScreen(instance.component)
            is ManagementRootComponent.Child.GlobalUserList -> GlobalUserListScreen(instance.component)
            is ManagementRootComponent.Child.CreateUser -> CreateUserScreen(instance.component)
            is ManagementRootComponent.Child.UserDetail -> UserDetailScreen(instance.component)
            is ManagementRootComponent.Child.UserSessionList -> UserSessionListScreen(instance.component)
            is ManagementRootComponent.Child.UserIdentifierList -> UserIdentifierListScreen(instance.component)
            is ManagementRootComponent.Child.AuditEventList -> AuditEventListScreen(instance.component)
            is ManagementRootComponent.Child.AuditEventDetail -> AuditEventDetailScreen(instance.component)
            is ManagementRootComponent.Child.GlobalSessionList -> GlobalSessionListScreen(instance.component)
            is ManagementRootComponent.Child.SessionDetail -> SessionDetailScreen(instance.component)
            is ManagementRootComponent.Child.GlobalIdentifierList -> GlobalIdentifierListScreen(instance.component)
            is ManagementRootComponent.Child.IdentifierDetail -> IdentifierDetailScreen(instance.component)
        }
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
@ScreenSizePreviews
@Composable
private fun ScreenSizePreview() {
    ScreenPreviewContainer {
        ManagementRootScreenPreviewContent()
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        ManagementRootScreenPreviewContent()
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        ManagementRootScreenPreviewContent()
    }
}