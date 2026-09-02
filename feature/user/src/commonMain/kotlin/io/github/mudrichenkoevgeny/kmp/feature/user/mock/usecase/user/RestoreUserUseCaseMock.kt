package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.user

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.user.userDetailsMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.user.UserRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.RestoreUserUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserDetails

@InternalApi
open class RestoreUserUseCaseMock : RestoreUserUseCase(
    userRepository = UserRepositoryMock()
) {
    var resultProvider: () -> AppResult<UserDetails> = {
        AppResult.Success(userDetailsMock())
    }
    var executeCalls = 0

    override suspend fun invoke(): AppResult<UserDetails> {
        executeCalls++
        return resultProvider()
    }
}
