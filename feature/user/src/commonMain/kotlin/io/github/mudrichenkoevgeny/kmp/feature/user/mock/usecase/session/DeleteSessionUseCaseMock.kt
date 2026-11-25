package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.session

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.session.SessionRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.DeleteSessionUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserSessionId

/**
 * Mock implementation of [DeleteSessionUseCase] for testing.
 */
@InternalApi
public class DeleteSessionUseCaseMock : DeleteSessionUseCase(
    sessionRepository = SessionRepositoryMock()
) {
    public var resultProvider: (UserSessionId) -> AppResult<Unit> = { AppResult.Success(Unit) }
    public var executeCalls: Int = 0

    public override suspend fun invoke(userSessionId: UserSessionId): AppResult<Unit> {
        executeCalls++
        return resultProvider(userSessionId)
    }
}
