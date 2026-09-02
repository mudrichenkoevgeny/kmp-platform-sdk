package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.session

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.session.SessionRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.DeleteAllOtherSessionsUseCase

@InternalApi
class DeleteAllOtherSessionsUseCaseMock : DeleteAllOtherSessionsUseCase(
    sessionRepository = SessionRepositoryMock()
) {
    var resultProvider: () -> AppResult<Unit> = { AppResult.Success(Unit) }
    var executeCalls = 0

    override suspend fun invoke(): AppResult<Unit> {
        executeCalls++
        return resultProvider()
    }
}
