package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.user.security

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.user.security.UserSecurityRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.SetupTotpUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totpsetup.TotpSetup

@InternalApi
class SetupTotpUseCaseMock : SetupTotpUseCase(
    userSecurityRepository = UserSecurityRepositoryMock()
) {
    var executeCalls: Int = 0

    var resultProvider: () -> AppResult<TotpSetup> = {
        AppResult.Error(CommonError.Unknown())
    }

    override suspend fun invoke(): AppResult<TotpSetup> {
        executeCalls++
        return resultProvider()
    }
}