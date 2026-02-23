package io.github.mudrichenkoevgeny.kmp.feature.user.auth

import android.content.Context
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.google.AndroidGoogleAuthService
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.google.GoogleAuthService

class AndroidUserAuthServices(
    context: Context,
    googleWebClientId: String
) : UserAuthServices {
    override val googleAuth: GoogleAuthService = AndroidGoogleAuthService(context, googleWebClientId)
}