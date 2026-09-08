package io.github.mudrichenkoevgeny.kmp.feature.clientuser.usecase.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.auth.settings.openAuthSettingsPayloadMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.settings.OpenAuthSettingsRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.model.apptype.AppType
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings.GetAvailableUserAuthProvidersUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.AvailableAuthProviders
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.auth.settings.toOpenAuthSettings
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class GetClientAvailableUserAuthProvidersUseCaseTest {

    @Test
    fun invoke_returnsPrimaryAndSecondaryFromAuthSettings() = runTest {
        val authSettings = openAuthSettingsPayloadMock().toOpenAuthSettings()
        val repo = OpenAuthSettingsRepositoryMock().apply {
            resultProvider = { AppResult.Success(authSettings) }
        }
        val useCase = GetAvailableUserAuthProvidersUseCase(AppType.CLIENT, repo)

        val invokeResult = useCase()

        val success = assertIs<AppResult.Success<AvailableAuthProviders>>(invokeResult)
        assertEquals(authSettings.availableAuthProviders, success.data)
    }
}
