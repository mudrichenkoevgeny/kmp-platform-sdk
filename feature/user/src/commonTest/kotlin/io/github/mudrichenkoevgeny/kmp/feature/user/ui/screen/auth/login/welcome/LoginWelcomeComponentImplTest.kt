package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.welcome

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.platform.externallauncher.ExternalLauncher
import io.github.mudrichenkoevgeny.kmp.core.settings.model.globalsettings.GlobalSettings
import io.github.mudrichenkoevgeny.kmp.core.settings.repository.globalsettings.GlobalSettingsRepository
import io.github.mudrichenkoevgeny.kmp.core.settings.usecase.GetGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.google.GoogleAuthService
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError
import io.github.mudrichenkoevgeny.kmp.feature.user.mapper.auth.toAuthData
import io.github.mudrichenkoevgeny.kmp.feature.user.mapper.auth.settings.toAuthSettings
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.auth.MockAuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.user.MockUserStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.AuthData
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.settings.AuthSettings
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.settings.AvailableAuthProviders
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.SendConfirmationData
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.login.LoginRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.settings.AuthSettingsRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.wireAuthDataResponse
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.wireAuthSettingsResponse
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.LoginByGoogleUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings.GetAvailableUserAuthProvidersUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserAuthProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.test.runUserUiComponentTest
import kotlinx.coroutines.test.advanceUntilIdle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
class LoginWelcomeComponentImplTest {

    @Test
    fun init_loadsContent_whenAuthAndGlobalSettingsSucceed() = runUserUiComponentTest {
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
    fun init_showsInitializationError_whenAuthSettingsFail() = runUserUiComponentTest {
        val authRepo = FakeAuthSettingsRepository()
        authRepo.getResult = AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE))
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
    fun onRetryInitClick_recoverAfterFailure() = runUserUiComponentTest {
        val authRepo = FakeAuthSettingsRepository()
        authRepo.getResult = AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE))
        val harness = createHarness(authSettingsRepository = authRepo)
        try {
            advanceUntilIdle()
            assertIs<LoginWelcomeScreenState.InitializationError>(harness.component.state.value)
            authRepo.getResult = AppResult.Success(wireAuthSettingsResponse().toAuthSettings())
            harness.component.onRetryInitClick()
            advanceUntilIdle()
            assertIs<LoginWelcomeScreenState.Content>(harness.component.state.value)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onLoginClick_email_navigatesToEmail() = runUserUiComponentTest {
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
    fun onLoginClick_phone_navigatesToPhone() = runUserUiComponentTest {
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
    fun onLoginClick_google_success_callsOnFinished() = runUserUiComponentTest {
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
    fun onLoginClick_google_error_showsActionError() = runUserUiComponentTest {
        val loginRepo = FakeLoginRepository()
        loginRepo.loginExternalResult = AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE))
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
    fun onLoginClick_apple_showsExternalAuthFailed() = runUserUiComponentTest {
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
    fun onPrivacyPolicyClick_opensUrl() = runUserUiComponentTest {
        val launcher = FakeExternalLauncher()
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
    fun onTermsOfServiceClick_opensUrl() = runUserUiComponentTest {
        val launcher = FakeExternalLauncher()
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
        authSettingsRepository: FakeAuthSettingsRepository = FakeAuthSettingsRepository().apply {
            getResult = AppResult.Success(wireAuthSettingsResponse().toAuthSettings())
        },
        globalSettingsRepository: FakeGlobalSettingsRepository = FakeGlobalSettingsRepository().apply {
            getResult = AppResult.Success(
                GlobalSettings(
                    privacyPolicyUrl = PRIVACY_POLICY_URL,
                    termsOfServiceUrl = TERMS_OF_SERVICE_URL,
                    contactSupportEmail = null
                )
            )
        },
        externalLauncher: ExternalLauncher = FakeExternalLauncher(),
        loginRepository: FakeLoginRepository = FakeLoginRepository().apply {
            loginExternalResult = AppResult.Success(wireAuthDataResponse().toAuthData())
        }
    ): Harness {
        val lifecycle = LifecycleRegistry()
        lifecycle.resume()
        val ctx = DefaultComponentContext(lifecycle)
        val counters = NavigationCounters()
        val getGlobalSettings = GetGlobalSettingsUseCase(globalSettingsRepository)
        val getProviders = GetAvailableUserAuthProvidersUseCase(authSettingsRepository)
        val googleAuth = FakeGoogleAuthService()
        val loginByGoogle = LoginByGoogleUseCase(
            authService = googleAuth,
            loginRepository = loginRepository,
            authStorage = MockAuthStorage(),
            userStorage = MockUserStorage()
        )
        val expectedProviders = when (val authResult = authSettingsRepository.getResult) {
            is AppResult.Success -> authResult.data.availableAuthProviders
            is AppResult.Error -> wireAuthSettingsResponse().toAuthSettings().availableAuthProviders
        }
        val component = LoginWelcomeComponentImpl(
            componentContext = ctx,
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

    private class FakeExternalLauncher : ExternalLauncher {
        val openedUrls = mutableListOf<String>()

        override fun openUrl(url: String) {
            openedUrls.add(url)
        }

        override fun openMail(email: String, subject: String?, body: String?) = Unit

        override fun openFile(url: String) = Unit
    }

    private class FakeGoogleAuthService : GoogleAuthService {
        var signInResult: AppResult<String> = AppResult.Success(GOOGLE_ID_TOKEN)

        override suspend fun signIn(): AppResult<String> = signInResult

        override suspend fun signOut(): AppResult<Unit> = AppResult.Success(Unit)
    }

    private class FakeAuthSettingsRepository : AuthSettingsRepository {
        lateinit var getResult: AppResult<AuthSettings>

        override suspend fun getAuthSettings(): AppResult<AuthSettings> = getResult

        override suspend fun refreshAuthSettings(): AppResult<AuthSettings> =
            AppResult.Error(CommonError.Unknown())

        override suspend fun updateAuthSettings(authSettings: AuthSettings) = Unit

        override fun observeAuthSettings(): Flow<AuthSettings?> = flowOf(null)
    }

    private class FakeGlobalSettingsRepository : GlobalSettingsRepository {
        lateinit var getResult: AppResult<GlobalSettings>

        override suspend fun getGlobalSettings(): AppResult<GlobalSettings> = getResult

        override suspend fun refreshGlobalSettings(): AppResult<GlobalSettings> =
            AppResult.Error(CommonError.Unknown())

        override suspend fun updateGlobalSettings(globalSettings: GlobalSettings) = Unit

        override fun observeGlobalSettings(): Flow<GlobalSettings?> = flowOf(null)
    }

    private class FakeLoginRepository : LoginRepository {
        var loginExternalResult: AppResult<AuthData> =
            AppResult.Success(wireAuthDataResponse().toAuthData())

        override suspend fun loginByEmail(email: String, password: String): AppResult<AuthData> =
            error(STUB_NOT_USED)

        override suspend fun loginByPhone(
            phoneNumber: String,
            confirmationCode: String
        ): AppResult<AuthData> = error(STUB_NOT_USED)

        override suspend fun loginByExternalAuthProvider(
            authProvider: UserAuthProvider,
            token: String
        ): AppResult<AuthData> = loginExternalResult

        override suspend fun sendLoginConfirmationToPhone(
            phoneNumber: String
        ): AppResult<SendConfirmationData> = error(STUB_NOT_USED)

        override fun getRemainingLoginConfirmationDelayInSeconds(phoneNumber: String): Int = ZERO_DELAY
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
        const val GOOGLE_ID_TOKEN = "google-id-token"
        const val NOT_RETRYABLE = false
        const val STUB_NOT_USED = "stub"
        const val ZERO_DELAY = 0
        const val ZERO_CALLS = 0
        const val ONE_CALL = 1
    }
}
