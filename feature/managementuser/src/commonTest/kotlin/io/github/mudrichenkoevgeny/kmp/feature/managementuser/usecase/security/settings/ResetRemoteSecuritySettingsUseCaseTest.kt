package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.security.settings

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.mock.domain.model.managementSecuritySettingsMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.security.settings.ManagementSecuritySettingsRepositoryMock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class ResetRemoteSecuritySettingsUseCaseTest {

    @Test
    fun `should reset settings via repository`() = runTest {
        val securitySettings = managementSecuritySettingsMock()
        val repository = ManagementSecuritySettingsRepositoryMock().apply {
            resetRemoteSecuritySettingsResult = AppResult.Success(securitySettings)
        }
        val useCase = ResetRemoteSecuritySettingsUseCase(repository)

        val result = useCase()

        assertIs<AppResult.Success<*>>(result)
        assertEquals(securitySettings, result.data)
    }

    @Test
    fun `should propagate error from repository`() = runTest {
        val error = CommonError.Network(Exception("api_fail"))
        val repository = ManagementSecuritySettingsRepositoryMock().apply {
            resetRemoteSecuritySettingsResult = AppResult.Error(error)
        }
        val useCase = ResetRemoteSecuritySettingsUseCase(repository)

        val result = useCase()

        assertIs<AppResult.Error>(result)
        assertEquals(error, result.error)
    }
}
