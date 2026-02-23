package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.login.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.security.di.SecurityComponent
import io.github.mudrichenkoevgeny.kmp.core.settings.di.SettingsComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.di.UserComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.login.LoginDestination
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.login.email.LoginByEmailComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.login.phone.LoginByPhoneComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.login.welcome.LoginWelcomeComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.registration.email.RegistrationByEmailComponentImpl

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
                onNavigateToLoginByEmail = { } ,
                onNavigateToLoginByPhone = { },
                onFinished = onFinished
            )
        )
        is LoginDestination.LoginByEmail -> LoginRootComponent.Child.LoginByEmail(
            LoginByEmailComponentImpl(
                componentContext = context,
                loginByEmailUseCase = userComponent.loginByEmailUseCase,
                validatePasswordUseCase = securityComponent.validatePasswordUseCase,
                onNavigateToRegistration = { },
                onNavigateToForgotPassword = { } ,
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
    }

    override fun onDismiss() = onFinished()
}