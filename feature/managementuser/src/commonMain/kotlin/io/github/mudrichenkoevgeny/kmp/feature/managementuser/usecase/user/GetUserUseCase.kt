package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.user.ManagementUserRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserDetails
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId

/**
 * Retrieves full management-level details of a specific user.
 *
 * @param managementUserRepository Administrative user management API.
 */
class GetUserUseCase(
    private val managementUserRepository: ManagementUserRepository
) {
    /**
     * @param userId Unique account identifier.
     * @return Detailed profile information of the target user domain model, or a mapped failure.
     */
    suspend operator fun invoke(userId: UserId): AppResult<UserDetails> {
        return managementUserRepository.getUser(userId)
    }
}
