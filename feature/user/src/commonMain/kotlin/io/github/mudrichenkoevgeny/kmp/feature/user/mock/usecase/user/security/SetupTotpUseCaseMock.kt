package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.user.security

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.user.security.UserSecurityRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.SetupTotpUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totpsetup.TotpSetup

/**
 * Mock implementation of [SetupTotpUseCase] for testing.
 */
@InternalApi
public class SetupTotpUseCaseMock : SetupTotpUseCase(
    userSecurityRepository = UserSecurityRepositoryMock()
) {
    public var executeCalls: Int = 0

    public var resultProvider: () -> AppResult<TotpSetup> = {
        AppResult.Error(CommonError.Unknown())
    }

    public override suspend fun invoke(): AppResult<TotpSetup> {
        executeCalls++
        return resultProvider()
    }
}
