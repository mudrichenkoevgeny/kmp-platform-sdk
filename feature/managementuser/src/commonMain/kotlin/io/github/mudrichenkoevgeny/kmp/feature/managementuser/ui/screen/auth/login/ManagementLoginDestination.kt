package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.auth.login

import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.totp.LoginByTotpParams
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.AccountUnlockParams
import kotlinx.serialization.Serializable

/**
 * Serializable configurations for the management login flow stack router.
 *
 * Each value identifies one child screen in the nested login navigation graph.
 */
@Serializable
sealed interface ManagementLoginDestination {
    /** Entry screen: provider selection and OAuth entry points. */
    @Serializable object Welcome : ManagementLoginDestination

    /** Email + password sign-in. */
    @Serializable object LoginByEmail : ManagementLoginDestination

    /** Forgot password / reset password by email. */
    @Serializable object ResetEmailPassword : ManagementLoginDestination

    /** MFA/TOTP verification after successful initial login. */
    @Serializable data class LoginByTotp(
        val params: LoginByTotpParams
    ) : ManagementLoginDestination {
        val mfaToken: String get() = params.mfaToken
    }

    /** Intercepts logins for accounts scheduled for deletion. */
    @Serializable object PendingDeletion : ManagementLoginDestination

    /** Intercepts logins for locked accounts. */
    @Serializable data class AccountUnlock(
        val params: AccountUnlockParams = AccountUnlockParams()
    ) : ManagementLoginDestination
}
