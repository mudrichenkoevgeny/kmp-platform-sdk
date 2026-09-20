package io.github.mudrichenkoevgeny.kmp.feature.user.network.httpclient.mfa

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Default implementation of [MfaChallengeHandler].
 * Exposes active [MfaChallengeRequest] as a [StateFlow] for UI dialog rendering.
 */
class DefaultMfaChallengeHandler : MfaChallengeHandler {

    private val _challengeRequest = MutableStateFlow<MfaChallengeRequest?>(null)
    val challengeRequest: StateFlow<MfaChallengeRequest?> = _challengeRequest.asStateFlow()

    override suspend fun onRequestMfaCode(mfaToken: String): String? {
        val deferred = CompletableDeferred<String?>()
        val request = MfaChallengeRequest(
            mfaToken = mfaToken,
            deferred = deferred
        )
        _challengeRequest.value = request

        val result = deferred.await()
        _challengeRequest.value = null
        return result
    }

    /**
     * Resolves the current challenge with the user-provided TOTP code.
     *
     * @param code Verification secret supplied by the user.
     */
    fun onConfirm(code: String) {
        val current = _challengeRequest.value ?: return
        current.deferred.complete(code)
    }

    /**
     * Cancels the current challenge.
     */
    fun onCancel() {
        val current = _challengeRequest.value ?: return
        current.deferred.complete(null)
    }
}
