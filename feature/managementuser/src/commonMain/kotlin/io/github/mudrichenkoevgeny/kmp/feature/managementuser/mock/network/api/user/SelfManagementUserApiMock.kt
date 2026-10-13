package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.user

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.user.SelfManagementUserApi
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.user.UserPrivatePayload

@InternalApi
class SelfManagementUserApiMock : SelfManagementUserApi {

    var userResult: AppResult<UserPrivatePayload> = AppResult.Error(CommonError.Unknown())

    override suspend fun getUser(): AppResult<UserPrivatePayload> = userResult
}