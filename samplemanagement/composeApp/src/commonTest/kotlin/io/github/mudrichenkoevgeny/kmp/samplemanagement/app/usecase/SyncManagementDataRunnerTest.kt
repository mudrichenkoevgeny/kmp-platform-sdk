package io.github.mudrichenkoevgeny.kmp.samplemanagement.app.usecase

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.auth.settings.ManagementAuthSettingsRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.globalsettings.ManagementGlobalSettingsRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.security.settings.ManagementSecuritySettingsRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings.RefreshManagementAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.globalsettings.RefreshManagementGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.security.settings.RefreshManagementSecuritySettingsUseCase
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
class SyncManagementDataRunnerTest {

    @Test
    fun invoke_executesAllThreeUseCasesConcurrently() = runTest {
        val managementGlobalSettingsRepository = ManagementGlobalSettingsRepositoryMock()
        val securitySettingsRepository = ManagementSecuritySettingsRepositoryMock()
        val authSettingsRepository = ManagementAuthSettingsRepositoryMock()

        val runner = SyncManagementDataRunner(
            refreshManagementGlobalSettingsUseCase =
                RefreshManagementGlobalSettingsUseCase(managementGlobalSettingsRepository),
            refreshManagementSecuritySettingsUseCase =
                RefreshManagementSecuritySettingsUseCase(securitySettingsRepository),
            refreshManagementAuthSettingsUseCase =
                RefreshManagementAuthSettingsUseCase(authSettingsRepository)
        )

        runner()

        assertEquals(1, managementGlobalSettingsRepository.refreshCallCount)
        assertEquals(1, securitySettingsRepository.refreshCallCount)
        assertEquals(1, authSettingsRepository.refreshCallCount)
    }

    @Test
    fun invoke_whenOneUseCaseFails_completesRemainingUseCasesWithoutThrowing() = runTest {
        val managementGlobalSettingsRepository = ManagementGlobalSettingsRepositoryMock()
        val securitySettingsRepository = ManagementSecuritySettingsRepositoryMock().apply {
            refreshManagementSecuritySettingsResult = AppResult.Error(CommonError.Unknown())
        }
        val authSettingsRepository = ManagementAuthSettingsRepositoryMock()

        val runner = SyncManagementDataRunner(
            refreshManagementGlobalSettingsUseCase =
                RefreshManagementGlobalSettingsUseCase(managementGlobalSettingsRepository),
            refreshManagementSecuritySettingsUseCase =
                RefreshManagementSecuritySettingsUseCase(securitySettingsRepository),
            refreshManagementAuthSettingsUseCase =
                RefreshManagementAuthSettingsUseCase(authSettingsRepository)
        )

        runner()

        assertEquals(1, managementGlobalSettingsRepository.refreshCallCount)
        assertEquals(1, securitySettingsRepository.refreshCallCount)
        assertEquals(1, authSettingsRepository.refreshCallCount)
    }
}
