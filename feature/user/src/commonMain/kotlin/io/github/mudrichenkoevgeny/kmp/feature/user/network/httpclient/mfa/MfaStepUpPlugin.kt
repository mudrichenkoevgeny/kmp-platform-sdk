package io.github.mudrichenkoevgeny.kmp.feature.user.network.httpclient.mfa

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.error.model.ApiErrorResponse
import io.github.mudrichenkoevgeny.shared.foundation.core.security.error.naming.SecurityErrorArgs
import io.github.mudrichenkoevgeny.shared.foundation.core.security.error.naming.SecurityErrorCodes
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.verifytotp.VerifyTotpPayload
import io.ktor.client.call.body
import io.ktor.client.call.save
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess

private const val MFA_STEP_UP_PLUGIN_NAME = "MfaStepUpPlugin"

/**
 * Intercepts responses with status code error containing [SecurityErrorCodes.MFA_CONFIRMATION_REQUIRED].
 * Pauses the original request, calls [MfaChallengeHandler] to obtain the code,
 * performs re-authentication via [MfaStepUpConfig.reauthenticateAction] or [MfaStepUpConfig.reauthenticateRoute],
 * and retries the original request upon success.
 */
val MfaStepUpPlugin = createClientPlugin(MFA_STEP_UP_PLUGIN_NAME, ::MfaStepUpConfig) {
    val baseUrl = pluginConfig.baseUrl
    val reauthenticateRoute = pluginConfig.reauthenticateRoute
    val mfaChallengeHandler = pluginConfig.mfaChallengeHandler
    val authClientProvider = pluginConfig.authClientProvider
    val reauthenticateAction = pluginConfig.reauthenticateAction

    on(io.ktor.client.plugins.api.Send) { request ->
        val originalCall = proceed(request)
        val response = originalCall.response

        if (!response.status.isSuccess()) {
            val savedCall = originalCall.save()
            val apiError = try {
                savedCall.response.body<ApiErrorResponse>()
            } catch (_: Exception) {
                null
            }

            if (apiError?.code == SecurityErrorCodes.MFA_CONFIRMATION_REQUIRED) {
                val hasAuthHeader = request.headers.contains(HttpHeaders.Authorization)
                if (!hasAuthHeader) {
                    return@on savedCall
                }

                val handler = mfaChallengeHandler ?: return@on savedCall
                val mfaToken = apiError.args[SecurityErrorArgs.MFA_TOKEN]
                    ?: return@on savedCall

                val code = handler.onRequestMfaCode(mfaToken)
                    ?: return@on savedCall

                val action = reauthenticateAction
                if (action != null) {
                    val result = action(mfaToken, code)
                    if (result !is AppResult.Success) {
                        return@on savedCall
                    }
                } else {
                    val targetClient = authClientProvider?.invoke() ?: client
                    try {
                        val payload = VerifyTotpPayload(mfaToken = mfaToken, code = code)
                        targetClient.post("$baseUrl$reauthenticateRoute") {
                            setBody(payload)
                        }.body<Unit>()
                    } catch (_: Exception) {
                        return@on savedCall
                    }
                }

                val newRequest = HttpRequestBuilder().apply {
                    takeFrom(request)
                }
                return@on proceed(newRequest)
            }
            return@on savedCall
        }
        return@on originalCall
    }
}
