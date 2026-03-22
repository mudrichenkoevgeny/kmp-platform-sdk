package io.github.mudrichenkoevgeny.kmp.feature.user.auth.google

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError

/**
 * Cross-platform contract for Google Sign-In used by user login flows.
 *
 * Implementations run platform UI (Credential Manager, Google Identity Services, GIDSignIn, etc.) and surface outcomes as [AppResult].
 */
interface GoogleAuthService {
    /**
     * Runs interactive Google sign-in and returns a backend-ready credential (typically a Google ID token string).
     *
     * @return Success with the token, or [AppResult.Error] wrapping [UserError.ExternalAuthCancelled] / [UserError.ExternalAuthFailed].
     */
    suspend fun signIn(): AppResult<String>

    /**
     * Clears the on-device Google session or credential state where the platform API supports it.
     *
     * @return Success on clean sign-out, or an error if the platform call fails.
     */
    suspend fun signOut(): AppResult<Unit>
}