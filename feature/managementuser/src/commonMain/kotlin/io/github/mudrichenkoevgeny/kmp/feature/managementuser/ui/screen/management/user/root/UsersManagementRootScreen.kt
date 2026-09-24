package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.root

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
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.user.UsersManagementRootComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.identifier.userlist.UserIdentifierListScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.userlist.UserSessionListScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.create.CreateUserScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.detail.UserDetailScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.main.UsersManagementMainScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.detail.IdentifierDetailScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.detail.SessionDetailScreen

@Composable
fun UsersManagementRootScreen(component: UsersManagementRootComponent) {
    Children(
        stack = component.stack,
        animation = stackAnimation(fade())
    ) { child ->
        when (val instance = child.instance) {
            is UsersManagementRootComponent.Child.Main -> UsersManagementMainScreen(instance.component)
            is UsersManagementRootComponent.Child.Detail -> UserDetailScreen(instance.component)
            is UsersManagementRootComponent.Child.Create -> CreateUserScreen(instance.component)
            is UsersManagementRootComponent.Child.UserSessionList -> UserSessionListScreen(instance.component)
            is UsersManagementRootComponent.Child.SessionDetail -> SessionDetailScreen(instance.component)
            is UsersManagementRootComponent.Child.Identifiers -> UserIdentifierListScreen(instance.component)
            is UsersManagementRootComponent.Child.IdentifierDetail -> IdentifierDetailScreen(instance.component)
        }
    }
}

@InternalApi
@Composable
private fun UsersManagementRootScreenPreviewContent() {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        UsersManagementRootScreen(
            component = UsersManagementRootComponentMock()
        )
    }
}

@InternalApi
@Preview(showBackground = true)
@Composable
private fun UsersManagementRootScreenPreview() {
    ScreenPreviewContainer {
        UsersManagementRootScreenPreviewContent()
    }
}

@InternalApi
@ScreenSizePreviews
@Composable
private fun ScreenSizePreview() {
    ScreenPreviewContainer {
        UsersManagementRootScreenPreviewContent()
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        UsersManagementRootScreenPreviewContent()
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        UsersManagementRootScreenPreviewContent()
    }
}
