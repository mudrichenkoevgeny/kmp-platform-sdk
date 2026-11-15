package io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.root

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
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.mock.ui.screen.auth.login.root.ClientLoginRootComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.phone.LoginByPhoneScreen
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.registration.email.RegistrationByEmailScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.email.LoginByEmailScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.pendingdeletion.PendingDeletionScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.root.LoginRootContainer
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.totp.LoginByTotpScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.welcome.LoginWelcomeScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.resetpassword.ResetEmailPasswordScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.root.UnlockRootScreen

/**
 * Root Composable host for client login dialog flow.
 *
 * @param component Decompose root component for login flow navigation.
 * @param isDismissible Whether the dialog can be dismissed by user interaction.
 * @param isFullScreen Whether the login dialog should be displayed in full screen.
 */
@Composable
public fun ClientLoginRootScreen(
    component: ClientLoginRootComponent,
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
            is ClientLoginRootComponent.Child.Welcome -> LoginWelcomeScreen(instance.component)
            is ClientLoginRootComponent.Child.LoginByEmail -> LoginByEmailScreen(instance.component)
            is ClientLoginRootComponent.Child.LoginByPhone -> LoginByPhoneScreen(instance.component)
            is ClientLoginRootComponent.Child.LoginByTotp -> LoginByTotpScreen(instance.component)
            is ClientLoginRootComponent.Child.RegistrationByEmail -> RegistrationByEmailScreen(instance.component)
            is ClientLoginRootComponent.Child.ResetEmailPassword -> ResetEmailPasswordScreen(instance.component)
            is ClientLoginRootComponent.Child.PendingDeletion -> PendingDeletionScreen(instance.component)
            is ClientLoginRootComponent.Child.AccountUnlock -> UnlockRootScreen(instance.component)
        }
    }
}

@InternalApi
@Composable
private fun ClientLoginRootScreenPreviewContent() {
    CompositionLocalProvider(
        LocalCommonComponent provides commonComponentMock(),
        LocalErrorParser provides AppErrorParserMock
    ) {
        ClientLoginRootScreen(
            component = ClientLoginRootComponentMock()
        )
    }
}

@InternalApi
@Preview(showBackground = true)
@Composable
private fun ClientLoginRootScreenPreview() {
    DialogPreviewContainer {
        ClientLoginRootScreenPreviewContent()
    }
}

@InternalApi
@PreviewDialogSizes
@Composable
private fun DialogSizePreview() {
    DialogPreviewContainer {
        ClientLoginRootScreenPreviewContent()
    }
}

@InternalApi
@PreviewThemes
@Composable
private fun ThemePreview() {
    DialogPreviewContainer {
        ClientLoginRootScreenPreviewContent()
    }
}

@InternalApi
@PreviewFontScales
@Composable
private fun FontScalePreview() {
    DialogPreviewContainer {
        ClientLoginRootScreenPreviewContent()
    }
}
