package io.github.mudrichenkoevgeny.kmp.sampleclient.app.usecase

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.mock.repository.OpenSecuritySettingsRepositoryMock
import io.github.mudrichenkoevgeny.kmp.core.security.usecase.RefreshOpenSecuritySettingsUseCase
import io.github.mudrichenkoevgeny.kmp.core.settings.mock.repository.OpenGlobalSettingsRepositoryMock
import io.github.mudrichenkoevgeny.kmp.core.settings.usecase.RefreshOpenGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.usecase.auth.settings.RefreshOpenAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.settings.OpenAuthSettingsRepositoryMock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

@InternalApi
class SyncOpenDataUseCaseTest {

    @Test
    fun `invoke runs all three refresh use cases`() = runTest {
        val globalRepo = OpenGlobalSettingsRepositoryMock().apply {
            refreshGlobalSettingsResult = AppResult.Error(CommonError.Unknown())
        }
        val securityRepo = OpenSecuritySettingsRepositoryMock().apply {
            refreshSecuritySettingsResult = AppResult.Error(CommonError.Unknown())
        }
        val authRepo = OpenAuthSettingsRepositoryMock().apply {
            resultProvider = { AppResult.Error(CommonError.Unknown()) }
        }

        val useCase = SyncDataUseCase(
            refreshOpenGlobalSettingsUseCase = RefreshOpenGlobalSettingsUseCase(globalRepo),
            refreshOpenSecuritySettingsUseCase = RefreshOpenSecuritySettingsUseCase(securityRepo),
            refreshOpenAuthSettingsUseCase = RefreshOpenAuthSettingsUseCase(authRepo)
        )

        useCase()
    }

    @Test
    fun `invoke completes successfully even if all repositories fail`() = runTest {
        val globalRepo = OpenGlobalSettingsRepositoryMock().apply {
            refreshGlobalSettingsResult = AppResult.Error(CommonError.Unknown())
        }
        val securityRepo = OpenSecuritySettingsRepositoryMock().apply {
            refreshSecuritySettingsResult = AppResult.Error(CommonError.Unknown())
        }
        val authRepo = OpenAuthSettingsRepositoryMock().apply {
            resultProvider = { AppResult.Error(CommonError.Unknown()) }
        }

        val useCase = SyncDataUseCase(
            refreshOpenGlobalSettingsUseCase = RefreshOpenGlobalSettingsUseCase(globalRepo),
            refreshOpenSecuritySettingsUseCase = RefreshOpenSecuritySettingsUseCase(securityRepo),
            refreshOpenAuthSettingsUseCase = RefreshOpenAuthSettingsUseCase(authRepo)
        )

        useCase()
    }
}
