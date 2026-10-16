package io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.navigate
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackDispatcher
import io.github.mudrichenkoevgeny.kmp.core.security.di.SecurityComponent
import io.github.mudrichenkoevgeny.kmp.core.settings.di.SettingsComponent
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.di.ClientUserComponent
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.phone.LoginByPhoneComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.registration.email.RegistrationByEmailComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.model.apptype.AppType
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.email.LoginByEmailComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.pendingdeletion.PendingDeletionComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.totp.LoginByTotpComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.welcome.LoginWelcomeComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.resetpassword.ResetEmailPasswordComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.root.UnlockRootComponentImpl
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.route.client.ClientAuthNavigation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.auth.login.totp.LoginByTotpParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.auth.unlock.methodselection.UnlockMethodSelectionParams

class ClientLoginRootComponentImpl(
    componentContext: ComponentContext,
    private val settingsComponent: SettingsComponent,
    private val securityComponent: SecurityComponent,
    private val clientUserComponent: ClientUserComponent,
    private val onFinished: () -> Unit
) : ClientLoginRootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<ClientAuthNavigation>()

    override val stack: Value<ChildStack<ClientAuthNavigation, ClientLoginRootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = ClientAuthNavigation.serializer(),
            initialConfiguration = ClientAuthNavigation.Main,
            handleBackButton = true,
            childFactory = ::createChild
        )

    override fun navigateTo(destination: ClientAuthNavigation) {
        navigation.bringToFront(destination)
    }

    private fun handleBack(parentDestination: ClientAuthNavigation? = ClientAuthNavigation.Main) {
        if ((backHandler as? BackDispatcher)?.back() == true) {
            return
        }
        val items = stack.value.items
        val previousConfig = if (items.size >= 2) items[items.size - 2].configuration else null

        if (previousConfig != null && (parentDestination == null || previousConfig == parentDestination)) {
            navigation.pop()
        } else if (parentDestination != null && parentDestination != stack.value.active.configuration) {
            navigation.navigate { currentStack ->
                currentStack.dropLast(1) + parentDestination
            }
        } else {
            onFinished()
        }
    }

    @OptIn(DelicateDecomposeApi::class)
    private fun createChild(
        config: ClientAuthNavigation,
        context: ComponentContext
    ): ClientLoginRootComponent.Child = when (config) {
        ClientAuthNavigation.Auth -> createChild(ClientAuthNavigation.Main, context)
        ClientAuthNavigation.Main -> ClientLoginRootComponent.Child.Welcome(
            LoginWelcomeComponentImpl(
                componentContext = context,
                externalLauncher = clientUserComponent.commonComponent.externalLauncher,
                getOpenGlobalSettingsUseCase = settingsComponent.getGlobalSettingsUseCase,
                getAvailableUserAuthProvidersUseCase = clientUserComponent.getAvailableUserAuthProvidersUseCase,
                loginByGoogleUseCase = clientUserComponent.loginByGoogleUseCase,
                onNavigateToLoginByEmail = { navigation.bringToFront(ClientAuthNavigation.LoginByEmail()) },
                onNavigateToLoginByPhone = { navigation.bringToFront(ClientAuthNavigation.LoginByPhone()) },
                onNavigateToTotp = { mfaToken -> navigation.bringToFront(ClientAuthNavigation.LoginByTotp(LoginByTotpParams(mfaToken = mfaToken))) },
                onNavigateToPendingDeletion = { navigation.bringToFront(ClientAuthNavigation.PendingDeletion) },
                onNavigateToAccountUnlock = { lockoutType, lockoutUntil ->
                    navigation.bringToFront(
                        ClientAuthNavigation.UnlockMethodSelection(
                            UnlockMethodSelectionParams(accountLockoutType = lockoutType, temporaryLockoutUntil = lockoutUntil)
                        )
                    )
                },
                onFinished = onFinished
            )
        )
        is ClientAuthNavigation.LoginByEmail -> ClientLoginRootComponent.Child.LoginByEmail(
            LoginByEmailComponentImpl(
                componentContext = context,
                appType = AppType.CLIENT,
                params = config.params,
                loginByEmailUseCase = clientUserComponent.loginByEmailUseCase,
                onNavigateToRegistrationByEmail = { navigation.bringToFront(ClientAuthNavigation.Registration()) },
                onNavigateToForgotPassword = { navigation.bringToFront(ClientAuthNavigation.ResetPassword()) },
                onNavigateToTotp = { mfaToken -> navigation.bringToFront(ClientAuthNavigation.LoginByTotp(LoginByTotpParams(mfaToken = mfaToken))) },
                onNavigateToPendingDeletion = { navigation.bringToFront(ClientAuthNavigation.PendingDeletion) },
                onNavigateToAccountUnlock = { lockoutType, lockoutUntil ->
                    navigation.bringToFront(
                        ClientAuthNavigation.UnlockMethodSelection(
                            UnlockMethodSelectionParams(accountLockoutType = lockoutType, temporaryLockoutUntil = lockoutUntil)
                        )
                    )
                },
                onBack = { handleBack(ClientAuthNavigation.Main) },
                onFinished = onFinished
            )
        )
        is ClientAuthNavigation.LoginByPhone -> ClientLoginRootComponent.Child.LoginByPhone(
            LoginByPhoneComponentImpl(
                componentContext = context,
                params = config.params,
                loginRepository = clientUserComponent.loginRepository,
                sendLoginConfirmationToPhoneUseCase = clientUserComponent.sendLoginConfirmationToPhoneUseCase,
                loginByPhoneUseCase = clientUserComponent.loginByPhoneUseCase,
                onNavigateToTotp = { mfaToken -> navigation.bringToFront(ClientAuthNavigation.LoginByTotp(LoginByTotpParams(mfaToken = mfaToken))) },
                onNavigateToPendingDeletion = { navigation.bringToFront(ClientAuthNavigation.PendingDeletion) },
                onNavigateToAccountUnlock = { lockoutType, lockoutUntil ->
                    navigation.bringToFront(
                        ClientAuthNavigation.UnlockMethodSelection(
                            UnlockMethodSelectionParams(accountLockoutType = lockoutType, temporaryLockoutUntil = lockoutUntil)
                        )
                    )
                },
                onBack = { handleBack(ClientAuthNavigation.Main) },
                onFinished = onFinished
            )
        )
        is ClientAuthNavigation.Registration -> ClientLoginRootComponent.Child.RegistrationByEmail(
            RegistrationByEmailComponentImpl(
                componentContext = context,
                params = config.params,
                registrationRepository = clientUserComponent.registrationRepository,
                sendRegistrationConfirmationToEmailUseCase = clientUserComponent.sendRegistrationConfirmationToEmailUseCase,
                registrationByEmailUseCase = clientUserComponent.registrationByEmailUseCase,
                validatePasswordUseCase = securityComponent.validatePasswordUseCase,
                onBack = { handleBack(ClientAuthNavigation.Main) },
                onFinished = onFinished
            )
        )
        is ClientAuthNavigation.ResetPassword -> ClientLoginRootComponent.Child.ResetEmailPassword(
            ResetEmailPasswordComponentImpl(
                componentContext = context,
                params = config.params,
                resetPasswordRepository = clientUserComponent.passwordRepository,
                sendResetPasswordConfirmationToEmailUseCase = clientUserComponent.sendResetPasswordConfirmationToEmailUseCase,
                resetEmailPasswordUseCase = clientUserComponent.resetEmailPasswordUseCase,
                validatePasswordUseCase = securityComponent.validatePasswordUseCase,
                onBack = { handleBack(ClientAuthNavigation.Main) },
                onFinished = onFinished
            )
        )
        is ClientAuthNavigation.LoginByTotp -> ClientLoginRootComponent.Child.LoginByTotp(
            LoginByTotpComponentImpl(
                componentContext = context,
                params = config.params,
                loginByTotpUseCase = clientUserComponent.loginByTotpUseCase,
                loginByTotpRecoveryCodeUseCase = clientUserComponent.loginByTotpRecoveryCodeUseCase,
                onNavigateToPendingDeletion = { navigation.bringToFront(ClientAuthNavigation.PendingDeletion) },
                onBack = { handleBack(ClientAuthNavigation.Main) },
                onFinished = onFinished
            )
        )
        ClientAuthNavigation.PendingDeletion -> ClientLoginRootComponent.Child.PendingDeletion(
            PendingDeletionComponentImpl(
                componentContext = context,
                restoreUserUseCase = clientUserComponent.restoreUserUseCase,
                logoutUseCase = clientUserComponent.logoutUseCase,
                onRestoreSuccess = onFinished,
                onSignOut = { handleBack(ClientAuthNavigation.Main) }
            )
        )
        is ClientAuthNavigation.UnlockMethodSelection,
        is ClientAuthNavigation.UnlockTargetInput,
        is ClientAuthNavigation.UnlockOtpInput,
        is ClientAuthNavigation.UnlockSuccess -> ClientLoginRootComponent.Child.AccountUnlock(
            UnlockRootComponentImpl(
                componentContext = context,
                sendUnlockEmailConfirmationUseCase = clientUserComponent.sendUnlockEmailConfirmationUseCase,
                sendUnlockPhoneConfirmationUseCase = clientUserComponent.sendUnlockPhoneConfirmationUseCase,
                unlockByEmailUseCase = clientUserComponent.unlockByEmailUseCase,
                unlockByPhoneUseCase = clientUserComponent.unlockByPhoneUseCase,
                onFinished = { handleBack(ClientAuthNavigation.Main) }
            )
        )
    }

    override fun onDismiss() = onFinished()
}
