package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mapper.auth.settings.toAuthSettings
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.settings.AvailableAuthProviders
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.settings.AuthSettings
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.settings.AuthSettingsRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.wireAuthSettingsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class GetAvailableUserAuthProvidersUseCaseTest {

    @Test
    fun invoke_returnsPrimaryAndSecondaryFromAuthSettings() = runTest {
        val authSettings = wireAuthSettingsResponse().toAuthSettings()
        val repo = FakeAuthSettingsRepository().apply {
            getResult = AppResult.Success(authSettings)
        }
        val useCase = GetAvailableUserAuthProvidersUseCase(repo)

        val invokeResult = useCase()

        val success = assertIs<AppResult.Success<AvailableAuthProviders>>(invokeResult)
        assertEquals(authSettings.availableAuthProviders, success.data)
    }

    private class FakeAuthSettingsRepository : AuthSettingsRepository {
        var getResult: AppResult<AuthSettings> = AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE))

        override suspend fun getAuthSettings(): AppResult<AuthSettings> = getResult

        override suspend fun refreshAuthSettings(): AppResult<AuthSettings> = error(STUB_NOT_USED)

        override suspend fun updateAuthSettings(authSettings: AuthSettings) = Unit

        override fun observeAuthSettings(): Flow<AuthSettings?> = flowOf(null)
    }

    private companion object {
        private const val NOT_RETRYABLE = false
        private const val STUB_NOT_USED = "not used"
    }
}
