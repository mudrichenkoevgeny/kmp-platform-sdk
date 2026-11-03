package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.user.ManagementUserRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.user.globallist.ManagementUserListParams

/**
 * Returns a paginated list of user summaries for administrative user management.
 *
 * @param managementUserRepository Administrative user management API.
 */
class GetUsersUseCase(
    private val managementUserRepository: ManagementUserRepository
) {
    /**
     * @param params Query and filter parameters DTO.
     * @return Paginated result containing user summary domain models, or a mapped failure.
     */
    suspend operator fun invoke(
        params: ManagementUserListParams? = null
    ): AppResult<PagedResult<UserSummary>> {
        return managementUserRepository.getUsers(params)
    }
}
