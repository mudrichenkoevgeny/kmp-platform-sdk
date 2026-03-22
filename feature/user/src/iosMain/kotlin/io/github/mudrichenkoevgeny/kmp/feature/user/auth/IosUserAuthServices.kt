package io.github.mudrichenkoevgeny.kmp.feature.user.auth

import io.github.mudrichenkoevgeny.kmp.feature.user.auth.google.GoogleAuthService
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.google.IosGoogleAuthService
import platform.UIKit.UIViewController

/**
 * iOS implementation of [UserAuthServices] backed by [IosGoogleAuthService].
 *
 * @param getRootController Supplies the `UIViewController` used to present Google Sign-In.
 * @param googleWebClientId Server OAuth client ID (`serverClientID`) for ID tokens.
 */
class IosUserAuthServices(
    getRootController: () -> UIViewController,
    googleWebClientId: String
) : UserAuthServices {
    override val googleAuth: GoogleAuthService = IosGoogleAuthService(getRootController, googleWebClientId)
}