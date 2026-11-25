package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.session.ManagementSessionRepositoryMock
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.session.userlist.ManagementUserSessionListParams
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
        val params = ManagementUserSessionListParams(userId = targetUserId.asHexDashString())

        val result = useCase(params = params)

        assertIs<AppResult.Success<*>>(result)
        assertEquals(params, repo.lastUserParams)
    }
}
