package io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.api.user.security

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.user.security.UserSecurityApi
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.totprecoverycodes.TotpRecoveryCodesPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.totpsetup.TotpSetupPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.verifytotp.VerifyTotpPayload

/**
 * Mock implementation of [UserSecurityApi] for testing.
 */
@InternalApi
public open class UserSecurityApiMock : UserSecurityApi {

    public var setupTotpResult: AppResult<TotpSetupPayload> = AppResult.Error(CommonError.Unknown())
    public var enableTotpResult: AppResult<TotpRecoveryCodesPayload> = AppResult.Error(CommonError.Unknown())
    public var disableTotpResult: AppResult<Unit> = AppResult.Success(Unit)
    public var getRecoveryCodesResult: AppResult<TotpRecoveryCodesPayload> = AppResult.Error(CommonError.Unknown())
    public var regenerateRecoveryCodesResult: AppResult<TotpRecoveryCodesPayload> = AppResult.Error(
        CommonError.Unknown()
    )

    public override suspend fun setupTotp(): AppResult<TotpSetupPayload> = setupTotpResult

    public override suspend fun enableTotp(request: VerifyTotpPayload): AppResult<TotpRecoveryCodesPayload> =
        enableTotpResult

    public override suspend fun disableTotp(): AppResult<Unit> = disableTotpResult

    public override suspend fun getRecoveryCodes(): AppResult<TotpRecoveryCodesPayload> = getRecoveryCodesResult

    public override suspend fun regenerateRecoveryCodes(): AppResult<TotpRecoveryCodesPayload> =
        regenerateRecoveryCodesResult
}
