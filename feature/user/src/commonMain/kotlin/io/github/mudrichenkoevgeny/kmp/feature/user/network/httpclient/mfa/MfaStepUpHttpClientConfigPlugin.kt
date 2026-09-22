package io.github.mudrichenkoevgeny.kmp.feature.user.network.httpclient.mfa

import io.github.mudrichenkoevgeny.kmp.core.common.network.httpclient.HttpClientConfigPlugin
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.ReauthenticateSessionUseCase
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.logging.Logger

/**
 * [HttpClientConfigPlugin] that installs [MfaStepUpPlugin] into the Ktor HTTP client.
 *
 * @param baseUrl Base URL of the API.
 * @param reauthenticateRoute Endpoint path to post the MFA challenge payload to.
 * @param mfaChallengeHandler Implementation responsible for requesting the MFA code from the user.
 * @param authClientProvider Lazy provider for the [HttpClient] used to send re-authentication requests.
 * @param reauthenticateAction Domain action performing re-authentication via [ReauthenticateSessionUseCase].
 */
class MfaStepUpHttpClientConfigPlugin(
    private val baseUrl: String,
    private val reauthenticateRoute: String,
    private val mfaChallengeHandler: MfaChallengeHandler? = null,
    private val authClientProvider: (() -> HttpClient)? = null,
    private val reauthenticateAction: (suspend (mfaToken: String, code: String) -> AppResult<Unit>)? = null
) : HttpClientConfigPlugin {
    override fun install(
        config: HttpClientConfig<out HttpClientEngineConfig>,
        networkLogger: Logger
    ) {
        config.install(MfaStepUpPlugin) {
            this.baseUrl = this@MfaStepUpHttpClientConfigPlugin.baseUrl
            this.reauthenticateRoute = this@MfaStepUpHttpClientConfigPlugin.reauthenticateRoute
            this.mfaChallengeHandler = this@MfaStepUpHttpClientConfigPlugin.mfaChallengeHandler
            this.authClientProvider = this@MfaStepUpHttpClientConfigPlugin.authClientProvider
            this.reauthenticateAction = this@MfaStepUpHttpClientConfigPlugin.reauthenticateAction
        }
    }
}
