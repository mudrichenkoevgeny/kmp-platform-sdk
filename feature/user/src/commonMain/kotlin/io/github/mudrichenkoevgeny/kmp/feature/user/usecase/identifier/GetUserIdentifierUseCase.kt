package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.identifier.IdentifierRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId

/**
 * Retrieves specific identifier details by its unique id for the current account.
 *
 * @param identifierRepository Remote identifier management API.
 */
class GetUserIdentifierUseCase(
    private val identifierRepository: IdentifierRepository
) {
    /**
     * @param userIdentifierId Unique identifier payload id.
     * @return Detailed identifier info or a mapped failure.
     */
    suspend operator fun invoke(userIdentifierId: UserIdentifierId): AppResult<UserIdentifier> {
        return identifierRepository.getUserIdentifier(userIdentifierId)
    }
}
