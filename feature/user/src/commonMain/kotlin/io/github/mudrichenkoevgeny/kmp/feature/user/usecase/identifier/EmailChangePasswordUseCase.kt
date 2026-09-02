package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.identifier.IdentifierRepository

/**
 * Updates the account password using current credentials.
 *
 * @param identifierRepository Remote identifier management API.
 */
open class EmailChangePasswordUseCase(
    private val identifierRepository: IdentifierRepository
) {
    /**
     * @param email Account email address.
     * @param oldPassword Current valid password.
     * @param newPassword Target password to be applied.
     * @return Success indicator, or a mapped failure.
     */
    open suspend operator fun invoke(
        email: String,
        oldPassword: String,
        newPassword: String
    ): AppResult<Unit> {
        return identifierRepository.emailChangePassword(email, oldPassword, newPassword)
    }
}
