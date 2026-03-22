package io.github.mudrichenkoevgeny.kmp.feature.user.auth.google

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.UserAuthServices
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError

/**
 * No-op Google integration that always fails with [UserError.ExternalAuthFailed].
 *
 * Used when [UserAuthServices.googleAuth] is absent and the use case substitutes a safe default instead of null.
 */
class DisabledGoogleAuthService : GoogleAuthService {

    private fun error() = AppResult.Error(
        UserError.ExternalAuthFailed(
            Exception("Google Auth is not supported")
        )
    )
    override suspend fun signIn(): AppResult<String> {
        return error()
    }

    override suspend fun signOut(): AppResult<Unit> {
        return error()
    }
}