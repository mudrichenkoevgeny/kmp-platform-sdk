package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.user.security

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.user.security.UserSecurityRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.GetRecoveryCodesUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totprecoverycodes.TotpRecoveryCodes

@InternalApi
class GetRecoveryCodesUseCaseMock : GetRecoveryCodesUseCase(
    userSecurityRepository = UserSecurityRepositoryMock()
) {
    var executeCalls: Int = 0

    var resultProvider: () -> AppResult<TotpRecoveryCodes> = {
        AppResult.Error(CommonError.Unknown())
    }

    override suspend fun invoke(): AppResult<TotpRecoveryCodes> {
        executeCalls++
        return resultProvider()
    }
}