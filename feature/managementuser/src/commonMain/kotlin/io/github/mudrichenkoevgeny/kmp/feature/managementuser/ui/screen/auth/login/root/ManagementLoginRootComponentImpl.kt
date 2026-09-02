package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.auth.login.root

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
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.di.ManagementUserComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.auth.login.ManagementLoginDestination
import io.github.mudrichenkoevgeny.kmp.feature.user.model.apptype.AppType
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.email.LoginByEmailComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.totp.LoginByTotpComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.welcome.LoginWelcomeComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.resetpassword.ResetEmailPasswordComponentImpl

class ManagementLoginRootComponentImpl(
    componentContext: ComponentContext,
    private val settingsComponent: SettingsComponent,
    private val securityComponent: SecurityComponent,
    private val managementUserComponent: ManagementUserComponent,
    private val onFinished: () -> Unit
) : ManagementLoginRootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<ManagementLoginDestination>()

    override val stack: Value<ChildStack<ManagementLoginDestination, ManagementLoginRootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = ManagementLoginDestination.serializer(),
            initialConfiguration = ManagementLoginDestination.Welcome,
            handleBackButton = true,
            childFactory = ::createChild
        )

    @OptIn(DelicateDecomposeApi::class)
    private fun createChild(
        config: ManagementLoginDestination,
        context: ComponentContext
    ): ManagementLoginRootComponent.Child = when (config) {
        is ManagementLoginDestination.Welcome -> ManagementLoginRootComponent.Child.Welcome(
            LoginWelcomeComponentImpl(
                componentContext = context,
                appType = AppType.MANAGEMENT,
                externalLauncher = managementUserComponent.commonComponent.externalLauncher,
                getGlobalSettingsUseCase = settingsComponent.getGlobalSettingsUseCase,
                getAvailableUserAuthProvidersUseCase = managementUserComponent.getAvailableUserAuthProvidersUseCase,
                loginByGoogleUseCase = null,
                onNavigateToLoginByEmail = { navigation.push(ManagementLoginDestination.LoginByEmail) },
                onNavigateToLoginByPhone = { },
                onNavigateToTotp = { mfaToken -> navigation.push(ManagementLoginDestination.LoginByTotp(mfaToken)) },
                onFinished = onFinished
            )
        )
        is ManagementLoginDestination.LoginByEmail -> ManagementLoginRootComponent.Child.LoginByEmail(
            LoginByEmailComponentImpl(
                componentContext = context,
                appType = AppType.MANAGEMENT,
                loginByEmailUseCase = managementUserComponent.loginByEmailUseCase,
                validatePasswordUseCase = securityComponent.validatePasswordUseCase,
                onNavigateToRegistrationByEmail = { },
                onNavigateToForgotPassword = { navigation.push(ManagementLoginDestination.ResetEmailPassword) },
                onNavigateToTotp = { mfaToken -> navigation.push(ManagementLoginDestination.LoginByTotp(mfaToken)) },
                onBack = navigation::pop,
                onFinished = onFinished
            )
        )
        is ManagementLoginDestination.ResetEmailPassword -> ManagementLoginRootComponent.Child.ResetEmailPassword(
            ResetEmailPasswordComponentImpl(
                componentContext = context,
                resetPasswordRepository = managementUserComponent.passwordRepository,
                sendResetPasswordConfirmationToEmailUseCase = managementUserComponent.sendResetPasswordConfirmationToEmailUseCase,
                resetEmailPasswordUseCase = managementUserComponent.resetEmailPasswordUseCase,
                validatePasswordUseCase = securityComponent.validatePasswordUseCase,
                onBack = navigation::pop,
                onFinished = onFinished
            )
        )
        is ManagementLoginDestination.LoginByTotp -> ManagementLoginRootComponent.Child.LoginByTotp(
            LoginByTotpComponentImpl(
                componentContext = context,
                mfaToken = config.mfaToken,
                loginByTotpUseCase = managementUserComponent.loginByTotpUseCase,
                loginByTotpRecoveryCodeUseCase = managementUserComponent.loginByTotpRecoveryCodeUseCase,
                onBack = navigation::pop,
                onFinished = onFinished
            )
        )
    }

    override fun onDismiss() = onFinished()
}