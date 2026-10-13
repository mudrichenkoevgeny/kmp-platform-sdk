package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.identifier.IdentifierRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierPrivate

/**
 * Links a new phone number identifier to the authenticated account.
 *
 * @param identifierRepository Identifiers repository.
 */
open class AddUserIdentifierPhoneUseCase(
    private val identifierRepository: IdentifierRepository
) {
    /**
     * @param phoneNumber Phone number string.
     * @param confirmationCode Verification code delivered to phone.
     * @return New [UserIdentifierPrivate] on success, or a mapped failure.
     */
    open suspend operator fun invoke(
        phoneNumber: String,
        confirmationCode: String
    ): AppResult<UserIdentifierPrivate> {
        return identifierRepository.addUserIdentifierPhone(
            phoneNumber = phoneNumber,
            confirmationCode = confirmationCode
        )
    }
}