package io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.user.security

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.mock.domain.model.totpRecoveryCodesMock
import io.github.mudrichenkoevgeny.kmp.core.security.mock.domain.model.totpSetupMock
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.security.UserSecurityRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totprecoverycodes.TotpRecoveryCodes
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totpsetup.TotpSetup

@InternalApi
open class UserSecurityRepositoryMock : UserSecurityRepository {

    var totpSetupResultProvider: () -> AppResult<TotpSetup> = {
        AppResult.Success(
            totpSetupMock()
        )
    }

    var enableTotpResultProvider: (mfaToken: String, code: String) -> AppResult<TotpRecoveryCodes> = { _, _ ->
        AppResult.Success(
            totpRecoveryCodesMock()
        )
    }

    var disableTotpResultProvider: () -> AppResult<Unit> = { AppResult.Success(Unit) }

    var recoveryCodesResultProvider: () -> AppResult<TotpRecoveryCodes> = {
        AppResult.Success(
            totpRecoveryCodesMock()
        )
    }

    var regenerateRecoveryCodesResultProvider: () -> AppResult<TotpRecoveryCodes> = {
        AppResult.Success(
            totpRecoveryCodesMock()
        )
    }

    var lastMfaToken: String? = null
    var lastCode: String? = null

    override suspend fun setupTotp(): AppResult<TotpSetup> = totpSetupResultProvider()

    override suspend fun enableTotp(mfaToken: String, code: String): AppResult<TotpRecoveryCodes> {
        lastMfaToken = mfaToken
        lastCode = code
        return enableTotpResultProvider(mfaToken, code)
    }

    override suspend fun disableTotp(): AppResult<Unit> = disableTotpResultProvider()

    override suspend fun getRecoveryCodes(): AppResult<TotpRecoveryCodes> = recoveryCodesResultProvider()

    override suspend fun regenerateRecoveryCodes(): AppResult<TotpRecoveryCodes> = regenerateRecoveryCodesResultProvider()
}
