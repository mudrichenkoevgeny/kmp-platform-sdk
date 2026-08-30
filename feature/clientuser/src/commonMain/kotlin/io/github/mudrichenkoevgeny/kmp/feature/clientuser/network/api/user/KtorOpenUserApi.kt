package io.github.mudrichenkoevgeny.kmp.feature.clientuser.network.api.user

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.user.UserDetailsPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.route.open.user.OpenUserRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post

/** [OpenUserApi] backed by [HttpClient]. */
class KtorOpenUserApi(
    private val client: HttpClient
) : OpenUserApi {

    override suspend fun getUser(): AppResult<UserDetailsPayload> = client.callResult {
        get(OpenUserRoutes.GET_USER)
    }

    override suspend fun scheduleUserDeletion(): AppResult<UserDetailsPayload> = client.callResult {
        delete(OpenUserRoutes.SCHEDULE_DELETION)
    }

    override suspend fun restoreUser(): AppResult<UserDetailsPayload> = client.callResult {
        post(OpenUserRoutes.RESTORE_USER)
    }
}