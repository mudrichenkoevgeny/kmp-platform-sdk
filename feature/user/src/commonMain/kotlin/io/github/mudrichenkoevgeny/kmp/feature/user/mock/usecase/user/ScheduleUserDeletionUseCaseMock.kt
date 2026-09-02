package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.user

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.user.userDetailsMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.user.UserRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.ScheduleUserDeletionUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserDetails

@InternalApi
class ScheduleUserDeletionUseCaseMock : ScheduleUserDeletionUseCase(
    userRepository = UserRepositoryMock()
) {
    var executeCalls: Int = 0

    var resultProvider: () -> AppResult<UserDetails> = {
        AppResult.Success(userDetailsMock())
    }

    override suspend fun invoke(): AppResult<UserDetails> {
        executeCalls++
        return resultProvider()
    }
}