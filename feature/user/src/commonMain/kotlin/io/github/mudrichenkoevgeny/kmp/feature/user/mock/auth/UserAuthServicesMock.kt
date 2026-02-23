package io.github.mudrichenkoevgeny.kmp.feature.user.mock.auth

import io.github.mudrichenkoevgeny.kmp.feature.user.auth.UserAuthServices
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.google.GoogleAuthService
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.auth.google.MockGoogleAuthService

class UserAuthServicesMock(
    override val googleAuth: GoogleAuthService = MockGoogleAuthService()
) : UserAuthServices