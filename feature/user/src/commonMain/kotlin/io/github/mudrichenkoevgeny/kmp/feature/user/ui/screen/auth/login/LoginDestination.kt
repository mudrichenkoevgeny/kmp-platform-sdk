package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login

import kotlinx.serialization.Serializable

/**
 * Serializable configurations for the login flow stack router.
 *
 * Each value identifies one child screen in the nested login navigation graph.
 */
@Serializable
sealed interface LoginDestination {
    /** Entry screen: provider selection and OAuth entry points. */
    @Serializable object Welcome : LoginDestination

    /** Email + password sign-in. */
    @Serializable object LoginByEmail : LoginDestination

    /** Phone number + SMS code sign-in. */
    @Serializable object LoginByPhone : LoginDestination

    /** Email registration with confirmation code. */
    @Serializable object RegistrationByEmail : LoginDestination

    /** Forgot password / reset password by email. */
    @Serializable object ResetEmailPassword : LoginDestination
}