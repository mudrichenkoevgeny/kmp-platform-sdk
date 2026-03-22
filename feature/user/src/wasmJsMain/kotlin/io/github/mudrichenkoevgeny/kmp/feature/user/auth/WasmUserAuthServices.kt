package io.github.mudrichenkoevgeny.kmp.feature.user.auth

import io.github.mudrichenkoevgeny.kmp.feature.user.auth.google.GoogleAuthService
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.google.WasmGoogleAuthService

/**
 * Wasm/browser implementation of [UserAuthServices] using [WasmGoogleAuthService] (Google Identity Services / `google.accounts.id`).
 *
 * @param googleWebClientId Google Identity client ID configured for your web app.
 */
class WasmUserAuthServices(
    googleWebClientId: String
) : UserAuthServices {
    override val googleAuth: GoogleAuthService = WasmGoogleAuthService(googleWebClientId)
}