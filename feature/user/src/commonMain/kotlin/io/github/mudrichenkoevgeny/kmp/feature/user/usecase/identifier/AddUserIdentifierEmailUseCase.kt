package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.identifier.IdentifierRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier

/**
 * Associates a new email identifier with the current account.
 *
 * @param identifierRepository Remote identifier management API.
 */
class AddUserIdentifierEmailUseCase(
    private val identifierRepository: IdentifierRepository
) {
    /**
     * @param email Account email address.
     * @param password Account password for verification.
     * @param confirmationCode One-time code sent to the email.
     * @return New [UserIdentifier] on success, or a mapped failure.
     */
    suspend operator fun invoke(
        email: String,
        password: String,
        confirmationCode: String
    ): AppResult<UserIdentifier> {
        return identifierRepository.addUserIdentifierEmail(email, password, confirmationCode)
    }
}
