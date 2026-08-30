package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.user

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.ListingParamNames
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.permission.PermissionCode
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.accountstatus.UserAccountStatus
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserFilterValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.contract.UserApiPaths
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.user.UserDetailsPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.create.CreateByEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.user.UpdateUserRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.route.management.user.ManagementUserRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody

/** [ManagementUserApi] backed by [HttpClient]. */
class KtorManagementUserApi(
    private val client: HttpClient
) : ManagementUserApi {

    override suspend fun createUser(request: CreateByEmailRequest): AppResult<UserDetailsPayload> = client.callResult {
        post(ManagementUserRoutes.CREATE_USER) {
            setBody(request)
        }
    }

    override suspend fun getUsers(
        pageNumber: Int?,
        pageSize: Int?,
        sortBy: UserSortValues.UserSortBy?,
        sortOrder: SortOrder?,
        roles: List<UserRole>?,
        accountStatuses: List<UserAccountStatus>?,
        accountStatusesBeforeDeletion: List<UserAccountStatus>?,
        authorityLevelFrom: Int?,
        authorityLevelTo: Int?,
        isTotpEnabled: Boolean?,
        permissionCodes: List<PermissionCode>?
    ): AppResult<PagedResult<UserDetailsPayload>> = client.callResult {
        get(ManagementUserRoutes.GET_USERS) {
            parameter(ListingParamNames.Pagination.PAGE_NUMBER, pageNumber)
            parameter(ListingParamNames.Pagination.PAGE_SIZE, pageSize)
            parameter(ListingParamNames.Sort.SORT_BY, sortBy?.serialName)
            parameter(ListingParamNames.Sort.SORT_ORDER, sortOrder?.serialName)
            roles?.forEach { role ->
                parameter(UserFilterValues.UserFilterValues.ROLE, role.serialName)
            }
            accountStatuses?.forEach { accountStatus ->
                parameter(UserFilterValues.UserFilterValues.ACCOUNT_STATUS, accountStatus.serialName)
            }
            accountStatusesBeforeDeletion?.forEach { accountStatusBeforeDeletion ->
                parameter(
                    UserFilterValues.UserFilterValues.ACCOUNT_STATUS_BEFORE_DELETION,
                    accountStatusBeforeDeletion.serialName
                )
            }
            parameter(UserFilterValues.UserFilterValues.AUTHORITY_LEVEL_FROM, authorityLevelFrom)
            parameter(UserFilterValues.UserFilterValues.AUTHORITY_LEVEL_TO, authorityLevelTo)
            parameter(UserFilterValues.UserFilterValues.IS_TOTP_ENABLED, isTotpEnabled)
            permissionCodes?.forEach { permissionCode ->
                parameter(UserFilterValues.UserFilterValues.PERMISSION_CODES, permissionCode.value)
            }
        }
    }

    override suspend fun getUser(userId: UserId): AppResult<UserDetailsPayload> = client.callResult {
        get(ManagementUserRoutes.GET_USER) {
            parameter(UserApiPaths.USER_ID, userId.value)
        }
    }

    override suspend fun updateUser(userId: UserId, request: UpdateUserRequest): AppResult<Unit> = client.callResult {
        patch(ManagementUserRoutes.UPDATE_USER) {
            parameter(UserApiPaths.USER_ID, userId.value)
            setBody(request)
        }
    }

    override suspend fun deleteUser(userId: UserId): AppResult<Unit> = client.callResult {
        delete(ManagementUserRoutes.DELETE_USER) {
            parameter(UserApiPaths.USER_ID, userId.value)
        }
    }
}