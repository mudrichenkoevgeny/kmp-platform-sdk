package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.user.ManagementUserRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.domain.model.user.UpdateUserParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId

/**
 * Updates user details, status, or permissions for a specific user administratively.
 *
 * @param managementUserRepository Administrative user management API.
 */
public class UpdateUserUseCase(
    private val managementUserRepository: ManagementUserRepository
) {
    /**
     * @param userId Unique account identifier to update.
     * @param params Domain parameters containing fields to change.
     * @return Empty success indicator, or a mapped failure.
     */
    public suspend operator fun invoke(
        userId: UserId,
        params: UpdateUserParams
    ): AppResult<Unit> = managementUserRepository.updateUser(userId, params)
}
