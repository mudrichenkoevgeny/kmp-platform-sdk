package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.user.security

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.user.security.ManagementUserSecurityApiMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.toUserIdOrThrow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertIs

@InternalApi
class ManagementUserSecurityRepositoryImplTest {

    private val api = ManagementUserSecurityApiMock()
    private val repository = ManagementUserSecurityRepositoryImpl(api)
    private val testUserId = "550e8400-e29b-41d4-a716-446655440000".toUserIdOrThrow()

    @Test
    fun `disableTotp returns result from api`() = runTest {
        api.disableTotpResult = AppResult.Success(Unit)

        val result = repository.disableTotp(testUserId)

        assertIs<AppResult.Success<Unit>>(result)
    }
}