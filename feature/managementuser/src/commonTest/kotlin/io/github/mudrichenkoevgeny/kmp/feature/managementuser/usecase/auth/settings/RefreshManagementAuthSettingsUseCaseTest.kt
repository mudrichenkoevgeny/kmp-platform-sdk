package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.auth.settings.ManagementAuthSettingsRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.auth.settings.managementAuthSettingsMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.ManagementAuthSettings
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class RefreshManagementAuthSettingsUseCaseTest {

    @Test
    fun `should refresh settings from repository`() = runTest {
        val authSettings = managementAuthSettingsMock()
        val repository = ManagementAuthSettingsRepositoryMock().apply {
            resultProvider = { AppResult.Success(authSettings) }
        }
        val useCase = RefreshManagementAuthSettingsUseCase(repository)

        val result = useCase()

        assertIs<AppResult.Success<ManagementAuthSettings>>(result)
        assertEquals(authSettings, result.data)
    }

    @Test
    fun `should propagate error from repository`() = runTest {
        val error = CommonError.Network(Exception("api_fail"))
        val repository = ManagementAuthSettingsRepositoryMock().apply {
            resultProvider = { AppResult.Error(error) }
        }
        val useCase = RefreshManagementAuthSettingsUseCase(repository)

        val result = useCase()

        assertIs<AppResult.Error>(result)
        assertEquals(error, result.error)
    }
}
