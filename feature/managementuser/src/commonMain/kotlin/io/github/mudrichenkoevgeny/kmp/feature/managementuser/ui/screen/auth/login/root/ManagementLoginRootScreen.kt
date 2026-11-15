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
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewDialogSizes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewFontScales
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewThemes
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.auth.login.root.ManagementLoginRootComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.email.LoginByEmailScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.pendingdeletion.PendingDeletionScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.root.LoginRootContainer
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.totp.LoginByTotpScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.welcome.LoginWelcomeScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.resetpassword.ResetEmailPasswordScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.root.UnlockRootScreen

/**
 * Root Composable for the management user login authentication flow.
 *
 * @param component Decompose controller for managing login root navigation stack.
 * @param isDismissible True if the login modal dialog can be dismissed by tapping outside.
 * @param isFullScreen True if the login container should fill the entire screen size.
 */
@Composable
public fun ManagementLoginRootScreen(
    component: ManagementLoginRootComponent,
    isDismissible: Boolean = true,
    isFullScreen: Boolean = false
) {
    LoginRootContainer(
        stack = component.stack,
        onDismiss = component::onDismiss,
        isDismissible = isDismissible,
        isFullScreen = isFullScreen
    ) { instance ->
        when (instance) {
            is ManagementLoginRootComponent.Child.Welcome -> LoginWelcomeScreen(instance.component)
            is ManagementLoginRootComponent.Child.LoginByEmail -> LoginByEmailScreen(instance.component)
            is ManagementLoginRootComponent.Child.ResetEmailPassword -> ResetEmailPasswordScreen(instance.component)
            is ManagementLoginRootComponent.Child.LoginByTotp -> LoginByTotpScreen(instance.component)
            is ManagementLoginRootComponent.Child.PendingDeletion -> PendingDeletionScreen(instance.component)
            is ManagementLoginRootComponent.Child.AccountUnlock -> UnlockRootScreen(instance.component)
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
@PreviewDialogSizes
@Composable
private fun DialogSizePreview() {
    DialogPreviewContainer {
        ManagementLoginRootScreenPreviewContent()
    }
}

@InternalApi
@PreviewThemes
@Composable
private fun ThemePreview() {
    DialogPreviewContainer {
        ManagementLoginRootScreenPreviewContent()
    }
}

@InternalApi
@PreviewFontScales
@Composable
private fun FontScalePreview() {
    DialogPreviewContainer {
        ManagementLoginRootScreenPreviewContent()
    }
}
