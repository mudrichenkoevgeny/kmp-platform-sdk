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
        val harness = createHarness()
        try {
            advanceUntilIdle()
            val content = assertIs<LoginWelcomeScreenState.Content>(harness.component.state.value)
            assertEquals(harness.expectedProviders, content.availableAuthProviders)
            assertEquals(PRIVACY_POLICY_URL, content.privacyPolicyUrl)
            assertEquals(TERMS_OF_SERVICE_URL, content.termsOfServiceUrl)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun init_showsInitializationError_whenAuthSettingsFail() = runComponentTest {
        val authRepo = OpenAuthSettingsRepositoryMock().apply {
            resultProvider = { AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE)) }
        }
        val harness = createHarness(authSettingsRepository = authRepo)
        try {
            advanceUntilIdle()
            val err = assertIs<LoginWelcomeScreenState.InitializationError>(harness.component.state.value)
            assertIs<CommonError.Unknown>(err.error)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onRetryInitClick_recoverAfterFailure() = runComponentTest {
        val authRepo = OpenAuthSettingsRepositoryMock().apply {
            resultProvider = { AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE)) }
        }
        val harness = createHarness(authSettingsRepository = authRepo)
        try {
            advanceUntilIdle()
            assertIs<LoginWelcomeScreenState.InitializationError>(harness.component.state.value)

            val settings = publicAuthSettingsMock()
            authRepo.resultProvider = { AppResult.Success(settings) }

            harness.component.onRetryInitClick()
            advanceUntilIdle()
            assertIs<LoginWelcomeScreenState.Content>(harness.component.state.value)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onLoginClick_email_navigatesToEmail() = runComponentTest {
        val harness = createHarness()
        try {
            advanceUntilIdle()
            harness.component.onLoginClick(UserAuthProvider.EMAIL)
            assertEquals(ONE_CALL, harness.counters.navigateEmail)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onLoginClick_phone_navigatesToPhone() = runComponentTest {
        val harness = createHarness()
        try {
            advanceUntilIdle()
            harness.component.onLoginClick(UserAuthProvider.PHONE)
            assertEquals(ONE_CALL, harness.counters.navigatePhone)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onLoginClick_google_success_callsOnFinished() = runComponentTest {
        val harness = createHarness()
        try {
            advanceUntilIdle()
            harness.component.onLoginClick(UserAuthProvider.GOOGLE)
            advanceUntilIdle()
            assertEquals(ONE_CALL, harness.counters.finished)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onLoginClick_google_error_showsActionError() = runComponentTest {
        val loginRepo = LoginRepositoryMock().apply {
            authDataResultProvider = { AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE)) }
        }
        val harness = createHarness(loginRepository = loginRepo)
        try {
            advanceUntilIdle()
            harness.component.onLoginClick(UserAuthProvider.GOOGLE)
            advanceUntilIdle()
            assertEquals(ZERO_CALLS, harness.counters.finished)
            val content = assertIs<LoginWelcomeScreenState.Content>(harness.component.state.value)
            assertIs<CommonError.Unknown>(content.actionError)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onLoginClick_apple_showsExternalAuthFailed() = runComponentTest {
        val harness = createHarness()
        try {
            advanceUntilIdle()
            harness.component.onLoginClick(UserAuthProvider.APPLE)
            val content = assertIs<LoginWelcomeScreenState.Content>(harness.component.state.value)
            assertIs<UserError.ExternalAuthFailed>(content.actionError)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onPrivacyPolicyClick_opensUrl() = runComponentTest {
        val launcher = ExternalLauncherMock()
        val harness = createHarness(externalLauncher = launcher)
        try {
            advanceUntilIdle()
            harness.component.onPrivacyPolicyClick()
            assertEquals(listOf(PRIVACY_POLICY_URL), launcher.openedUrls)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onTermsOfServiceClick_opensUrl() = runComponentTest {
        val launcher = ExternalLauncherMock()
        val harness = createHarness(externalLauncher = launcher)
        try {
            advanceUntilIdle()
            harness.component.onTermsOfServiceClick()
            assertEquals(listOf(TERMS_OF_SERVICE_URL), launcher.openedUrls)
        } finally {
            harness.destroy()
        }
    }

    private fun createHarness(
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
    ): Harness {
        val lifecycle = LifecycleRegistry()
        lifecycle.resume()
        val ctx = DefaultComponentContext(lifecycle)
        val counters = NavigationCounters()
        val getGlobalSettings = GetGlobalSettingsUseCase(globalSettingsRepository)
        val getProviders = GetAvailableUserAuthProvidersUseCase(
            appType = AppType.CLIENT,
            openAuthSettingsRepository = authSettingsRepository
        )
        val googleAuth = GoogleAuthServiceMock()
        val loginByGoogle = LoginByGoogleUseCase(
            authService = googleAuth,
            loginRepository = loginRepository,
            authStorage = AuthStorageMock(),
            userStorage = UserStorageMock()
        )
        val expectedProviders = when (val authResult = authSettingsRepository.resultProvider()) {
            is AppResult.Success -> authResult.data.availableAuthProviders
            is AppResult.Error -> publicAuthSettingsMock().availableAuthProviders
        }
        val component = LoginWelcomeComponentImpl(
            componentContext = ctx,
            appType = AppType.CLIENT,
            externalLauncher = externalLauncher,
            getGlobalSettingsUseCase = getGlobalSettings,
            getAvailableUserAuthProvidersUseCase = getProviders,
            loginByGoogleUseCase = loginByGoogle,
            onNavigateToLoginByEmail = { counters.navigateEmail++ },
            onNavigateToLoginByPhone = { counters.navigatePhone++ },
            onFinished = { counters.finished++ }
        )
        return Harness(lifecycle, component, counters, expectedProviders)
    }

    private class NavigationCounters(
        var navigateEmail: Int = ZERO_CALLS,
        var navigatePhone: Int = ZERO_CALLS,
        var finished: Int = ZERO_CALLS
    )

    private class Harness(
        private val lifecycle: LifecycleRegistry,
        val component: LoginWelcomeComponentImpl,
        val counters: NavigationCounters,
        val expectedProviders: AvailableAuthProviders
    ) {
        fun destroy() {
            lifecycle.destroy()
        }
    }

    private companion object {
        const val PRIVACY_POLICY_URL = "https://example.com/privacy"
        const val TERMS_OF_SERVICE_URL = "https://example.com/terms"
        const val NOT_RETRYABLE = false
        const val ZERO_CALLS = 0
        const val ONE_CALL = 1
    }
}
