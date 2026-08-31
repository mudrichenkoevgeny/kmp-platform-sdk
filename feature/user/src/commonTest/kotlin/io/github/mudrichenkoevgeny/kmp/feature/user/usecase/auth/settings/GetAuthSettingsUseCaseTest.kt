package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.auth.settings.publicAuthSettingsMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.settings.OpenAuthSettingsRepositoryMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.PublicAuthSettings
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class GetAuthSettingsUseCaseTest {

    @Test
    fun `should return settings from repository`() = runTest {
        val authSettings = publicAuthSettingsMock()
        val repository = OpenAuthSettingsRepositoryMock().apply {
            resultProvider = { AppResult.Success(authSettings) }
        }
        val useCase = GetAuthSettingsUseCase(repository)

        val result = useCase()

        assertIs<AppResult.Success<PublicAuthSettings>>(result)
        assertEquals(authSettings, result.data)
    }

    @Test
    fun `should propagate error from repository`() = runTest {
        val error = CommonError.Network(Exception("api_fail"))
        val repository = OpenAuthSettingsRepositoryMock().apply {
            resultProvider = { AppResult.Error(error) }
        }
        val useCase = GetAuthSettingsUseCase(repository)

        val result = useCase()

        assertIs<AppResult.Error>(result)
        assertEquals(error, result.error)
    }
}
