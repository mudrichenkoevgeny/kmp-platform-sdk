package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.root

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
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.user.userDetailsMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.ProfileRootComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.main.MainProfileComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.detail.IdentifierDetailScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.list.SelfIdentifierListScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.main.MainProfileScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.main.MainProfileScreenState
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.detail.SessionDetailScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.list.SelfSessionListScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp.main.TotpMainScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp.recovery.TotpRecoveryCodesScreen

@Composable
fun ProfileRootScreen(component: ProfileRootComponent) {
    Children(
        stack = component.stack,
        animation = stackAnimation(fade()),
    ) { child ->
        when (val instance = child.instance) {
            is ProfileRootComponent.Child.Main -> MainProfileScreen(instance.component)
            is ProfileRootComponent.Child.TotpMain -> TotpMainScreen(instance.component)
            is ProfileRootComponent.Child.TotpRecoveryCodes -> TotpRecoveryCodesScreen(instance.component)
            is ProfileRootComponent.Child.Sessions -> SelfSessionListScreen(instance.component)
            is ProfileRootComponent.Child.SessionDetail -> SessionDetailScreen(instance.component)
            is ProfileRootComponent.Child.Identifiers -> SelfIdentifierListScreen(instance.component)
            is ProfileRootComponent.Child.IdentifierDetail -> IdentifierDetailScreen(instance.component)
        }
    }
}

@InternalApi
@Composable
private fun ProfileRootScreenPreviewContent() {
    val mainMock = MainProfileComponentMock(
        initialState = MainProfileScreenState.Content(user = userDetailsMock())
    )
    val rootMock = ProfileRootComponentMock(
        initialChild = ProfileRootComponent.Child.Main(mainMock)
    )

    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        ProfileRootScreen(component = rootMock)
    }
}

@InternalApi
@Preview(showBackground = true)
@Composable
private fun ProfileRootScreenPreview() {
    ScreenPreviewContainer {
        ProfileRootScreenPreviewContent()
    }
}

@InternalApi
@ScreenSizePreviews
@Composable
private fun ProfileRootScreenSizePreview() {
    ScreenPreviewContainer {
        ProfileRootScreenPreviewContent()
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ProfileRootThemePreview() {
    ScreenPreviewContainer {
        ProfileRootScreenPreviewContent()
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun ProfileRootFontScalePreview() {
    ScreenPreviewContainer {
        ProfileRootScreenPreviewContent()
    }
}
