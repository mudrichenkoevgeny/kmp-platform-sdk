package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.identifier.ManagementIdentifierRepositoryMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.uuid.Uuid

@InternalApi
class ManagementGetUserIdentifiersUseCaseTest {

    @Test
    fun invoke_delegatesToRepositoryWithUserId() = runTest {
        val repo = ManagementIdentifierRepositoryMock()
        val useCase = ManagementGetUserIdentifiersUseCase(repo)
        val targetUserId = UserId(Uuid.random())

        val result = useCase(userId = targetUserId)

        assertIs<AppResult.Success<*>>(result)
        assertEquals(targetUserId, repo.lastUserId)
    }
}
