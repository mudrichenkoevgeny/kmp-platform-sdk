package io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login

import kotlinx.serialization.Serializable

/**
 * Serializable configurations for the client login flow stack router.
 *
 * Each value identifies one child screen in the nested login navigation graph.
 */
@Serializable
sealed interface ClientLoginDestination {
    /** Entry screen: provider selection and OAuth entry points. */
    @Serializable object Welcome : ClientLoginDestination

    /** Email + password sign-in. */
    @Serializable object LoginByEmail : ClientLoginDestination

    /** Phone number + SMS code sign-in. */
    @Serializable object LoginByPhone : ClientLoginDestination

    /** Email registration with confirmation code. */
    @Serializable object RegistrationByEmail : ClientLoginDestination

    /** Forgot password / reset password by email. */
    @Serializable object ResetEmailPassword : ClientLoginDestination

    /** MFA/TOTP verification after successful initial login. */
    @Serializable data class LoginByTotp(val mfaToken: String) : ClientLoginDestination

    /** Intercepts logins for accounts scheduled for deletion. */
    @Serializable object PendingDeletion : ClientLoginDestination
}