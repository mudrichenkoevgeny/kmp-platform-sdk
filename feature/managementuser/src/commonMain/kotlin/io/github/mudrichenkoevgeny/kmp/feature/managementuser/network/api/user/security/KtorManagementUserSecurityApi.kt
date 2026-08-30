package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.user.security

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.contract.UserApiPaths
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.route.management.user.security.ManagementUserSecurityRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.parameter

/** [ManagementUserSecurityApi] backed by [HttpClient]. */
class KtorManagementUserSecurityApi(
    private val client: HttpClient
) : ManagementUserSecurityApi {

    override suspend fun disableTotp(userId: UserId): AppResult<Unit> = client.callResult {
        delete(ManagementUserSecurityRoutes.DISABLE_TOTP) {
            parameter(UserApiPaths.USER_ID, userId.value)
        }
    }
}