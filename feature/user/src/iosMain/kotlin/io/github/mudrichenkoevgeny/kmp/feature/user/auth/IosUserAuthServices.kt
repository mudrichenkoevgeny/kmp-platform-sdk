package io.github.mudrichenkoevgeny.kmp.feature.user.auth

import io.github.mudrichenkoevgeny.kmp.feature.user.auth.google.GoogleAuthService
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.google.IosGoogleAuthService
import platform.UIKit.UIViewController

class IosUserAuthServices(
    getRootController: () -> UIViewController,
    googleWebClientId: String
) : UserAuthServices {
    override val googleAuth: GoogleAuthService = IosGoogleAuthService(getRootController, googleWebClientId)
}