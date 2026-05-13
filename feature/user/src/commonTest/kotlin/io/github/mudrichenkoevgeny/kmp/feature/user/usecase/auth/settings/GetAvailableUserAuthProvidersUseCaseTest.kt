package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.auth.settings.publicAuthSettingsPayloadMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.settings.AuthSettingsRepositoryMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.AvailableAuthProviders
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.auth.settings.toAuthSettings
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class GetAvailableUserAuthProvidersUseCaseTest {

    @Test
    fun invoke_returnsPrimaryAndSecondaryFromAuthSettings() = runTest {
        val authSettings = publicAuthSettingsPayloadMock().toAuthSettings()
        val repo = AuthSettingsRepositoryMock().apply {
            resultProvider = { AppResult.Success(authSettings) }
        }
        val useCase = GetAvailableUserAuthProvidersUseCase(repo)

        val invokeResult = useCase()

        val success = assertIs<AppResult.Success<AvailableAuthProviders>>(invokeResult)
        assertEquals(authSettings.availableAuthProviders, success.data)
    }
}