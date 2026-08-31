package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.user.ManagementUserRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.user.UpdateUserRequest

/**
 * Updates profile details, status, or permissions for a specific user administratively.
 *
 * @param managementUserRepository Administrative user management API.
 */
class UpdateUserUseCase(
    private val managementUserRepository: ManagementUserRepository
) {
    /**
     * @param userId Unique account identifier to update.
     * @param request Patch payload containing fields to change.
     * @return Empty success indicator, or a mapped failure.
     */
    suspend operator fun invoke(userId: UserId, request: UpdateUserRequest): AppResult<Unit> {
        return managementUserRepository.updateUser(userId, request)
    }
}
