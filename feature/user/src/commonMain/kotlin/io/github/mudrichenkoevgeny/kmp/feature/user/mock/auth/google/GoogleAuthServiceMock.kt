package io.github.mudrichenkoevgeny.kmp.feature.user.mock.auth.google

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.google.GoogleAuthService
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.login.LoginByExternalAuthProviderRequest
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.network.request.auth.login.createTestLoginByExternalAuthProviderRequest

/**
 * Deterministic [GoogleAuthService] for tests and previews.
 */
@InternalApi
class GoogleAuthServiceMock : GoogleAuthService {

    var signInResultProvider: () -> AppResult<LoginByExternalAuthProviderRequest> = {
        AppResult.Success(createTestLoginByExternalAuthProviderRequest())
    }
    var signOutResultProvider: () -> AppResult<Unit> = { AppResult.Success(Unit) }

    override suspend fun signIn(): AppResult<LoginByExternalAuthProviderRequest> = signInResultProvider()

    override suspend fun signOut(): AppResult<Unit> = signOutResultProvider()
}
