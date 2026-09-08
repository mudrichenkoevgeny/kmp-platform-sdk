package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.model.apptype.AppType
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.settings.OpenAuthSettingsRepositoryMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.AvailableAuthProviders
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.OpenAuthSettings
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
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

    private val authSettingsMock = OpenAuthSettings(
        availableAuthProviders = providersMock,
        maxTotalIdentifiers = 5,
        maxEmailIdentifiers = 1,
        maxPhoneIdentifiers = 1,
        maxIdentifiersPerExternalProvider = 1,
        isRegistrationEnabled = true
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
        val repository = OpenAuthSettingsRepositoryMock().apply {
            resultProvider = { AppResult.Success(authSettingsMock) }
        }
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
        val repository = OpenAuthSettingsRepositoryMock().apply {
            resultProvider = { AppResult.Error(error) }
        }
        val useCase = GetAvailableUserAuthProvidersUseCase(AppType.CLIENT, repository)

        val result = useCase()

        assertIs<AppResult.Error>(result)
        assertEquals(error, result.error)
    }
}
