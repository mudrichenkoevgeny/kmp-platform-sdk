package io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.security.di.SecurityComponent
import io.github.mudrichenkoevgeny.kmp.core.settings.di.SettingsComponent
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.di.ClientUserComponent
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.ClientLoginDestination
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.registration.email.RegistrationByEmailComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.model.apptype.AppType
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.email.LoginByEmailComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.phone.LoginByPhoneComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.pendingdeletion.PendingDeletionComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.totp.LoginByTotpComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.welcome.LoginWelcomeComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.resetpassword.ResetEmailPasswordComponentImpl

class ClientLoginRootComponentImpl(
    componentContext: ComponentContext,
    private val settingsComponent: SettingsComponent,
    private val securityComponent: SecurityComponent,
    private val clientUserComponent: ClientUserComponent,
    private val onFinished: () -> Unit
) : ClientLoginRootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<ClientLoginDestination>()

    override val stack: Value<ChildStack<ClientLoginDestination, ClientLoginRootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = ClientLoginDestination.serializer(),
            initialConfiguration = ClientLoginDestination.Welcome,
            handleBackButton = true,
            childFactory = ::createChild
        )

    @OptIn(DelicateDecomposeApi::class)
    private fun createChild(
        config: ClientLoginDestination,
        context: ComponentContext
    ): ClientLoginRootComponent.Child = when (config) {
        is ClientLoginDestination.Welcome -> ClientLoginRootComponent.Child.Welcome(
            LoginWelcomeComponentImpl(
                componentContext = context,
                appType = AppType.CLIENT,
                externalLauncher = clientUserComponent.commonComponent.externalLauncher,
                getOpenGlobalSettingsUseCase = settingsComponent.getGlobalSettingsUseCase,
                getAvailableUserAuthProvidersUseCase = clientUserComponent.getAvailableUserAuthProvidersUseCase,
                loginByGoogleUseCase = clientUserComponent.loginByGoogleUseCase,
                onNavigateToLoginByEmail = { navigation.push(ClientLoginDestination.LoginByEmail) },
                onNavigateToLoginByPhone = { navigation.push(ClientLoginDestination.LoginByPhone) },
                onNavigateToTotp = { mfaToken -> navigation.push(ClientLoginDestination.LoginByTotp(mfaToken)) },
                onNavigateToPendingDeletion = { navigation.push(ClientLoginDestination.PendingDeletion) },
                onFinished = onFinished
            )
        )
        is ClientLoginDestination.LoginByEmail -> ClientLoginRootComponent.Child.LoginByEmail(
            LoginByEmailComponentImpl(
                componentContext = context,
                appType = AppType.CLIENT,
                loginByEmailUseCase = clientUserComponent.loginByEmailUseCase,
                onNavigateToRegistrationByEmail = { navigation.push(ClientLoginDestination.RegistrationByEmail) },
                onNavigateToForgotPassword = { navigation.push(ClientLoginDestination.ResetEmailPassword) },
                onNavigateToTotp = { mfaToken -> navigation.push(ClientLoginDestination.LoginByTotp(mfaToken)) },
                onNavigateToPendingDeletion = { navigation.push(ClientLoginDestination.PendingDeletion) },
                onBack = navigation::pop,
                onFinished = onFinished
            )
        )
        is ClientLoginDestination.LoginByPhone -> ClientLoginRootComponent.Child.LoginByPhone(
            LoginByPhoneComponentImpl(
                componentContext = context,
                loginRepository = clientUserComponent.loginRepository,
                sendLoginConfirmationToPhoneUseCase = clientUserComponent.sendLoginConfirmationToPhoneUseCase,
                loginByPhoneUseCase = clientUserComponent.loginByPhoneUseCase,
                onNavigateToTotp = { mfaToken -> navigation.push(ClientLoginDestination.LoginByTotp(mfaToken)) },
                onNavigateToPendingDeletion = { navigation.push(ClientLoginDestination.PendingDeletion) },
                onBack = navigation::pop,
                onFinished = onFinished
            )
        )
        is ClientLoginDestination.RegistrationByEmail -> ClientLoginRootComponent.Child.RegistrationByEmail(
            RegistrationByEmailComponentImpl(
                componentContext = context,
                registrationRepository = clientUserComponent.registrationRepository,
                sendRegistrationConfirmationToEmailUseCase = clientUserComponent.sendRegistrationConfirmationToEmailUseCase,
                registrationByEmailUseCase = clientUserComponent.registrationByEmailUseCase,
                validatePasswordUseCase = securityComponent.validatePasswordUseCase,
                onBack = navigation::pop,
                onFinished = onFinished
            )
        )
        is ClientLoginDestination.ResetEmailPassword -> ClientLoginRootComponent.Child.ResetEmailPassword(
            ResetEmailPasswordComponentImpl(
                componentContext = context,
                resetPasswordRepository = clientUserComponent.passwordRepository,
                sendResetPasswordConfirmationToEmailUseCase = clientUserComponent.sendResetPasswordConfirmationToEmailUseCase,
                resetEmailPasswordUseCase = clientUserComponent.resetEmailPasswordUseCase,
                validatePasswordUseCase = securityComponent.validatePasswordUseCase,
                onBack = navigation::pop,
                onFinished = onFinished
            )
        )
        is ClientLoginDestination.LoginByTotp -> ClientLoginRootComponent.Child.LoginByTotp(
            LoginByTotpComponentImpl(
                componentContext = context,
                mfaToken = config.mfaToken,
                loginByTotpUseCase = clientUserComponent.loginByTotpUseCase,
                loginByTotpRecoveryCodeUseCase = clientUserComponent.loginByTotpRecoveryCodeUseCase,
                onNavigateToPendingDeletion = { navigation.push(ClientLoginDestination.PendingDeletion) },
                onBack = navigation::pop,
                onFinished = onFinished
            )
        )
        is ClientLoginDestination.PendingDeletion -> ClientLoginRootComponent.Child.PendingDeletion(
            PendingDeletionComponentImpl(
                componentContext = context,
                restoreUserUseCase = clientUserComponent.restoreUserUseCase,
                logoutUseCase = clientUserComponent.logoutUseCase,
                onRestoreSuccess = onFinished,
                onSignOut = { navigation.pop() }
            )
        )
    }

    override fun onDismiss() = onFinished()
}