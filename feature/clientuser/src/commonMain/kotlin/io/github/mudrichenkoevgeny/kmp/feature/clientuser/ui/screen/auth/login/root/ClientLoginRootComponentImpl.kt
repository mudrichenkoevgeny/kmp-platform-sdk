package io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackDispatcher
import io.github.mudrichenkoevgeny.kmp.core.security.di.SecurityComponent
import io.github.mudrichenkoevgeny.kmp.core.settings.di.SettingsComponent
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.di.ClientUserComponent
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.ClientLoginDestination
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.phone.LoginByPhoneComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.phone.LoginByPhoneStepConfig
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.registration.email.RegistrationByEmailComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.registration.email.RegistrationByEmailStepConfig
import io.github.mudrichenkoevgeny.kmp.feature.user.model.apptype.AppType
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.email.LoginByEmailComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.pendingdeletion.PendingDeletionComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.totp.LoginByTotpComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.totp.LoginByTotpParams
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.welcome.LoginWelcomeComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.resetpassword.ResetEmailPasswordComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.AccountUnlockParams
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.root.UnlockRootComponentImpl

class ClientLoginRootComponentImpl(
    componentContext: ComponentContext,
    initialDestination: ClientLoginDestination = ClientLoginDestination.Welcome,
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
            initialConfiguration = initialDestination,
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
                externalLauncher = clientUserComponent.commonComponent.externalLauncher,
                getOpenGlobalSettingsUseCase = settingsComponent.getGlobalSettingsUseCase,
                getAvailableUserAuthProvidersUseCase = clientUserComponent.getAvailableUserAuthProvidersUseCase,
                loginByGoogleUseCase = clientUserComponent.loginByGoogleUseCase,
                onNavigateToLoginByEmail = { navigation.bringToFront(ClientLoginDestination.LoginByEmail) },
                onNavigateToLoginByPhone = { navigation.bringToFront(ClientLoginDestination.LoginByPhone()) },
                onNavigateToTotp = { mfaToken -> navigation.bringToFront(ClientLoginDestination.LoginByTotp(LoginByTotpParams(mfaToken))) },
                onNavigateToPendingDeletion = { navigation.bringToFront(ClientLoginDestination.PendingDeletion) },
                onNavigateToAccountUnlock = { lockoutType, lockoutUntil ->
                    navigation.bringToFront(ClientLoginDestination.AccountUnlock(AccountUnlockParams(lockoutType, lockoutUntil)))
                },
                onFinished = onFinished
            )
        )
        is ClientLoginDestination.LoginByEmail -> ClientLoginRootComponent.Child.LoginByEmail(
            LoginByEmailComponentImpl(
                componentContext = context,
                appType = AppType.CLIENT,
                loginByEmailUseCase = clientUserComponent.loginByEmailUseCase,
                onNavigateToRegistrationByEmail = { navigation.bringToFront(ClientLoginDestination.RegistrationByEmail()) },
                onNavigateToForgotPassword = { navigation.bringToFront(ClientLoginDestination.ResetEmailPassword) },
                onNavigateToTotp = { mfaToken -> navigation.bringToFront(ClientLoginDestination.LoginByTotp(LoginByTotpParams(mfaToken))) },
                onNavigateToPendingDeletion = { navigation.bringToFront(ClientLoginDestination.PendingDeletion) },
                onNavigateToAccountUnlock = { lockoutType, lockoutUntil ->
                    navigation.bringToFront(ClientLoginDestination.AccountUnlock(AccountUnlockParams(lockoutType, lockoutUntil)))
                },
                onBack = { if ((backHandler as? BackDispatcher)?.back() != true) navigation.pop() },
                onFinished = onFinished
            )
        )
        is ClientLoginDestination.LoginByPhone -> ClientLoginRootComponent.Child.LoginByPhone(
            LoginByPhoneComponentImpl(
                componentContext = context,
                initialConfig = if (config.params.isConfirmStep) {
                    LoginByPhoneStepConfig.CodeInput(phone = config.params.phone ?: "")
                } else if (config.params.phone != null) {
                    LoginByPhoneStepConfig.PhoneInput(initialPhone = config.params.phone)
                } else {
                    LoginByPhoneStepConfig.PhoneInput()
                },
                loginRepository = clientUserComponent.loginRepository,
                sendLoginConfirmationToPhoneUseCase = clientUserComponent.sendLoginConfirmationToPhoneUseCase,
                loginByPhoneUseCase = clientUserComponent.loginByPhoneUseCase,
                onNavigateToTotp = { mfaToken -> navigation.bringToFront(ClientLoginDestination.LoginByTotp(LoginByTotpParams(mfaToken))) },
                onNavigateToPendingDeletion = { navigation.bringToFront(ClientLoginDestination.PendingDeletion) },
                onNavigateToAccountUnlock = { lockoutType, lockoutUntil ->
                    navigation.bringToFront(ClientLoginDestination.AccountUnlock(AccountUnlockParams(lockoutType, lockoutUntil)))
                },
                onBack = { if ((backHandler as? BackDispatcher)?.back() != true) navigation.pop() },
                onFinished = onFinished
            )
        )
        is ClientLoginDestination.RegistrationByEmail -> ClientLoginRootComponent.Child.RegistrationByEmail(
            RegistrationByEmailComponentImpl(
                componentContext = context,
                initialConfig = if (config.params.isConfirmStep) {
                    RegistrationByEmailStepConfig.ConfirmInput(email = config.params.email ?: "")
                } else if (config.params.email != null) {
                    RegistrationByEmailStepConfig.EmailInput(initialEmail = config.params.email)
                } else {
                    RegistrationByEmailStepConfig.EmailInput()
                },
                registrationRepository = clientUserComponent.registrationRepository,
                sendRegistrationConfirmationToEmailUseCase = clientUserComponent.sendRegistrationConfirmationToEmailUseCase,
                registrationByEmailUseCase = clientUserComponent.registrationByEmailUseCase,
                validatePasswordUseCase = securityComponent.validatePasswordUseCase,
                onBack = { if ((backHandler as? BackDispatcher)?.back() != true) navigation.pop() },
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
                onBack = { if ((backHandler as? BackDispatcher)?.back() != true) navigation.pop() },
                onFinished = onFinished
            )
        )
        is ClientLoginDestination.LoginByTotp -> ClientLoginRootComponent.Child.LoginByTotp(
            LoginByTotpComponentImpl(
                componentContext = context,
                params = config.params,
                loginByTotpUseCase = clientUserComponent.loginByTotpUseCase,
                loginByTotpRecoveryCodeUseCase = clientUserComponent.loginByTotpRecoveryCodeUseCase,
                onNavigateToPendingDeletion = { navigation.bringToFront(ClientLoginDestination.PendingDeletion) },
                onBack = { if ((backHandler as? BackDispatcher)?.back() != true) navigation.pop() },
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
        is ClientLoginDestination.AccountUnlock -> ClientLoginRootComponent.Child.AccountUnlock(
            UnlockRootComponentImpl(
                componentContext = context,
                params = config.params,
                getUserIdentifiersUseCase = clientUserComponent.getUserIdentifiersUseCase,
                unlockByGoogleUseCase = clientUserComponent.unlockByGoogleUseCase,
                sendUnlockEmailConfirmationUseCase = clientUserComponent.sendUnlockEmailConfirmationUseCase,
                sendUnlockPhoneConfirmationUseCase = clientUserComponent.sendUnlockPhoneConfirmationUseCase,
                unlockByEmailUseCase = clientUserComponent.unlockByEmailUseCase,
                unlockByPhoneUseCase = clientUserComponent.unlockByPhoneUseCase,
                onFinished = { navigation.pop() }
            )
        )
    }

    override fun onDismiss() = onFinished()
}
