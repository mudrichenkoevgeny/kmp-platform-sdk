package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.login

import kotlinx.serialization.Serializable

@Serializable
sealed interface LoginDestination {
    @Serializable object Welcome : LoginDestination
//    @Serializable object EmailLogin : LoginDestination
//    @Serializable object PhoneLogin : LoginDestination
//    @Serializable object ForgotPassword : LoginDestination
//    @Serializable object Registration : LoginDestination
}