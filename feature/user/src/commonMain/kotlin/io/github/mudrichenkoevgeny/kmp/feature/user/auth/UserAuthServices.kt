package io.github.mudrichenkoevgeny.kmp.feature.user.auth

import io.github.mudrichenkoevgeny.kmp.feature.user.auth.google.GoogleAuthService

interface UserAuthServices {
    val googleAuth: GoogleAuthService?
}