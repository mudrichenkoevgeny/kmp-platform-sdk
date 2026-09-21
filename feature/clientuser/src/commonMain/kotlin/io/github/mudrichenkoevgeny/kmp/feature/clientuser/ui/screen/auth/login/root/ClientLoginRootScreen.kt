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
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.DialogSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.mock.ui.screen.auth.login.root.ClientLoginRootComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.phone.LoginByPhoneScreen
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.registration.email.RegistrationByEmailScreen
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.unlock.root.UnlockRootScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.email.LoginByEmailScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.pendingdeletion.PendingDeletionScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.root.LoginRootContainer
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.totp.LoginByTotpScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.welcome.LoginWelcomeScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.resetpassword.ResetEmailPasswordScreen

@Composable
fun ClientLoginRootScreen(component: ClientLoginRootComponent) {
    LoginRootContainer(
        stack = component.stack,
        onDismiss = component::onDismiss
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
@DialogSizePreviews
@Composable
private fun DialogSizePreview() {
    DialogPreviewContainer {
        ClientLoginRootScreenPreviewContent()
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreview() {
    DialogPreviewContainer {
        ClientLoginRootScreenPreviewContent()
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    DialogPreviewContainer {
        ClientLoginRootScreenPreviewContent()
    }
}