package io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.api.user.security

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.user.security.UserSecurityApi
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.totprecoverycodes.TotpRecoveryCodesPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.totpsetup.TotpSetupPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.verifytotp.VerifyTotpPayload

@InternalApi
open class UserSecurityApiMock : UserSecurityApi {

    var setupTotpResult: AppResult<TotpSetupPayload> = AppResult.Error(CommonError.Unknown())
    var enableTotpResult: AppResult<TotpRecoveryCodesPayload> = AppResult.Error(CommonError.Unknown())
    var disableTotpResult: AppResult<Unit> = AppResult.Success(Unit)
    var getRecoveryCodesResult: AppResult<TotpRecoveryCodesPayload> = AppResult.Error(CommonError.Unknown())
    var regenerateRecoveryCodesResult: AppResult<TotpRecoveryCodesPayload> = AppResult.Error(CommonError.Unknown())

    override suspend fun setupTotp(): AppResult<TotpSetupPayload> = setupTotpResult

    override suspend fun enableTotp(request: VerifyTotpPayload): AppResult<TotpRecoveryCodesPayload> = enableTotpResult

    override suspend fun disableTotp(): AppResult<Unit> = disableTotpResult

    override suspend fun getRecoveryCodes(): AppResult<TotpRecoveryCodesPayload> = getRecoveryCodesResult

    override suspend fun regenerateRecoveryCodes(): AppResult<TotpRecoveryCodesPayload> = regenerateRecoveryCodesResult
}
