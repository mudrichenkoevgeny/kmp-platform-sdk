package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.user.security

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.user.security.UserSecurityApi
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.totprecoverycodes.TotpRecoveryCodesPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.totpsetup.TotpSetupPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.verifytotp.VerifyTotpPayload

@InternalApi
class SelfManagementUserSecurityApiMock : UserSecurityApi {
    var setupTotpResult: AppResult<TotpSetupPayload> = AppResult.Error(CommonError.Unknown())
    var enableTotpResult: AppResult<TotpRecoveryCodesPayload> = AppResult.Error(CommonError.Unknown())
    var disableTotpResult: AppResult<Unit> = AppResult.Error(CommonError.Unknown())
    var getRecoveryCodesResult: AppResult<TotpRecoveryCodesPayload> = AppResult.Error(CommonError.Unknown())
    var regenerateRecoveryCodesResult: AppResult<TotpRecoveryCodesPayload> = AppResult.Error(CommonError.Unknown())

    override suspend fun setupTotp() = setupTotpResult
    override suspend fun enableTotp(request: VerifyTotpPayload) = enableTotpResult
    override suspend fun disableTotp() = disableTotpResult
    override suspend fun getRecoveryCodes() = getRecoveryCodesResult
    override suspend fun regenerateRecoveryCodes() = regenerateRecoveryCodesResult
}