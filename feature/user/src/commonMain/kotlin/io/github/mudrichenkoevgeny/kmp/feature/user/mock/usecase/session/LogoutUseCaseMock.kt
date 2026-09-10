package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.session

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.session.SessionRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.user.UserRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.LogoutUseCase

@InternalApi
class LogoutUseCaseMock : LogoutUseCase(
    sessionRepository = SessionRepositoryMock(),
    userRepository = UserRepositoryMock()
) {
    var executeCalls: Int = 0

    var resultProvider: () -> AppResult<Unit> = {
        AppResult.Success(Unit)
    }

    override suspend fun invoke(): AppResult<Unit> {
        executeCalls++
        return resultProvider()
    }
}
