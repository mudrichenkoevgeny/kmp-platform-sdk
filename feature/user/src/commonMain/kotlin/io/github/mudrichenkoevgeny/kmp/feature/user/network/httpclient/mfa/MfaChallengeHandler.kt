package io.github.mudrichenkoevgeny.kmp.feature.user.network.httpclient.mfa

/**
 * Handles MFA (Multi-Factor Authentication) challenges requested by the backend
 * when performing sensitive operations (Step-Up Authentication).
 */
public interface MfaChallengeHandler {

    /**
     * Called by the networking layer when a sensitive operation is blocked,
     * requiring the user to confirm their identity.
     *
     * This method suspends until the user provides the code or cancels the flow.
     *
     * @param mfaToken The temporary opaque token identifying the challenge session.
     * @return The verification code entered by the user, or null if the user cancelled the flow.
     */
    public suspend fun onRequestMfaCode(mfaToken: String): String?
}
