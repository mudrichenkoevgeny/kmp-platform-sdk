package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.user.UserRootComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.detail.IdentifierDetailScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.list.SelfIdentifierListScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.detail.SessionDetailScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.list.SelfSessionListScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp.main.TotpMainScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp.recovery.TotpRecoveryCodesScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.main.MainUserScreen

/**
 * Root Composable for user profile and settings stack.
 *
 * @param component Root Decompose stack component.
 * @param modifier Optional layout modifier.
 */
@Composable
fun UserRootScreen(
    component: UserRootComponent,
    modifier: Modifier = Modifier
) {
    Children(
        stack = component.stack,
        modifier = modifier,
        animation = stackAnimation(slide())
    ) { child ->
        when (val instance = child.instance) {
            is UserRootComponent.Child.Main -> MainUserScreen(instance.component)
            is UserRootComponent.Child.TotpMain -> TotpMainScreen(instance.component)
            is UserRootComponent.Child.TotpRecoveryCodes -> TotpRecoveryCodesScreen(instance.component)
            is UserRootComponent.Child.Sessions -> SelfSessionListScreen(instance.component)
            is UserRootComponent.Child.SessionDetail -> SessionDetailScreen(instance.component)
            is UserRootComponent.Child.Identifiers -> SelfIdentifierListScreen(instance.component)
            is UserRootComponent.Child.IdentifierDetail -> IdentifierDetailScreen(instance.component)
        }
    }
}

@InternalApi
@Composable
private fun UserRootScreenPreviewContent() {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        UserRootScreen(
            component = UserRootComponentMock()
        )
    }
}

@InternalApi
@Preview(showBackground = true)
@Composable
private fun UserRootScreenPreview() {
    ScreenPreviewContainer {
        UserRootScreenPreviewContent()
    }
}

@InternalApi
@ScreenSizePreviews
@Composable
private fun ScreenSizePreview() {
    ScreenPreviewContainer {
        UserRootScreenPreviewContent()
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        UserRootScreenPreviewContent()
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        UserRootScreenPreviewContent()
    }
}
