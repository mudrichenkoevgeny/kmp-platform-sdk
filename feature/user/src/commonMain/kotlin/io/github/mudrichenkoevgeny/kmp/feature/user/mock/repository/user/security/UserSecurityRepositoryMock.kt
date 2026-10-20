package io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.user.security

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.security.UserSecurityRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totprecoverycodes.TotpRecoveryCodes
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totpsetup.TotpSetup
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.security.domain.model.totprecoverycodes.createTestTotpRecoveryCodes
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.security.domain.model.totpsetup.createTestTotpSetup

@InternalApi
open class UserSecurityRepositoryMock : UserSecurityRepository {

    var totpSetupResultProvider: () -> AppResult<TotpSetup> = {
        AppResult.Success(
            createTestTotpSetup()
        )
    }

    var enableTotpResultProvider: (mfaToken: String, code: String) -> AppResult<TotpRecoveryCodes> = { _, _ ->
        AppResult.Success(
            createTestTotpRecoveryCodes()
        )
    }

    var disableTotpResultProvider: () -> AppResult<Unit> = { AppResult.Success(Unit) }

    var recoveryCodesResultProvider: () -> AppResult<TotpRecoveryCodes> = {
        AppResult.Success(
            createTestTotpRecoveryCodes()
        )
    }

    var regenerateRecoveryCodesResultProvider: () -> AppResult<TotpRecoveryCodes> = {
        AppResult.Success(
            createTestTotpRecoveryCodes()
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
