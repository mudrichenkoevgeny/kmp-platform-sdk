package io.github.mudrichenkoevgeny.kmp.feature.user.network.api.security.password

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.model.useridentifier.UserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.security.password.PasswordChangeRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.route.security.password.PasswordRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class KtorSecurityPasswordApi(
    private val client: HttpClient
) : SecurityPasswordApi {

    override suspend fun changePassword(
        request: PasswordChangeRequest
    ): AppResult<UserIdentifier> = client.callResult {
        post(PasswordRoutes.PASSWORD_CHANGE) {
            setBody(request)
        }
    }
}