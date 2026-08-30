package io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.security

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totprecoverycodes.TotpRecoveryCodes
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totpsetup.TotpSetup

/**
 * Manages security settings, multifactor authentication (TOTP), and recovery codes
 * for the current authenticated account.
 */
interface UserSecurityRepository {

    /**
     * Initiates the TOTP setup process by generating a secret key and a configuration URI.
     *
     * @return [TotpSetup] details on success, or a mapped failure.
     */
    suspend fun setupTotp(): AppResult<TotpSetup>

    /**
     * Finalizes and enables TOTP multifactor authentication using a verification code.
     *
     * @param mfaToken Opaque intermediate verification token.
     * @param code Time-based verification code.
     * @return Freshly generated static [TotpRecoveryCodes] on success, or a mapped failure.
     */
    suspend fun enableTotp(mfaToken: String, code: String): AppResult<TotpRecoveryCodes>

    /**
     * Disables TOTP multifactor authentication for the current account.
     *
     * @return Empty success indicator, or a mapped failure.
     */
    suspend fun disableTotp(): AppResult<Unit>

    /**
     * Retrieves the active backup recovery codes for the current account.
     *
     * @return Existing [TotpRecoveryCodes] details on success, or a mapped failure.
     */
    suspend fun getRecoveryCodes(): AppResult<TotpRecoveryCodes>

    /**
     * Invalidates current recovery codes and generates a fresh replacement set.
     *
     * @return Newly generated replacement [TotpRecoveryCodes] on success, or a mapped failure.
     */
    suspend fun regenerateRecoveryCodes(): AppResult<TotpRecoveryCodes>
}