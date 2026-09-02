package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.security.UserSecurityRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totprecoverycodes.TotpRecoveryCodes

/**
 * Invalidates current recovery codes and generates a fresh replacement set.
 *
 * @param userSecurityRepository Remote security management API.
 */
open class RegenerateRecoveryCodesUseCase(
    private val userSecurityRepository: UserSecurityRepository
) {
    /**
     * @return Newly generated replacement [TotpRecoveryCodes] on success, or a mapped failure.
     */
    open suspend operator fun invoke(): AppResult<TotpRecoveryCodes> {
        return userSecurityRepository.regenerateRecoveryCodes()
    }
}
