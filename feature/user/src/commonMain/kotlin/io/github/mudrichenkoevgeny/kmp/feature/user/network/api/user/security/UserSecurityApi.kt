package io.github.mudrichenkoevgeny.kmp.feature.user.network.api.user.security

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.totprecoverycodes.TotpRecoveryCodesPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.totpsetup.TotpSetupPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.verifytotp.VerifyTotpPayload

interface UserSecurityApi {

    suspend fun setupTotp(): AppResult<TotpSetupPayload>

    suspend fun enableTotp(request: VerifyTotpPayload): AppResult<TotpRecoveryCodesPayload>

    suspend fun disableTotp(): AppResult<Unit>

    suspend fun getRecoveryCodes(): AppResult<TotpRecoveryCodesPayload>

    suspend fun regenerateRecoveryCodes(): AppResult<TotpRecoveryCodesPayload>
}