package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.auth.login.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalCommonComponent
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.di.commonComponentMock
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.DialogPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.DialogSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.auth.login.root.ManagementLoginRootComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.email.LoginByEmailScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.pendingdeletion.PendingDeletionScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.root.LoginRootContainer
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.totp.LoginByTotpScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.welcome.LoginWelcomeScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.resetpassword.ResetEmailPasswordScreen

@Composable
fun ManagementLoginRootScreen(component: ManagementLoginRootComponent) {
    LoginRootContainer(
        stack = component.stack,
        onDismiss = component::onDismiss
    ) { instance ->
        when (instance) {
            is ManagementLoginRootComponent.Child.Welcome -> LoginWelcomeScreen(instance.component)
            is ManagementLoginRootComponent.Child.LoginByEmail -> LoginByEmailScreen(instance.component)
            is ManagementLoginRootComponent.Child.ResetEmailPassword -> ResetEmailPasswordScreen(instance.component)
            is ManagementLoginRootComponent.Child.LoginByTotp -> LoginByTotpScreen(instance.component)
            is ManagementLoginRootComponent.Child.PendingDeletion -> PendingDeletionScreen(instance.component)
        }
    }
}

@InternalApi
@Composable
private fun ManagementLoginRootScreenPreviewContent() {
    CompositionLocalProvider(
        LocalCommonComponent provides commonComponentMock(),
        LocalErrorParser provides AppErrorParserMock
    ) {
        ManagementLoginRootScreen(
            component = ManagementLoginRootComponentMock()
        )
    }
}

@InternalApi
@Preview(showBackground = true)
@Composable
private fun ManagementLoginRootScreenPreview() {
    DialogPreviewContainer {
        ManagementLoginRootScreenPreviewContent()
    }
}

@InternalApi
@DialogSizePreviews
@Composable
private fun DialogSizePreview() {
    DialogPreviewContainer {
        ManagementLoginRootScreenPreviewContent()
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreview() {
    DialogPreviewContainer {
        ManagementLoginRootScreenPreviewContent()
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    DialogPreviewContainer {
        ManagementLoginRootScreenPreviewContent()
    }
}