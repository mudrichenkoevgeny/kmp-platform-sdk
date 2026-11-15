package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.user

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.user.SelfManagementUserApi
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.user.UserPrivatePayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.user.SelfUpdateUserRequest

/**
 * Mock implementation of [SelfManagementUserApi] for testing.
 */
@InternalApi
public class SelfManagementUserApiMock : SelfManagementUserApi {

    public var userResult: AppResult<UserPrivatePayload> = AppResult.Error(CommonError.Unknown())
    public var updateUserResult: AppResult<UserPrivatePayload> = AppResult.Error(CommonError.Unknown())

    public override suspend fun getUser(): AppResult<UserPrivatePayload> = userResult

    public override suspend fun selfUpdateUser(request: SelfUpdateUserRequest): AppResult<UserPrivatePayload> =
        updateUserResult
}
