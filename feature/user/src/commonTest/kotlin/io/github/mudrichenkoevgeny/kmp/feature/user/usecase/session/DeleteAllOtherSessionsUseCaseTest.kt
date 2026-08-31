package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.session.SessionRepositoryMock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class DeleteAllOtherSessionsUseCaseTest {

    @Test
    fun invoke_returnsSuccess_whenRepositorySucceeds() = runTest {
        val repository = SessionRepositoryMock().apply {
            deleteAllOtherSessionsResultProvider = { AppResult.Success(Unit) }
        }
        val useCase = DeleteAllOtherSessionsUseCase(repository)

        val result = useCase()

        assertIs<AppResult.Success<Unit>>(result)
    }

    @Test
    fun invoke_returnsError_whenRepositoryFails() = runTest {
        val expectedError = CommonError.Unknown()
        val repository = SessionRepositoryMock().apply {
            deleteAllOtherSessionsResultProvider = { AppResult.Error(expectedError) }
        }
        val useCase = DeleteAllOtherSessionsUseCase(repository)

        val result = useCase()

        assertIs<AppResult.Error>(result)
        assertEquals(expectedError, result.error)
    }
}
