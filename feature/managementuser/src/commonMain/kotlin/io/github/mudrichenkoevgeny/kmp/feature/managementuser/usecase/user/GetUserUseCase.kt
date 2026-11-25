package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.user.ManagementUserRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserPrivate

/**
 * Retrieves full management-level details of a specific user.
 *
 * @param managementUserRepository Administrative user management API.
 */
public class GetUserUseCase(
    private val managementUserRepository: ManagementUserRepository
) {
    /**
     * @param userId Unique account identifier.
     * @return Detailed information of the target user domain model, or a mapped failure.
     */
    public suspend operator fun invoke(userId: UserId): AppResult<UserPrivate> = managementUserRepository.getUser(
        userId
    )
}
