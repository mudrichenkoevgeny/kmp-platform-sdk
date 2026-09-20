package io.github.mudrichenkoevgeny.kmp.feature.user.network.httpclient.mfa

import io.ktor.client.HttpClient

/**
 * Configuration options for [MfaStepUpPlugin].
 */
class MfaStepUpConfig {
    lateinit var baseUrl: String
    lateinit var reauthenticateRoute: String
    var mfaChallengeHandler: MfaChallengeHandler? = null
    var authClientProvider: (() -> HttpClient)? = null
}
