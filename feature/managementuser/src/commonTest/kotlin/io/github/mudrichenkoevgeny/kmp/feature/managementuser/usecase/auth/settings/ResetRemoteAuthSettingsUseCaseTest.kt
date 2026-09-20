package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.auth.settings.ManagementAuthSettingsRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.auth.settings.managementAuthSettingsMock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class ResetRemoteAuthSettingsUseCaseTest {

    @Test
    fun `should reset settings via repository`() = runTest {
        val authSettings = managementAuthSettingsMock()
        val repository = ManagementAuthSettingsRepositoryMock().apply {
            resetResultProvider = { AppResult.Success(authSettings) }
        }
        val useCase = ResetRemoteAuthSettingsUseCase(repository)

        val result = useCase()

        assertIs<AppResult.Success<*>>(result)
        assertEquals(authSettings, result.data)
    }

    @Test
    fun `should propagate error from repository`() = runTest {
        val error = CommonError.Network(Exception("api_fail"))
        val repository = ManagementAuthSettingsRepositoryMock().apply {
            resetResultProvider = { AppResult.Error(error) }
        }
        val useCase = ResetRemoteAuthSettingsUseCase(repository)

        val result = useCase()

        assertIs<AppResult.Error>(result)
        assertEquals(error, result.error)
    }
}
