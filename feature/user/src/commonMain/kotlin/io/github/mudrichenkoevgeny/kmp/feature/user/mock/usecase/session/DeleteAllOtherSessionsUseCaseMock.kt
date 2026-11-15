package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.session

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.session.SessionRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.DeleteAllOtherSessionsUseCase

/**
 * Mock implementation of [DeleteAllOtherSessionsUseCase] for testing.
 */
@InternalApi
public class DeleteAllOtherSessionsUseCaseMock : DeleteAllOtherSessionsUseCase(
    sessionRepository = SessionRepositoryMock()
) {
    public var resultProvider: () -> AppResult<Unit> = { AppResult.Success(Unit) }
    public var executeCalls: Int = 0

    public override suspend fun invoke(): AppResult<Unit> {
        executeCalls++
        return resultProvider()
    }
}
