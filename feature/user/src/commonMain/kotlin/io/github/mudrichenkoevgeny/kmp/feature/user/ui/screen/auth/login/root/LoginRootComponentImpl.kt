package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.root

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
import io.github.mudrichenkoevgeny.kmp.feature.user.di.UserComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.LoginDestination
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.email.LoginByEmailComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.phone.LoginByPhoneComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.welcome.LoginWelcomeComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.password.ResetEmailPasswordComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.registration.email.RegistrationByEmailComponentImpl

class LoginRootComponentImpl(
    componentContext: ComponentContext,
    private val settingsComponent: SettingsComponent,
    private val securityComponent: SecurityComponent,
    private val userComponent: UserComponent,
    private val onFinished: () -> Unit
) : LoginRootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<LoginDestination>()

    override val stack: Value<ChildStack<LoginDestination, LoginRootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = LoginDestination.serializer(),
            initialConfiguration = LoginDestination.Welcome,
            handleBackButton = true,
            childFactory = ::createChild
        )

    @OptIn(DelicateDecomposeApi::class)
    private fun createChild(
        config: LoginDestination,
        context: ComponentContext
    ): LoginRootComponent.Child = when (config) {
        is LoginDestination.Welcome -> LoginRootComponent.Child.Welcome(
            LoginWelcomeComponentImpl(
                componentContext = context,
                externalLauncher = userComponent.commonComponent.externalLauncher,
                getGlobalSettingsUseCase = settingsComponent.getGlobalSettingsUseCase,
                getAvailableUserAuthProvidersUseCase = userComponent.getAvailableUserAuthProvidersUseCase,
                loginByGoogleUseCase = userComponent.loginByGoogleUseCase,
                onNavigateToLoginByEmail = { navigation.push(LoginDestination.LoginByEmail) },
                onNavigateToLoginByPhone = { navigation.push(LoginDestination.LoginByPhone) },
                onFinished = onFinished
            )
        )
        is LoginDestination.LoginByEmail -> LoginRootComponent.Child.LoginByEmail(
            LoginByEmailComponentImpl(
                componentContext = context,
                loginByEmailUseCase = userComponent.loginByEmailUseCase,
                validatePasswordUseCase = securityComponent.validatePasswordUseCase,
                onNavigateToRegistrationByEmail = { navigation.push(LoginDestination.RegistrationByEmail) },
                onNavigateToForgotPassword = { navigation.push(LoginDestination.ResetEmailPassword) },
                onBack = navigation::pop,
                onFinished = onFinished
            )
        )
        is LoginDestination.LoginByPhone -> LoginRootComponent.Child.LoginByPhone(
            LoginByPhoneComponentImpl(
                componentContext = context,
                loginRepository = userComponent.loginRepository,
                sendLoginConfirmationToPhoneUseCase = userComponent.sendLoginConfirmationToPhoneUseCase,
                loginByPhoneUseCase = userComponent.loginByPhoneUseCase,
                onBack = navigation::pop,
                onFinished = onFinished
            )
        )
        is LoginDestination.RegistrationByEmail -> LoginRootComponent.Child.RegistrationByEmail(
            RegistrationByEmailComponentImpl(
                componentContext = context,
                registrationRepository = userComponent.registrationRepository,
                sendRegistrationConfirmationToEmailUseCase = userComponent.sendRegistrationConfirmationToEmailUseCase,
                registrationByEmailUseCase = userComponent.registrationByEmailUseCase,
                validatePasswordUseCase = securityComponent.validatePasswordUseCase,
                onBack = navigation::pop,
                onFinished = onFinished
            )
        )
        is LoginDestination.ResetEmailPassword -> LoginRootComponent.Child.ResetEmailPassword(
            ResetEmailPasswordComponentImpl(
                componentContext = context,
                passwordRepository = userComponent.passwordRepository,
                sendResetPasswordConfirmationToEmailUseCase = userComponent.sendResetPasswordConfirmationToEmailUseCase,
                resetEmailPasswordUseCase = userComponent.resetEmailPasswordUseCase,
                validatePasswordUseCase = securityComponent.validatePasswordUseCase,
                onBack = navigation::pop,
                onFinished = onFinished
            )
        )
    }

    override fun onDismiss() = onFinished()
}