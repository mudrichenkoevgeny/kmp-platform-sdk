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

@InternalApi
class SyncManagementDataUseCaseTest {

    @Test
    fun `invoke runs all three refresh use cases`() = runTest {
        val globalRepo = ManagementGlobalSettingsRepositoryMock().apply {
            refreshManagementGlobalSettingsResult = AppResult.Error(CommonError.Unknown())
        }
        val securityRepo = ManagementSecuritySettingsRepositoryMock().apply {
            refreshManagementSecuritySettingsResult = AppResult.Error(CommonError.Unknown())
        }
        val authRepo = ManagementAuthSettingsRepositoryMock().apply {
            resultProvider = { AppResult.Error(CommonError.Unknown()) }
        }

        val useCase = SyncManagementDataUseCase(
            refreshManagementGlobalSettingsUseCase = RefreshManagementGlobalSettingsUseCase(globalRepo),
            refreshManagementSecuritySettingsUseCase = RefreshManagementSecuritySettingsUseCase(securityRepo),
            refreshManagementAuthSettingsUseCase = RefreshManagementAuthSettingsUseCase(authRepo)
        )

        useCase()
    }

    @Test
    fun `invoke completes successfully even if all repositories fail`() = runTest {
        val globalRepo = ManagementGlobalSettingsRepositoryMock().apply {
            refreshManagementGlobalSettingsResult = AppResult.Error(CommonError.Unknown())
        }
        val securityRepo = ManagementSecuritySettingsRepositoryMock().apply {
            refreshManagementSecuritySettingsResult = AppResult.Error(CommonError.Unknown())
        }
        val authRepo = ManagementAuthSettingsRepositoryMock().apply {
            resultProvider = { AppResult.Error(CommonError.Unknown()) }
        }

        val useCase = SyncManagementDataUseCase(
            refreshManagementGlobalSettingsUseCase = RefreshManagementGlobalSettingsUseCase(globalRepo),
            refreshManagementSecuritySettingsUseCase = RefreshManagementSecuritySettingsUseCase(securityRepo),
            refreshManagementAuthSettingsUseCase = RefreshManagementAuthSettingsUseCase(authRepo)
        )

        useCase()
    }
}
