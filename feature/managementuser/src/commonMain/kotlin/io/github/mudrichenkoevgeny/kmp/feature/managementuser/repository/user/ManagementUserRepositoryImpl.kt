package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.user

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.user.ManagementUserApi
import io.github.mudrichenkoevgeny.kmp.feature.user.domain.model.user.UpdateUserParams
import io.github.mudrichenkoevgeny.kmp.feature.user.mapper.user.toManagementUpdateUserRequest
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.mapper.pagedresult.mapItems
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.user.toUserPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.user.toUserSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.user.globallist.ManagementUserListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.create.CreateByEmailRequest

/**
 * Implements [ManagementUserRepository] delegating to [ManagementUserApi].
 */
class ManagementUserRepositoryImpl(
    private val managementUserApi: ManagementUserApi
) : ManagementUserRepository {

    override suspend fun createUser(request: CreateByEmailRequest): AppResult<UserPrivate> {
        return managementUserApi.createUser(request).mapSuccess { it.toUserPrivate() }
    }

    override suspend fun getUsers(
        params: ManagementUserListParams?
    ): AppResult<PagedResult<UserSummary>> {
        return managementUserApi.getUsers(params).mapSuccess { pagedPayload ->
            pagedPayload.mapItems { payload -> payload.toUserSummary() }
        }
    }

    override suspend fun getUser(userId: UserId): AppResult<UserPrivate> {
        return managementUserApi.getUser(userId).mapSuccess { it.toUserPrivate() }
    }

    override suspend fun updateUser(userId: UserId, params: UpdateUserParams): AppResult<Unit> {
        return managementUserApi.updateUser(userId, params.toManagementUpdateUserRequest())
    }

    override suspend fun deleteUser(userId: UserId): AppResult<Unit> {
        return managementUserApi.deleteUser(userId)
    }
}
