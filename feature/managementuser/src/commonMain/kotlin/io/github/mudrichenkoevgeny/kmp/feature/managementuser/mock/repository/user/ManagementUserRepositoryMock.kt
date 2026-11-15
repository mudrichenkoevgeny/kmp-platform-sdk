package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.user

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.user.ManagementUserRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.domain.model.user.UpdateUserParams
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.user.globallist.ManagementUserListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.create.CreateByEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.common.domain.model.listing.createTestPagedResult
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.user.createTestUserPrivate
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.user.createTestUserSummary

/**
 * Mock implementation of [ManagementUserRepository] for testing.
 */
@InternalApi
public class ManagementUserRepositoryMock : ManagementUserRepository {

    public var createUserResultProvider: (CreateByEmailRequest) -> AppResult<UserPrivate> = {
        AppResult.Success(createTestUserPrivate())
    }

    public var getUsersResultProvider: () -> AppResult<PagedResult<UserSummary>> = {
        AppResult.Success(createTestPagedResult(listOf(createTestUserSummary())))
    }

    public var getUserResultProvider: (UserId) -> AppResult<UserPrivate> = {
        AppResult.Success(createTestUserPrivate())
    }

    public var updateUserResultProvider: (UserId, UpdateUserParams) -> AppResult<Unit> = { _, _ ->
        AppResult.Success(Unit)
    }

    public var deleteUserResultProvider: (UserId) -> AppResult<Unit> = { _ ->
        AppResult.Success(Unit)
    }

    public var lastCreateRequest: CreateByEmailRequest? = null
    public var lastUpdateUserId: UserId? = null
    public var lastUpdateParams: UpdateUserParams? = null
    public var lastDeleteUserId: UserId? = null
    public var lastGetUserId: UserId? = null
    public var lastUserListParams: ManagementUserListParams? = null

    public override suspend fun createUser(request: CreateByEmailRequest): AppResult<UserPrivate> {
        lastCreateRequest = request
        return createUserResultProvider(request)
    }

    public override suspend fun getUsers(
        params: ManagementUserListParams?
    ): AppResult<PagedResult<UserSummary>> {
        lastUserListParams = params
        return getUsersResultProvider()
    }

    public override suspend fun getUser(userId: UserId): AppResult<UserPrivate> {
        lastGetUserId = userId
        return getUserResultProvider(userId)
    }

    public override suspend fun updateUser(
        userId: UserId,
        params: UpdateUserParams
    ): AppResult<Unit> {
        lastUpdateUserId = userId
        lastUpdateParams = params
        return updateUserResultProvider(userId, params)
    }

    public override suspend fun deleteUser(userId: UserId): AppResult<Unit> {
        lastDeleteUserId = userId
        return deleteUserResultProvider(userId)
    }
}
