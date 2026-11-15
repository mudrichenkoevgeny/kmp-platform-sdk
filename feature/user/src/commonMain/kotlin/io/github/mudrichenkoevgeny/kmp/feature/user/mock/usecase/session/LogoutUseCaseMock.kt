package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.session

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.session.SessionRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.user.UserRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.LogoutUseCase

/**
 * Mock implementation of [LogoutUseCase] for testing.
 */
@InternalApi
public class LogoutUseCaseMock : LogoutUseCase(
    sessionRepository = SessionRepositoryMock(),
    userRepository = UserRepositoryMock()
) {
    public var executeCalls: Int = 0

    public var resultProvider: () -> AppResult<Unit> = {
        AppResult.Success(Unit)
    }

    public override suspend fun invoke(): AppResult<Unit> {
        executeCalls++
        return resultProvider()
    }
}
