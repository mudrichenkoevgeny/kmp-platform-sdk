package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.identifier.IdentifierRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId

/**
 * Updates the account password using current credentials.
 *
 * @param identifierRepository Remote identifier management API.
 */
open class EmailChangePasswordUseCase(
    private val identifierRepository: IdentifierRepository
) {
    /**
     * @param userIdentifierId Target identifier ID.
     * @param oldPassword Current valid password.
     * @param newPassword Target password to be applied.
     * @return Success indicator, or a mapped failure.
     */
    open suspend operator fun invoke(
        userIdentifierId: UserIdentifierId,
        oldPassword: String,
        newPassword: String
    ): AppResult<Unit> {
        return identifierRepository.emailChangePassword(userIdentifierId, oldPassword, newPassword)
    }
}
