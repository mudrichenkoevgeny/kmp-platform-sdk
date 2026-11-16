package io.github.mudrichenkoevgeny.kmp.sampleclient.app.usecase

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.mock.repository.OpenSecuritySettingsRepositoryMock
import io.github.mudrichenkoevgeny.kmp.core.security.usecase.RefreshOpenSecuritySettingsUseCase
import io.github.mudrichenkoevgeny.kmp.core.settings.mock.repository.OpenGlobalSettingsRepositoryMock
import io.github.mudrichenkoevgeny.kmp.core.settings.usecase.RefreshOpenGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.settings.OpenAuthSettingsRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings.RefreshOpenAuthSettingsUseCase
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
class SyncClientDataRunnerTest {

    @Test
    fun invoke_executesAllThreeUseCasesConcurrently() = runTest {
        val globalSettingsRepository = OpenGlobalSettingsRepositoryMock()
        val openSecuritySettingsRepository = OpenSecuritySettingsRepositoryMock()
        val authSettingsRepository = OpenAuthSettingsRepositoryMock()

        val runner = SyncClientDataRunner(
            refreshOpenGlobalSettingsUseCase = RefreshOpenGlobalSettingsUseCase(globalSettingsRepository),
            refreshOpenSecuritySettingsUseCase = RefreshOpenSecuritySettingsUseCase(openSecuritySettingsRepository),
            refreshOpenAuthSettingsUseCase = RefreshOpenAuthSettingsUseCase(authSettingsRepository)
        )

        runner()

        assertEquals(1, globalSettingsRepository.refreshCallCount)
        assertEquals(1, openSecuritySettingsRepository.refreshCallCount)
        assertEquals(1, authSettingsRepository.refreshCallCount)
    }

    @Test
    fun invoke_whenOneUseCaseFails_completesRemainingUseCasesWithoutThrowing() = runTest {
        val globalSettingsRepository = OpenGlobalSettingsRepositoryMock()
        val openSecuritySettingsRepository = OpenSecuritySettingsRepositoryMock().apply {
            refreshSecuritySettingsResult = AppResult.Error(CommonError.Unknown())
        }
        val authSettingsRepository = OpenAuthSettingsRepositoryMock()

        val runner = SyncClientDataRunner(
            refreshOpenGlobalSettingsUseCase = RefreshOpenGlobalSettingsUseCase(globalSettingsRepository),
            refreshOpenSecuritySettingsUseCase = RefreshOpenSecuritySettingsUseCase(openSecuritySettingsRepository),
            refreshOpenAuthSettingsUseCase = RefreshOpenAuthSettingsUseCase(authSettingsRepository)
        )

        runner()

        assertEquals(1, globalSettingsRepository.refreshCallCount)
        assertEquals(1, openSecuritySettingsRepository.refreshCallCount)
        assertEquals(1, authSettingsRepository.refreshCallCount)
    }
}
