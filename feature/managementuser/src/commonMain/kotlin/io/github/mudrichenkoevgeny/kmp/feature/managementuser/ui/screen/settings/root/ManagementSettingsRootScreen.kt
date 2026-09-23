package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.root

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
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.ui.screen.root.AuditApiRootScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.settings.ManagementSettingsRootComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.globallist.GlobalSessionListScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.root.UsersManagementRootScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.auth.EditAuthSettingsScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.global.EditGlobalSettingsScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.main.MainManagementSettingsScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.security.EditSecuritySettingsScreen

/**
 * Root Composable for management settings stack.
 *
 * @param component Root Decompose stack component.
 */
@Composable
fun ManagementSettingsRootScreen(component: ManagementSettingsRootComponent) {
    Children(
        stack = component.stack,
        animation = stackAnimation(fade())
    ) { child ->
        when (val instance = child.instance) {
            is ManagementSettingsRootComponent.Child.Main -> MainManagementSettingsScreen(instance.component)
            is ManagementSettingsRootComponent.Child.EditAuthSettings -> EditAuthSettingsScreen(instance.component)
            is ManagementSettingsRootComponent.Child.EditGlobalSettings -> EditGlobalSettingsScreen(instance.component)
            is ManagementSettingsRootComponent.Child.EditSecuritySettings -> EditSecuritySettingsScreen(instance.component)
            is ManagementSettingsRootComponent.Child.UsersManagement -> UsersManagementRootScreen(instance.component)
            is ManagementSettingsRootComponent.Child.AuditLogs -> AuditApiRootScreen(instance.component)
            is ManagementSettingsRootComponent.Child.Sessions -> GlobalSessionListScreen(instance.component)
        }
    }
}

@InternalApi
@Composable
private fun ManagementSettingsRootScreenPreviewContent() {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        ManagementSettingsRootScreen(
            component = ManagementSettingsRootComponentMock()
        )
    }
}

@InternalApi
@Preview(showBackground = true)
@Composable
private fun ManagementSettingsRootScreenPreview() {
    ScreenPreviewContainer {
        ManagementSettingsRootScreenPreviewContent()
    }
}

@InternalApi
@ScreenSizePreviews
@Composable
private fun ScreenSizePreview() {
    ScreenPreviewContainer {
        ManagementSettingsRootScreenPreviewContent()
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        ManagementSettingsRootScreenPreviewContent()
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        ManagementSettingsRootScreenPreviewContent()
    }
}
