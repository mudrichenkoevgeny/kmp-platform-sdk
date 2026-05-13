package io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.security

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totprecoverycodes.TotpRecoveryCodes
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totpsetup.TotpSetup

interface UserSecurityRepository {

    suspend fun setupTotp(): AppResult<TotpSetup>

    suspend fun enableTotp(mfaToken: String, code: String): AppResult<TotpRecoveryCodes>

    suspend fun disableTotp(): AppResult<Unit>

    suspend fun getRecoveryCodes(): AppResult<TotpRecoveryCodes>

    suspend fun regenerateRecoveryCodes(): AppResult<TotpRecoveryCodes>
}