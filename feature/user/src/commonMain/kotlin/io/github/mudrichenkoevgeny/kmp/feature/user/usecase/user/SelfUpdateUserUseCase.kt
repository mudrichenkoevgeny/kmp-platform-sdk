package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.domain.model.user.UpdateUserParams
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.UserRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserPrivate

/**
 * Partially updates account details and profile fields of the currently authenticated user.
 *
 * @param userRepository User repository for self-management operations.
 */
open class SelfUpdateUserUseCase(
    private val userRepository: UserRepository
) {
    /**
     * @param params Domain parameters containing fields to change.
     * @return Updated current user DTO, or a mapped failure.
     */
    open suspend operator fun invoke(params: UpdateUserParams): AppResult<UserPrivate> {
        return userRepository.selfUpdateUser(params)
    }
}
