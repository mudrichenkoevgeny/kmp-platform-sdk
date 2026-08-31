package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.user.ManagementUserRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId

/**
 * Completely deletes a specified user account administratively.
 *
 * @param managementUserRepository Administrative user management API.
 */
class DeleteUserUseCase(
    private val managementUserRepository: ManagementUserRepository
) {
    /**
     * @param userId Unique account identifier to remove.
     * @return Empty success indicator, or a mapped failure.
     */
    suspend operator fun invoke(userId: UserId): AppResult<Unit> {
        return managementUserRepository.deleteUser(userId)
    }
}
