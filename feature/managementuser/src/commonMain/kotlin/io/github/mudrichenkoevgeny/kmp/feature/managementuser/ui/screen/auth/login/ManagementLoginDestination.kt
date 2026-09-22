package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.auth.login

import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.accountlockout.AccountLockoutType
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
    @Serializable data class LoginByTotp(val mfaToken: String) : ManagementLoginDestination

    /** Intercepts logins for accounts scheduled for deletion. */
    @Serializable object PendingDeletion : ManagementLoginDestination

    /** Intercepts logins for locked accounts. */
    @Serializable data class AccountUnlock(
        val lockoutType: AccountLockoutType? = null,
        val lockoutUntil: Long? = null
    ) : ManagementLoginDestination
}
