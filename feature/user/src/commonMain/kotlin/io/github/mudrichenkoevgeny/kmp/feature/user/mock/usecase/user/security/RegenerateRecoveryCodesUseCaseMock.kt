package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.user.security

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.user.security.UserSecurityRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.RegenerateRecoveryCodesUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totprecoverycodes.TotpRecoveryCodes

/**
 * Mock implementation of [RegenerateRecoveryCodesUseCase] for testing.
 */
@InternalApi
public class RegenerateRecoveryCodesUseCaseMock : RegenerateRecoveryCodesUseCase(
    userSecurityRepository = UserSecurityRepositoryMock()
) {
    public var executeCalls: Int = 0

    public var resultProvider: () -> AppResult<TotpRecoveryCodes> = {
        AppResult.Error(CommonError.Unknown())
    }

    public override suspend fun invoke(): AppResult<TotpRecoveryCodes> {
        executeCalls++
        return resultProvider()
    }
}
