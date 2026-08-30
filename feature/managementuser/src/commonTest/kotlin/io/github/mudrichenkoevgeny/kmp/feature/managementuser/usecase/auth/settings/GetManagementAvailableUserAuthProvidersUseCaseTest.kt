package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.model.apptype.AppType
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings.GetAvailableUserAuthProvidersUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.AvailableAuthProviders
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class GetAvailableUserAuthProvidersUseCaseTest {

    @Test
    fun invoke_returnsEmailOnlyForManagement() = runTest {
        val useCase = GetAvailableUserAuthProvidersUseCase(AppType.MANAGEMENT)

        val invokeResult = useCase()

        val success = assertIs<AppResult.Success<AvailableAuthProviders>>(invokeResult)
        assertEquals(listOf(UserAuthProvider.EMAIL), success.data.primary)
        assertEquals(emptyList(), success.data.secondary)
    }
}
