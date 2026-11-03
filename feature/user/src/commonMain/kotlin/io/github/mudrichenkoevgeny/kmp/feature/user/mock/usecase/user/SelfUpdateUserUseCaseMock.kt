package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.user

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.domain.model.user.UpdateUserParams
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.user.UserRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.UserRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.SelfUpdateUserUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserPrivate
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.user.createTestUserPrivate

@InternalApi
class SelfUpdateUserUseCaseMock(
    userRepository: UserRepository = UserRepositoryMock()
) : SelfUpdateUserUseCase(userRepository) {

    var executeCalls = 0
    var resultProvider: (UpdateUserParams) -> AppResult<UserPrivate> = { _ ->
        AppResult.Success(createTestUserPrivate())
    }

    override suspend fun invoke(params: UpdateUserParams): AppResult<UserPrivate> {
        executeCalls++
        return resultProvider(params)
    }
}
