package io.github.mudrichenkoevgeny.kmp.feature.user.mock.auth

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.UserAuthServices
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.google.GoogleAuthService
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.auth.google.GoogleAuthServiceMock

/**
 * Test/preview [UserAuthServices] with a configurable [GoogleAuthService] (defaults to [GoogleAuthServiceMock]).
 *
 * @param googleAuth Google delegate; replace with a custom [GoogleAuthServiceMock] to simulate failures or tokens.
 */
@InternalApi
class UserAuthServicesMock(
    override val googleAuth: GoogleAuthService = GoogleAuthServiceMock()
) : UserAuthServices