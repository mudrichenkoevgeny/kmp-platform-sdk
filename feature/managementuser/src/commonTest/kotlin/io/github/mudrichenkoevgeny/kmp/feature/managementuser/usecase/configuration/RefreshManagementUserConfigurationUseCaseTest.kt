package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.configuration

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.mock.repository.OpenSecuritySettingsRepositoryMock
import io.github.mudrichenkoevgeny.kmp.core.settings.mock.repository.OpenGlobalSettingsRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.configuration.ManagementUserConfigurationApiMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.settings.OpenAuthSettingsRepositoryMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.configuration.OpenUserConfiguration
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.configuration.toOpenUserConfiguration
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.network.model.configuration.createTestOpenUserConfigurationPayload
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class RefreshManagementUserConfigurationUseCaseTest {

    private val api = ManagementUserConfigurationApiMock()
    private val globalRepo = OpenGlobalSettingsRepositoryMock()
    private val securityRepo = OpenSecuritySettingsRepositoryMock()
    private val authRepo = OpenAuthSettingsRepositoryMock()
    private val useCase = RefreshManagementUserConfigurationUseCase(
        managementUserConfigurationApi = api,
        openGlobalSettingsRepository = globalRepo,
        openSecuritySettingsRepository = securityRepo,
        openAuthSettingsRepository = authRepo
    )

    @Test
    fun invoke_updatesRepositories_whenApiSucceeds() = runTest {
        val payload = createTestOpenUserConfigurationPayload()
        api.getOpenUserConfigurationResult = AppResult.Success(payload)

        val result = useCase()

        val success = assertIs<AppResult.Success<OpenUserConfiguration>>(result)
        val expected = payload.toOpenUserConfiguration()
        assertEquals(expected, success.data)
        assertEquals(AppResult.Success(expected.openGlobalSettings), globalRepo.getOpenGlobalSettings())
        assertEquals(AppResult.Success(expected.openSecuritySettings), securityRepo.getOpenSecuritySettings())
        assertEquals(AppResult.Success(expected.openAuthSettings), authRepo.getOpenAuthSettings())
    }

    @Test
    fun invoke_returnsError_whenApiFails() = runTest {
        val expectedError = CommonError.Unknown()
        api.getOpenUserConfigurationResult = AppResult.Error(expectedError)

        val result = useCase()

        assertIs<AppResult.Error>(result)
    }
}
