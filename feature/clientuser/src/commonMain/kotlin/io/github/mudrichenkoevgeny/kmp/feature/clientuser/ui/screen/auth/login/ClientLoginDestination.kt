package io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login

import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.phone.LoginByPhoneParams
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.registration.email.RegistrationByEmailParams
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.totp.LoginByTotpParams
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.AccountUnlockParams
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
    @Serializable data class LoginByPhone(
        val params: LoginByPhoneParams = LoginByPhoneParams()
    ) : ClientLoginDestination

    /** Email registration with confirmation code. */
    @Serializable data class RegistrationByEmail(
        val params: RegistrationByEmailParams = RegistrationByEmailParams()
    ) : ClientLoginDestination

    /** Forgot password / reset password by email. */
    @Serializable object ResetEmailPassword : ClientLoginDestination

    /** MFA/TOTP verification after successful initial login. */
    @Serializable data class LoginByTotp(
        val params: LoginByTotpParams
    ) : ClientLoginDestination {
        val mfaToken: String get() = params.mfaToken
    }

    /** Intercepts logins for accounts scheduled for deletion. */
    @Serializable object PendingDeletion : ClientLoginDestination

    /** Intercepts logins for locked accounts. */
    @Serializable data class AccountUnlock(
        val params: AccountUnlockParams = AccountUnlockParams()
    ) : ClientLoginDestination
}
