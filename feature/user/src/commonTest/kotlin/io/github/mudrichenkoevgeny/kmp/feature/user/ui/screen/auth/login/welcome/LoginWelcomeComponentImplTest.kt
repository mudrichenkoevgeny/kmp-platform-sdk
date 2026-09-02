package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.welcome

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.platform.externallauncher.ExternalLauncherMock
import io.github.mudrichenkoevgeny.kmp.core.common.platform.externallauncher.ExternalLauncher
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.core.settings.mock.repository.GlobalSettingsRepositoryMock
import io.github.mudrichenkoevgeny.kmp.core.settings.usecase.GetGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.auth.google.GoogleAuthServiceMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.auth.settings.publicAuthSettingsMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.auth.data.authDataPayloadMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.login.LoginRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.settings.OpenAuthSettingsRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.auth.AuthStorageMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.user.UserStorageMock
import io.github.mudrichenkoevgeny.kmp.feature.user.model.apptype.AppType
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.LoginByGoogleUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings.GetAvailableUserAuthProvidersUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.domain.model.globalsettings.GlobalSettings
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.AvailableAuthProviders
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.auth.data.toAuthData
import kotlinx.coroutines.test.advanceUntilIdle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class LoginWelcomeComponentImplTest {

    @Test
    fun init_loadsContent_whenAuthAndGlobalSettingsSucceed() = runComponentTest {
        val context = createLoginWelcomeComponentTestContext()
        try {
            advanceUntilIdle()
            val content = assertIs<LoginWelcomeScreenState.Content>(context.component.state.value)
            assertEquals(context.expectedProviders, content.availableAuthProviders)
            assertEquals(PRIVACY_POLICY_URL, content.privacyPolicyUrl)
            assertEquals(TERMS_OF_SERVICE_URL, content.termsOfServiceUrl)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun init_showsInitializationError_whenAuthSettingsFail() = runComponentTest {
        val authRepo = OpenAuthSettingsRepositoryMock().apply {
            resultProvider = { AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE)) }
        }
        val context = createLoginWelcomeComponentTestContext(authSettingsRepository = authRepo)
        try {
            advanceUntilIdle()
            val err = assertIs<LoginWelcomeScreenState.InitializationError>(context.component.state.value)
            assertIs<CommonError.Unknown>(err.error)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onRetryInitClick_recoverAfterFailure() = runComponentTest {
        val authRepo = OpenAuthSettingsRepositoryMock().apply {
            resultProvider = { AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE)) }
        }
        val context = createLoginWelcomeComponentTestContext(authSettingsRepository = authRepo)
        try {
            advanceUntilIdle()
            assertIs<LoginWelcomeScreenState.InitializationError>(context.component.state.value)

            val settings = publicAuthSettingsMock()
            authRepo.resultProvider = { AppResult.Success(settings) }

            context.component.onRetryInitClick()
            advanceUntilIdle()
            assertIs<LoginWelcomeScreenState.Content>(context.component.state.value)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onLoginClick_email_navigatesToEmail() = runComponentTest {
        val context = createLoginWelcomeComponentTestContext()
        try {
            advanceUntilIdle()
            context.component.onLoginClick(UserAuthProvider.EMAIL)
            assertEquals(ONE_CALL, context.onNavigateToLoginByEmailCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onLoginClick_phone_navigatesToPhone() = runComponentTest {
        val context = createLoginWelcomeComponentTestContext()
        try {
            advanceUntilIdle()
            context.component.onLoginClick(UserAuthProvider.PHONE)
            assertEquals(ONE_CALL, context.onNavigateToLoginByPhoneCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onLoginClick_google_success_callsOnFinished() = runComponentTest {
        val context = createLoginWelcomeComponentTestContext()
        try {
            advanceUntilIdle()
            context.component.onLoginClick(UserAuthProvider.GOOGLE)
            advanceUntilIdle()
            assertEquals(ONE_CALL, context.onFinishedCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onLoginClick_google_error_showsActionError() = runComponentTest {
        val loginRepo = LoginRepositoryMock().apply {
            authDataResultProvider = { AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE)) }
        }
        val context = createLoginWelcomeComponentTestContext(loginRepository = loginRepo)
        try {
            advanceUntilIdle()
            context.component.onLoginClick(UserAuthProvider.GOOGLE)
            advanceUntilIdle()
            assertEquals(ZERO_CALLS, context.onFinishedCalls)
            val content = assertIs<LoginWelcomeScreenState.Content>(context.component.state.value)
            assertIs<CommonError.Unknown>(content.actionError)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onLoginClick_apple_showsExternalAuthFailed() = runComponentTest {
        val context = createLoginWelcomeComponentTestContext()
        try {
            advanceUntilIdle()
            context.component.onLoginClick(UserAuthProvider.APPLE)
            val content = assertIs<LoginWelcomeScreenState.Content>(context.component.state.value)
            assertIs<UserError.ExternalAuthFailed>(content.actionError)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onPrivacyPolicyClick_opensUrl() = runComponentTest {
        val launcher = ExternalLauncherMock()
        val context = createLoginWelcomeComponentTestContext(externalLauncher = launcher)
        try {
            advanceUntilIdle()
            context.component.onPrivacyPolicyClick()
            assertEquals(listOf(PRIVACY_POLICY_URL), launcher.openedUrls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onTermsOfServiceClick_opensUrl() = runComponentTest {
        val launcher = ExternalLauncherMock()
        val context = createLoginWelcomeComponentTestContext(externalLauncher = launcher)
        try {
            advanceUntilIdle()
            context.component.onTermsOfServiceClick()
            assertEquals(listOf(TERMS_OF_SERVICE_URL), launcher.openedUrls)
        } finally {
            context.destroy()
        }
    }

    private fun createLoginWelcomeComponentTestContext(
        authSettingsRepository: OpenAuthSettingsRepositoryMock = OpenAuthSettingsRepositoryMock().apply {
            resultProvider = { AppResult.Success(publicAuthSettingsMock()) }
        },
        globalSettingsRepository: GlobalSettingsRepositoryMock = GlobalSettingsRepositoryMock().apply {
            resultProvider = {
                AppResult.Success(
                    GlobalSettings(
                        privacyPolicyUrl = PRIVACY_POLICY_URL,
                        termsOfServiceUrl = TERMS_OF_SERVICE_URL,
                        contactSupportEmail = null
                    )
                )
            }
        },
        externalLauncher: ExternalLauncher = ExternalLauncherMock(),
        loginRepository: LoginRepositoryMock = LoginRepositoryMock().apply {
            authDataResultProvider = { AppResult.Success(authDataPayloadMock().toAuthData()) }
        }
    ): LoginWelcomeComponentTestContext {
        val lifecycle = LifecycleRegistry()
        lifecycle.resume()
        
        val context = LoginWelcomeComponentTestContext(
            lifecycle = lifecycle,
            expectedProviders = when (val authResult = authSettingsRepository.resultProvider()) {
                is AppResult.Success -> authResult.data.availableAuthProviders
                is AppResult.Error -> publicAuthSettingsMock().availableAuthProviders
            }
        )

        context.component = LoginWelcomeComponentImpl(
            componentContext = DefaultComponentContext(lifecycle),
            appType = AppType.CLIENT,
            externalLauncher = externalLauncher,
            getGlobalSettingsUseCase = GetGlobalSettingsUseCase(globalSettingsRepository),
            getAvailableUserAuthProvidersUseCase = GetAvailableUserAuthProvidersUseCase(
                appType = AppType.CLIENT,
                openAuthSettingsRepository = authSettingsRepository
            ),
            loginByGoogleUseCase = LoginByGoogleUseCase(
                authService = GoogleAuthServiceMock(),
                loginRepository = loginRepository,
                authStorage = AuthStorageMock(),
                userStorage = UserStorageMock()
            ),
            onNavigateToLoginByEmail = { context.onNavigateToLoginByEmailCalls++ },
            onNavigateToLoginByPhone = { context.onNavigateToLoginByPhoneCalls++ },
            onNavigateToTotp = { context.onNavigateToTotpCalls++ },
            onFinished = { context.onFinishedCalls++ }
        )
        
        return context
    }

    private class LoginWelcomeComponentTestContext(
        private val lifecycle: LifecycleRegistry,
        val expectedProviders: AvailableAuthProviders
    ) {
        lateinit var component: LoginWelcomeComponentImpl
        var onNavigateToLoginByEmailCalls: Int = 0
        var onNavigateToLoginByPhoneCalls: Int = 0
        var onNavigateToTotpCalls: Int = 0
        var onFinishedCalls: Int = 0

        fun destroy() = lifecycle.destroy()
    }

    private companion object {
        const val PRIVACY_POLICY_URL = "https://example.com/privacy"
        const val TERMS_OF_SERVICE_URL = "https://example.com/terms"
        const val NOT_RETRYABLE = false
        const val ZERO_CALLS = 0
        const val ONE_CALL = 1
    }
}
