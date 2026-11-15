package io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.user.security

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.security.UserSecurityRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totprecoverycodes.TotpRecoveryCodes
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totpsetup.TotpSetup
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.security.domain.model.totprecoverycodes.createTestTotpRecoveryCodes
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.security.domain.model.totpsetup.createTestTotpSetup

/**
 * Mock implementation of [UserSecurityRepository] for testing.
 */
@InternalApi
public open class UserSecurityRepositoryMock : UserSecurityRepository {

    public var totpSetupResultProvider: () -> AppResult<TotpSetup> = {
        AppResult.Success(
            createTestTotpSetup()
        )
    }

    public var enableTotpResultProvider: (mfaToken: String, code: String) -> AppResult<TotpRecoveryCodes> = { _, _ ->
        AppResult.Success(
            createTestTotpRecoveryCodes()
        )
    }

    public var disableTotpResultProvider: () -> AppResult<Unit> = { AppResult.Success(Unit) }

    public var recoveryCodesResultProvider: () -> AppResult<TotpRecoveryCodes> = {
        AppResult.Success(
            createTestTotpRecoveryCodes()
        )
    }

    public var regenerateRecoveryCodesResultProvider: () -> AppResult<TotpRecoveryCodes> = {
        AppResult.Success(
            createTestTotpRecoveryCodes()
        )
    }

    public var lastMfaToken: String? = null
    public var lastCode: String? = null

    public override suspend fun setupTotp(): AppResult<TotpSetup> = totpSetupResultProvider()

    public override suspend fun enableTotp(
        mfaToken: String,
        code: String
    ): AppResult<TotpRecoveryCodes> {
        lastMfaToken = mfaToken
        lastCode = code
        return enableTotpResultProvider(mfaToken, code)
    }

    public override suspend fun disableTotp(): AppResult<Unit> = disableTotpResultProvider()

    public override suspend fun getRecoveryCodes(): AppResult<TotpRecoveryCodes> = recoveryCodesResultProvider()

    public override suspend fun regenerateRecoveryCodes(): AppResult<TotpRecoveryCodes> =
        regenerateRecoveryCodesResultProvider()
}
