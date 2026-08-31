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
class SaveRemoteAuthSettingsUseCaseTest {

    @Test
    fun `should save settings to repository`() = runTest {
        val authSettings = managementAuthSettingsMock()
        val repository = ManagementAuthSettingsRepositoryMock()
        val useCase = SaveRemoteAuthSettingsUseCase(repository)

        val result = useCase(authSettings)

        assertIs<AppResult.Success<Unit>>(result)
        val getResult = repository.getManagementAuthSettings()
        assertIs<AppResult.Success<*>>(getResult)
        assertEquals(authSettings, getResult.data)
    }

    @Test
    fun `should propagate error from repository`() = runTest {
        val authSettings = managementAuthSettingsMock()
        val error = CommonError.Network(Exception("api_fail"))
        val repository = ManagementAuthSettingsRepositoryMock().apply {
            saveResultProvider = { AppResult.Error(error) }
        }
        val useCase = SaveRemoteAuthSettingsUseCase(repository)

        val result = useCase(authSettings)

        assertIs<AppResult.Error>(result)
        assertEquals(error, result.error)
    }
}
