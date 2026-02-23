package io.github.mudrichenkoevgeny.kmp.feature.user.auth

import io.github.mudrichenkoevgeny.kmp.feature.user.auth.google.GoogleAuthService
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.google.WasmGoogleAuthService

class WasmUserAuthServices(
    googleWebClientId: String
) : UserAuthServices {
    override val googleAuth: GoogleAuthService = WasmGoogleAuthService(googleWebClientId)
}