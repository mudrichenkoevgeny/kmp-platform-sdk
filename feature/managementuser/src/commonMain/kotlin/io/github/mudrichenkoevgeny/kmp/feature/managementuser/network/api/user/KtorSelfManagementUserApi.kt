package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.user

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.user.UserPrivatePayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.route.management.user.SelfManagementUserRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.get

/** [SelfManagementUserApi] backed by [HttpClient]. */
class KtorSelfManagementUserApi(
    private val client: HttpClient
) : SelfManagementUserApi {

    override suspend fun getUser(): AppResult<UserPrivatePayload> = client.callResult {
        get(SelfManagementUserRoutes.GET_USER)
    }
}