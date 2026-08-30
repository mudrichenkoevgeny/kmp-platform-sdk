package io.github.mudrichenkoevgeny.kmp.feature.user.network.api.user.security

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.totprecoverycodes.TotpRecoveryCodesPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.totpsetup.TotpSetupPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.verifytotp.VerifyTotpPayload

/** Self-service security management for the authenticated user, including TOTP and recovery codes configuration. */
interface UserSecurityApi {

    /**
     * Generates a new TOTP secret and returns initialization setup data.
     *
     * @return TOTP setup details including secret key and challenge token, or a mapped failure.
     */
    suspend fun setupTotp(): AppResult<TotpSetupPayload>

    /**
     * Finalizes and enables TOTP activation by verifying the initial token code.
     *
     * @param request Verification payload containing the current TOTP token code.
     * @return Initial set of generated backup recovery codes, or a mapped failure.
     */
    suspend fun enableTotp(request: VerifyTotpPayload): AppResult<TotpRecoveryCodesPayload>

    /**
     * Disables TOTP and invalidates all associated backup recovery codes for the account.
     *
     * @return Empty success indicator, or a mapped failure.
     */
    suspend fun disableTotp(): AppResult<Unit>

    /**
     * Returns the current active backup recovery codes for the account.
     *
     * @return Existing active recovery codes payload, or a mapped failure.
     */
    suspend fun getRecoveryCodes(): AppResult<TotpRecoveryCodesPayload>

    /**
     * Invalidates all existing backup recovery codes and generates a completely new set.
     *
     * @return Fresh set of generated backup recovery codes payload, or a mapped failure.
     */
    suspend fun regenerateRecoveryCodes(): AppResult<TotpRecoveryCodesPayload>
}