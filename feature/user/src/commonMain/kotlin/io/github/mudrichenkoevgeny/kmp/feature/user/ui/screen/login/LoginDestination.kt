package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.login

import kotlinx.serialization.Serializable

@Serializable
sealed interface LoginDestination {
    @Serializable object Welcome : LoginDestination
    @Serializable object LoginByEmail : LoginDestination
    @Serializable object LoginByPhone : LoginDestination
    @Serializable object RegistrationByEmail : LoginDestination
//    @Serializable object ForgotPassword : LoginDestination
//    @Serializable object Registration : LoginDestination
}