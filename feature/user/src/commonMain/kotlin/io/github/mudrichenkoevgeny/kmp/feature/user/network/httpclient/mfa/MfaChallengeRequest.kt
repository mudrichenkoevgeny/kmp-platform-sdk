package io.github.mudrichenkoevgeny.kmp.feature.user.network.httpclient.mfa

import kotlinx.coroutines.CompletableDeferred

/**
 * Encapsulates an active MFA challenge request.
 *
 * @property mfaToken The challenge token sent by the backend.
 * @property deferred Completable deferred resolved when the user submits or cancels the prompt.
 */
data class MfaChallengeRequest(
    val mfaToken: String,
    internal val deferred: CompletableDeferred<String?>
)
