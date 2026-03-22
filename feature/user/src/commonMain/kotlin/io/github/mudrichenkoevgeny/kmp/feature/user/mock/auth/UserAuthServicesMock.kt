package io.github.mudrichenkoevgeny.kmp.feature.user.mock.auth

import io.github.mudrichenkoevgeny.kmp.feature.user.auth.UserAuthServices
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.google.GoogleAuthService
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.auth.google.MockGoogleAuthService

/**
 * Test/preview [UserAuthServices] with a configurable [GoogleAuthService] (defaults to [MockGoogleAuthService]).
 *
 * @param googleAuth Google delegate; replace with a custom [MockGoogleAuthService] to simulate failures or tokens.
 */
class UserAuthServicesMock(
    override val googleAuth: GoogleAuthService = MockGoogleAuthService()
) : UserAuthServices