package io.github.mudrichenkoevgeny.kmp.feature.user.auth

import android.content.Context
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.google.AndroidGoogleAuthService
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.google.GoogleAuthService

/**
 * Android implementation of [UserAuthServices] backed by [AndroidGoogleAuthService] (Credential Manager + Google ID).
 *
 * @param context Android `Context` used for credential requests.
 * @param googleWebClientId OAuth 2.0 web application (server) client ID passed to `GetGoogleIdOption`.
 */
class AndroidUserAuthServices(
    context: Context,
    googleWebClientId: String
) : UserAuthServices {
    override val googleAuth: GoogleAuthService = AndroidGoogleAuthService(context, googleWebClientId)
}