package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.session.ManagementSessionRepositoryMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.uuid.Uuid

@InternalApi
class ManagementGetUserSessionsUseCaseTest {

    @Test
    fun invoke_delegatesToRepositoryWithUserId() = runTest {
        val repo = ManagementSessionRepositoryMock()
        val useCase = ManagementGetUserSessionsUseCase(repo)
        val targetUserId = UserId(Uuid.random())

        val result = useCase(userId = targetUserId)

        assertIs<AppResult.Success<*>>(result)
        assertEquals(targetUserId, repo.lastUserId)
    }
}
