package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.identifier.IdentifierRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierPrivate

/**
 * Links a new email identifier to the authenticated account.
 *
 * @param identifierRepository Identifiers repository.
 */
open class AddUserIdentifierEmailUseCase(
    private val identifierRepository: IdentifierRepository
) {
    /**
     * @param email Email address.
     * @param password Account password for confirmation.
     * @param confirmationCode One-time code sent to email.
     * @return New [UserIdentifierPrivate] on success, or a mapped failure.
     */
    open suspend operator fun invoke(
        email: String,
        password: String,
        confirmationCode: String
    ): AppResult<UserIdentifierPrivate> {
        return identifierRepository.addUserIdentifierEmail(
            email = email,
            password = password,
            confirmationCode = confirmationCode
        )
    }
}