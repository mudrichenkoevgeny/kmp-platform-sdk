package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.session

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.session.SessionRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.DeleteSessionUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId

@InternalApi
class DeleteSessionUseCaseMock : DeleteSessionUseCase(
    sessionRepository = SessionRepositoryMock()
) {
    var resultProvider: (UserSessionId) -> AppResult<Unit> = { AppResult.Success(Unit) }
    var executeCalls = 0

    override suspend fun invoke(userSessionId: UserSessionId): AppResult<Unit> {
        executeCalls++
        return resultProvider(userSessionId)
    }
}
