package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.user

import io.github.mudrichenkoevgeny.kmp.core.common.network.httprequest.parameters
import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.user.globallist.ManagementUserListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.contract.UserApiPaths
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.user.UserPrivatePayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.user.UserSummaryPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.create.CreateByEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.user.ManagementUpdateUserRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.route.management.users.ManagementUsersRoutes
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

    override suspend fun createUser(request: CreateByEmailRequest): AppResult<UserPrivatePayload> = client.callResult {
        post(ManagementUsersRoutes.CREATE_USER) {
            setBody(request)
        }
    }

    override suspend fun getUsers(
        params: ManagementUserListParams?
    ): AppResult<PagedResult<UserSummaryPayload>> = client.callResult {
        get(ManagementUsersRoutes.GET_USERS) {
            parameters(params)
        }
    }

    override suspend fun getUser(userId: UserId): AppResult<UserPrivatePayload> = client.callResult {
        get(ManagementUsersRoutes.GET_USER) {
            parameter(UserApiPaths.USER_ID, userId.value)
        }
    }

    override suspend fun updateUser(userId: UserId, request: ManagementUpdateUserRequest): AppResult<Unit> = client.callResult {
        patch(ManagementUsersRoutes.UPDATE_USER) {
            parameter(UserApiPaths.USER_ID, userId.value)
            setBody(request)
        }
    }

    override suspend fun deleteUser(userId: UserId): AppResult<Unit> = client.callResult {
        delete(ManagementUsersRoutes.DELETE_USER) {
            parameter(UserApiPaths.USER_ID, userId.value)
        }
    }
}
