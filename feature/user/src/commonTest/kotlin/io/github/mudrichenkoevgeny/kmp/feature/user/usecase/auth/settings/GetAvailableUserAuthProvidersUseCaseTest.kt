package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.model.apptype.AppType
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.settings.OpenAuthSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.AvailableAuthProviders
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.PublicAuthSettings
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

@InternalApi
class GetAvailableUserAuthProvidersUseCaseTest {

    private val providersMock = AvailableAuthProviders(
        primary = listOf(UserAuthProvider.EMAIL, UserAuthProvider.GOOGLE),
        secondary = listOf(UserAuthProvider.PHONE)
    )

    private val authSettingsMock = PublicAuthSettings(
        availableAuthProviders = providersMock,
        maxTotalIdentifiers = 5,
        maxEmailIdentifiers = 1,
        maxPhoneIdentifiers = 1,
        maxIdentifiersPerExternalProvider = 1
    )

    @Test
    fun `management app should return hardcoded email only`() = runTest {
        val useCase = GetAvailableUserAuthProvidersUseCase(AppType.MANAGEMENT)

        val result = useCase()

        assertIs<AppResult.Success<AvailableAuthProviders>>(result)
        assertEquals(1, result.data.primary.size)
        assertEquals(UserAuthProvider.EMAIL, result.data.primary.first())
        assertTrue(result.data.secondary.isEmpty())
    }

    @Test
    fun `client app should return settings from repository`() = runTest {
        val repository = FakeOpenAuthSettingsRepository(AppResult.Success(authSettingsMock))
        val useCase = GetAvailableUserAuthProvidersUseCase(AppType.CLIENT, repository)

        val result = useCase()

        assertIs<AppResult.Success<AvailableAuthProviders>>(result)
        assertEquals(providersMock, result.data)
    }

    @Test
    fun `client app should return error when repository is missing`() = runTest {
        val useCase = GetAvailableUserAuthProvidersUseCase(AppType.CLIENT, openAuthSettingsRepository = null)

        val result = useCase()

        assertIs<AppResult.Error>(result)
        assertIs<CommonError.ContractViolation>(result.error)
    }

    @Test
    fun `client app should propagate repository error`() = runTest {
        val error = CommonError.Network(Exception("api_fail"))
        val repository = FakeOpenAuthSettingsRepository(AppResult.Error(error))
        val useCase = GetAvailableUserAuthProvidersUseCase(AppType.CLIENT, repository)

        val result = useCase()

        assertIs<AppResult.Error>(result)
        assertEquals(error, result.error)
    }

    private class FakeOpenAuthSettingsRepository(
        private val result: AppResult<PublicAuthSettings>
    ) : OpenAuthSettingsRepository {
        override suspend fun getAuthSettings(): AppResult<PublicAuthSettings> = result
        override suspend fun refreshAuthSettings(): AppResult<PublicAuthSettings> = result
        override suspend fun updateAuthSettings(authSettings: PublicAuthSettings) = Unit
        override fun observeAuthSettings(): Flow<PublicAuthSettings?> = error("N/A")
    }
}
