package io.github.mudrichenkoevgeny.kmp.feature.user.auth.google

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError

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