package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.user.security

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.user.security.UserSecurityRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.DisableTotpUseCase

/**
 * Mock implementation of [DisableTotpUseCase] for testing.
 */
@InternalApi
public class DisableTotpUseCaseMock : DisableTotpUseCase(
    userSecurityRepository = UserSecurityRepositoryMock()
) {
    public var executeCalls: Int = 0

    public var resultProvider: () -> AppResult<Unit> = {
        AppResult.Error(CommonError.Unknown())
    }

    public override suspend fun invoke(): AppResult<Unit> {
        executeCalls++
        return resultProvider()
    }
}
