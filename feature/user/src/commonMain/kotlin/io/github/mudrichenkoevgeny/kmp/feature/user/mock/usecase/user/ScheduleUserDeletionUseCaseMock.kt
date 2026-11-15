package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.user

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.user.UserRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.UserRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.ScheduleUserDeletionUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserPrivate

/**
 * Mock implementation of [ScheduleUserDeletionUseCase] for testing.
 */
@InternalApi
public class ScheduleUserDeletionUseCaseMock(
    userRepository: UserRepository = UserRepositoryMock()
) : ScheduleUserDeletionUseCase(userRepository) {

    public var executeCalls: Int = 0
    public var resultProvider: () -> AppResult<UserPrivate> = {
        AppResult.Error(
            CommonError.ContractViolation(
                throwable = IllegalStateException("No mock result provided for ScheduleUserDeletionUseCaseMock.")
            )
        )
    }

    public override suspend fun invoke(): AppResult<UserPrivate> {
        executeCalls++
        return resultProvider()
    }
}
