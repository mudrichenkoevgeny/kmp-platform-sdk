package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.auth.login.root

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
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.di.ManagementUserComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.model.apptype.AppType
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.email.LoginByEmailComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.totp.LoginByTotpComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.welcome.LoginWelcomeComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.resetpassword.ResetEmailPasswordComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.root.UnlockRootComponentImpl
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.route.management.ManagementAuthNavigation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.auth.login.totp.LoginByTotpParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.auth.unlock.methodselection.UnlockMethodSelectionParams

/**
 * Default [ManagementLoginRootComponent] implementation: manages the administrative staff login navigation stack
 * (welcome, email login, password reset, MFA/TOTP, unlock, pending deletion).
 *
 * @param componentContext Decompose [ComponentContext].
 * @param settingsComponent Core settings component providing global app settings.
 * @param securityComponent Core security component providing password policy validation.
 * @param managementUserComponent Management user feature component providing administrative auth use cases.
 * @param onFinished Invoked when staff authentication succeeds or completes.
 */
class ManagementLoginRootComponentImpl(
    componentContext: ComponentContext,
    private val settingsComponent: SettingsComponent,
    private val securityComponent: SecurityComponent,
    private val managementUserComponent: ManagementUserComponent,
    private val onFinished: () -> Unit
) : ManagementLoginRootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<ManagementAuthNavigation>()

    override val stack: Value<ChildStack<ManagementAuthNavigation, ManagementLoginRootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = ManagementAuthNavigation.serializer(),
            initialConfiguration = ManagementAuthNavigation.Main,
            handleBackButton = true,
            childFactory = ::createChild
        )

    override fun navigateTo(destination: ManagementAuthNavigation) {
        navigation.bringToFront(destination)
    }

    override fun onDismiss() {
        onFinished()
    }

    private fun handleBack(parentDestination: ManagementAuthNavigation? = ManagementAuthNavigation.Main) {
        if ((backHandler as? BackDispatcher)?.back() == true) {
            return
        }
        val items = stack.value.items
        val previousConfig = if (items.size >= 2) items[items.size - 2].configuration else null

        if (previousConfig != null) {
            navigation.pop()
        } else if (parentDestination != null && parentDestination != stack.value.active.configuration) {
            navigation.navigate { currentStack ->
                val index = currentStack.indexOfLast { it == parentDestination || it::class == parentDestination::class }
                if (index >= 0) {
                    currentStack.take(index + 1)
                } else {
                    currentStack.dropLast(1) + parentDestination
                }
            }
        } else {
            onFinished()
        }
    }

    @OptIn(DelicateDecomposeApi::class)
    private fun createChild(
        config: ManagementAuthNavigation,
        context: ComponentContext
    ): ManagementLoginRootComponent.Child = when (config) {
        ManagementAuthNavigation.Auth -> createChild(ManagementAuthNavigation.Main, context)
        ManagementAuthNavigation.Main -> ManagementLoginRootComponent.Child.Welcome(
            LoginWelcomeComponentImpl(
                componentContext = context,
                externalLauncher = managementUserComponent.commonComponent.externalLauncher,
                getOpenGlobalSettingsUseCase = settingsComponent.getGlobalSettingsUseCase,
                getAvailableUserAuthProvidersUseCase = managementUserComponent.getAvailableUserAuthProvidersUseCase,
                loginByGoogleUseCase = null,
                onNavigateToLoginByEmail = { navigation.bringToFront(ManagementAuthNavigation.LoginByEmail()) },
                onNavigateToLoginByPhone = { },
                onNavigateToTotp = { mfaToken -> navigation.bringToFront(ManagementAuthNavigation.LoginByTotp(LoginByTotpParams(mfaToken))) },
                onNavigateToPendingDeletion = { },
                onNavigateToAccountUnlock = { lockoutType, lockoutUntil ->
                    navigation.bringToFront(
                        ManagementAuthNavigation.UnlockMethodSelection(
                            UnlockMethodSelectionParams(accountLockoutType = lockoutType, temporaryLockoutUntil = lockoutUntil)
                        )
                    )
                },
                onFinished = onFinished
            )
        )
        is ManagementAuthNavigation.LoginByEmail -> ManagementLoginRootComponent.Child.LoginByEmail(
            LoginByEmailComponentImpl(
                componentContext = context,
                appType = AppType.MANAGEMENT,
                params = config.params,
                loginByEmailUseCase = managementUserComponent.loginByEmailUseCase,
                onNavigateToRegistrationByEmail = { },
                onNavigateToForgotPassword = { navigation.bringToFront(ManagementAuthNavigation.ResetPassword()) },
                onNavigateToTotp = { mfaToken -> navigation.bringToFront(ManagementAuthNavigation.LoginByTotp(LoginByTotpParams(mfaToken))) },
                onNavigateToPendingDeletion = { },
                onNavigateToAccountUnlock = { lockoutType, lockoutUntil ->
                    navigation.bringToFront(
                        ManagementAuthNavigation.UnlockMethodSelection(
                            UnlockMethodSelectionParams(accountLockoutType = lockoutType, temporaryLockoutUntil = lockoutUntil)
                        )
                    )
                },
                onBack = { handleBack(ManagementAuthNavigation.Main) },
                onFinished = onFinished
            )
        )
        is ManagementAuthNavigation.ResetPassword -> ManagementLoginRootComponent.Child.ResetEmailPassword(
            ResetEmailPasswordComponentImpl(
                componentContext = context,
                params = config.params,
                resetPasswordRepository = managementUserComponent.passwordRepository,
                sendResetPasswordConfirmationToEmailUseCase = managementUserComponent.sendResetPasswordConfirmationToEmailUseCase,
                resetEmailPasswordUseCase = managementUserComponent.resetEmailPasswordUseCase,
                validatePasswordUseCase = securityComponent.validatePasswordUseCase,
                onBack = { handleBack(ManagementAuthNavigation.Main) },
                onFinished = onFinished
            )
        )
        is ManagementAuthNavigation.LoginByTotp -> ManagementLoginRootComponent.Child.LoginByTotp(
            LoginByTotpComponentImpl(
                componentContext = context,
                params = config.params,
                loginByTotpUseCase = managementUserComponent.loginByTotpUseCase,
                loginByTotpRecoveryCodeUseCase = managementUserComponent.loginByTotpRecoveryCodeUseCase,
                onNavigateToPendingDeletion = { },
                onBack = { handleBack(ManagementAuthNavigation.Main) },
                onFinished = onFinished
            )
        )
        is ManagementAuthNavigation.UnlockMethodSelection,
        is ManagementAuthNavigation.UnlockTargetInput,
        is ManagementAuthNavigation.UnlockOtpInput,
        is ManagementAuthNavigation.UnlockSuccess -> ManagementLoginRootComponent.Child.AccountUnlock(
            UnlockRootComponentImpl(
                componentContext = context,
                sendUnlockEmailConfirmationUseCase = managementUserComponent.sendUnlockEmailConfirmationUseCase,
                sendUnlockPhoneConfirmationUseCase = managementUserComponent.sendUnlockPhoneConfirmationUseCase,
                unlockByEmailUseCase = managementUserComponent.unlockByEmailUseCase,
                unlockByPhoneUseCase = managementUserComponent.unlockByPhoneUseCase,
                onFinished = { handleBack(ManagementAuthNavigation.Main) }
            )
        )
    }
}
