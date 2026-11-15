package io.github.mudrichenkoevgeny.kmp.feature.user.auth.google

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.UserAuthServices
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.login.LoginByExternalAuthProviderRequest

/**
 * No-op Google integration that always fails with [UserError.ExternalAuthFailed].
 *
 * Used when [UserAuthServices.googleAuth] is absent and the use case substitutes a safe default instead of null.
 */
public class DisabledGoogleAuthService : GoogleAuthService {

    private fun error(): AppResult.Error = AppResult.Error(
        UserError.ExternalAuthFailed(
            Exception("Google Auth is not supported")
        )
    )

    public override suspend fun signIn(): AppResult<LoginByExternalAuthProviderRequest> = error()

    public override suspend fun signOut(): AppResult<Unit> = error()
}
