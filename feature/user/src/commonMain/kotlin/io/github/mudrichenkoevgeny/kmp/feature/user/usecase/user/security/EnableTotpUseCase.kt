package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.security.UserSecurityRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totprecoverycodes.TotpRecoveryCodes

/**
 * Finalizes and enables TOTP multifactor authentication using a verification code.
 *
 * @param userSecurityRepository Remote security management API.
 */
open class EnableTotpUseCase(
    private val userSecurityRepository: UserSecurityRepository
) {
    /**
     * @param mfaToken Opaque intermediate verification token.
     * @param code Time-based verification code.
     * @return Freshly generated static [TotpRecoveryCodes] on success, or a mapped failure.
     */
    open suspend operator fun invoke(mfaToken: String, code: String): AppResult<TotpRecoveryCodes> {
        return userSecurityRepository.enableTotp(mfaToken, code)
    }
}
