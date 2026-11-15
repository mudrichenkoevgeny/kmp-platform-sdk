package io.github.mudrichenkoevgeny.kmp.feature.user.network.httpclient.mfa

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.ktor.client.HttpClient

/**
 * Configuration options for [MfaStepUpPlugin].
 */
public class MfaStepUpConfig {
    public lateinit var baseUrl: String
    public lateinit var reauthenticateRoute: String
    public var mfaChallengeHandler: MfaChallengeHandler? = null
    public var authClientProvider: (() -> HttpClient)? = null
    public var reauthenticateAction: (suspend (mfaToken: String, code: String) -> AppResult<Unit>)? = null
}
