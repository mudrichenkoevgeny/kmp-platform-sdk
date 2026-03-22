package io.github.mudrichenkoevgeny.kmp.feature.user.auth

import io.github.mudrichenkoevgeny.kmp.feature.user.auth.google.GoogleAuthService

/**
 * Platform-provided authentication integrations for the user feature.
 *
 * Host apps supply an `actual` implementation (or mocks) with concrete [GoogleAuthService] when Google
 * sign-in is enabled; otherwise [googleAuth] may be null and email/phone flows still work.
 */
interface UserAuthServices {
    val googleAuth: GoogleAuthService?
}