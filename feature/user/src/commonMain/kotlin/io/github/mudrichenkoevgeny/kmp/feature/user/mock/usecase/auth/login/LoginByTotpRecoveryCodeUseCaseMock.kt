package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.auth.login

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.login.LoginRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.auth.AuthStorageMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.user.UserStorageMock
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.LoginByTotpRecoveryCodeUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.data.AuthData

@InternalApi
class LoginByTotpRecoveryCodeUseCaseMock : LoginByTotpRecoveryCodeUseCase(
    loginRepository = LoginRepositoryMock(),
    authStorage = AuthStorageMock(),
    userStorage = UserStorageMock()
) {
    var executeCalls: Int = 0
    var lastMfaToken: String? = null
    var lastCode: String? = null

    var resultProvider: (String, String) -> AppResult<AuthData> = { _, _ ->
        AppResult.Error(CommonError.Unknown())
    }

    override suspend fun execute(mfaToken: String, code: String): AppResult<AuthData> {
        executeCalls++
        lastMfaToken = mfaToken
        lastCode = code
        return resultProvider(mfaToken, code)
    }
}