package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.globalsettings.ManagementGlobalSettingsRepositoryMock
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.settings.domain.model.globalsettings.createTestManagementGlobalSettings
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class ResetRemoteGlobalSettingsUseCaseTest {

    @Test
    fun `should reset settings via repository`() = runTest {
        val globalSettings = createTestManagementGlobalSettings()
        val repository = ManagementGlobalSettingsRepositoryMock().apply {
            resetRemoteGlobalSettingsResult = AppResult.Success(globalSettings)
        }
        val useCase = ResetRemoteGlobalSettingsUseCase(repository)

        val result = useCase()

        assertIs<AppResult.Success<*>>(result)
        assertEquals(globalSettings, result.data)
    }

    @Test
    fun `should propagate error from repository`() = runTest {
        val error = CommonError.Network(Exception("api_fail"))
        val repository = ManagementGlobalSettingsRepositoryMock().apply {
            resetRemoteGlobalSettingsResult = AppResult.Error(error)
        }
        val useCase = ResetRemoteGlobalSettingsUseCase(repository)

        val result = useCase()

        assertIs<AppResult.Error>(result)
        assertEquals(error, result.error)
    }
}
