package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.identifier.IdentifierRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier

/**
 * Associates a new phone number identifier with the current account.
 *
 * @param identifierRepository Remote identifier management API.
 */
class AddUserIdentifierPhoneUseCase(
    private val identifierRepository: IdentifierRepository
) {
    /**
     * @param phoneNumber Phone number in E.164 format.
     * @param confirmationCode One-time code sent to the phone.
     * @return New [UserIdentifier] on success, or a mapped failure.
     */
    suspend operator fun invoke(
        phoneNumber: String,
        confirmationCode: String
    ): AppResult<UserIdentifier> {
        return identifierRepository.addUserIdentifierPhone(phoneNumber, confirmationCode)
    }
}
