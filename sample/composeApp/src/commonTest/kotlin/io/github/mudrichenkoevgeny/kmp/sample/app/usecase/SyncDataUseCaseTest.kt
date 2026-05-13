package io.github.mudrichenkoevgeny.kmp.sample.app.usecase

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.mock.repository.SecuritySettingsRepositoryMock
import io.github.mudrichenkoevgeny.kmp.core.security.usecase.RefreshSecuritySettingsUseCase
import io.github.mudrichenkoevgeny.kmp.core.settings.mock.repository.GlobalSettingsRepositoryMock
import io.github.mudrichenkoevgeny.kmp.core.settings.usecase.RefreshGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.settings.AuthSettingsRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings.RefreshAuthSettingsUseCase
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
class SyncDataUseCaseTest {

    @Test
    fun `invoke runs all three refresh use cases`() = runTest {
        val globalRepo = GlobalSettingsRepositoryMock()
        val securityRepo = SecuritySettingsRepositoryMock()
        val authRepo = AuthSettingsRepositoryMock()

        var globalCalls = 0
        var securityCalls = 0
        var authCalls = 0

        globalRepo.resultProvider = {
            globalCalls++
            AppResult.Error(CommonError.Unknown())
        }
        securityRepo.resultProvider = {
            securityCalls++
            AppResult.Error(CommonError.Unknown())
        }
        authRepo.resultProvider = {
            authCalls++
            AppResult.Error(CommonError.Unknown())
        }

        val useCase = SyncDataUseCase(
            refreshGlobalSettingsUseCase = RefreshGlobalSettingsUseCase(globalRepo),
            refreshSecuritySettingsUseCase = RefreshSecuritySettingsUseCase(securityRepo),
            refreshAuthSettingsUseCase = RefreshAuthSettingsUseCase(authRepo)
        )

        useCase()

        assertEquals(1, globalCalls)
        assertEquals(1, securityCalls)
        assertEquals(1, authCalls)
    }

    @Test
    fun `invoke completes successfully even if all repositories fail`() = runTest {
        val globalRepo = GlobalSettingsRepositoryMock().apply {
            resultProvider = { AppResult.Error(CommonError.Unknown()) }
        }
        val securityRepo = SecuritySettingsRepositoryMock().apply {
            resultProvider = { AppResult.Error(CommonError.Unknown()) }
        }
        val authRepo = AuthSettingsRepositoryMock().apply {
            resultProvider = { AppResult.Error(CommonError.Unknown()) }
        }

        val useCase = SyncDataUseCase(
            refreshGlobalSettingsUseCase = RefreshGlobalSettingsUseCase(globalRepo),
            refreshSecuritySettingsUseCase = RefreshSecuritySettingsUseCase(securityRepo),
            refreshAuthSettingsUseCase = RefreshAuthSettingsUseCase(authRepo)
        )

        useCase()
    }
}