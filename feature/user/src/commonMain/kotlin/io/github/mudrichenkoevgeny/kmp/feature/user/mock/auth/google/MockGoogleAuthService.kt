package io.github.mudrichenkoevgeny.kmp.feature.user.mock.auth.google

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.google.GoogleAuthService
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError

class MockGoogleAuthService(
    private val resultToken: String = "mock_id_token",
    private val shouldSucceed: Boolean = true
) : GoogleAuthService {

    override suspend fun signIn(): AppResult<String> {
        return if (shouldSucceed) {
            AppResult.Success(resultToken)
        } else {
            AppResult.Error(UserError.ExternalAuthFailed(null))
        }
    }

    override suspend fun signOut(): AppResult<Unit> {
        return AppResult.Success(Unit)
    }
}