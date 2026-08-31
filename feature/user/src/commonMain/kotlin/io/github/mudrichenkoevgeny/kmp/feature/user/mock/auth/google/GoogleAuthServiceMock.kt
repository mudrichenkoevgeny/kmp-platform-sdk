package io.github.mudrichenkoevgeny.kmp.feature.user.mock.auth.google

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.google.GoogleAuthService
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError

/**
 * Deterministic [GoogleAuthService] for tests and previews.
 */
@InternalApi
class GoogleAuthServiceMock : GoogleAuthService {

    var signInResultProvider: () -> AppResult<String> = { AppResult.Success("mock_id_token") }
    var signOutResultProvider: () -> AppResult<Unit> = { AppResult.Success(Unit) }

    override suspend fun signIn(): AppResult<String> = signInResultProvider()

    override suspend fun signOut(): AppResult<Unit> = signOutResultProvider()
}
