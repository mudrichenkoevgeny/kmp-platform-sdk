package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.user.ManagementUserRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.permission.PermissionCode
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.accountstatus.UserAccountStatus
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserDetails

/**
 * Returns a paginated and filtered list of users based on search criteria for administrative purposes.
 *
 * @param managementUserRepository Administrative user management API.
 */
class GetUsersUseCase(
    private val managementUserRepository: ManagementUserRepository
) {
    /**
     * @param pageNumber One-based page index.
     * @param pageSize Maximum items returned per page.
     * @param sortBy Field to sort by.
     * @param sortOrder Sorting direction.
     * @param roles Filters users by their assigned role types.
     * @param accountStatuses Filters users by current account statuses.
     * @param accountStatusesBeforeDeletion Filters users by status held prior to scheduled deletion.
     * @param authorityLevelFrom Lower bound filter for authority level.
     * @param authorityLevelTo Upper bound filter for authority level.
     * @param isTotpEnabled Filters users by whether TOTP second-factor authentication is active.
     * @param permissionCodes Filters users possessing specific permission codes.
     * @return Paginated result containing user details domain models, or a mapped failure.
     */
    suspend operator fun invoke(
        pageNumber: Int? = null,
        pageSize: Int? = null,
        sortBy: UserSortValues.UserSortBy? = null,
        sortOrder: SortOrder? = null,
        roles: List<UserRole>? = null,
        accountStatuses: List<UserAccountStatus>? = null,
        accountStatusesBeforeDeletion: List<UserAccountStatus>? = null,
        authorityLevelFrom: Int? = null,
        authorityLevelTo: Int? = null,
        isTotpEnabled: Boolean? = null,
        permissionCodes: List<PermissionCode>? = null
    ): AppResult<PagedResult<UserDetails>> {
        return managementUserRepository.getUsers(
            pageNumber = pageNumber,
            pageSize = pageSize,
            sortBy = sortBy,
            sortOrder = sortOrder,
            roles = roles,
            accountStatuses = accountStatuses,
            accountStatusesBeforeDeletion = accountStatusesBeforeDeletion,
            authorityLevelFrom = authorityLevelFrom,
            authorityLevelTo = authorityLevelTo,
            isTotpEnabled = isTotpEnabled,
            permissionCodes = permissionCodes
        )
    }
}
