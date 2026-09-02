package io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.ClientLoginDestination
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.email.LoginByEmailComponent
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.phone.LoginByPhoneComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.totp.LoginByTotpComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.welcome.LoginWelcomeComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.resetpassword.ResetEmailPasswordComponent
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.registration.email.RegistrationByEmailComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.pendingdeletion.PendingDeletionComponent

/**
 * Root of the nested login flow: stack navigation over [ClientLoginDestination] with one active child component.
 *
 * Implementations supply settings, security validation, and user auth use cases from the host-wired modules.
 */
interface ClientLoginRootComponent {
    /**
     * Current navigation stack and active child; observed by [ClientLoginRootScreen] to render the matching UI.
     *
     * @return reactive stack from Decompose ([Value] of [ChildStack]).
     */
    val stack: Value<ChildStack<ClientLoginDestination, Child>>

    /**
     * Called when the host should close the login surface (dialog or bottom sheet dismissed).
     */
    fun onDismiss()

    /**
     * Active child for a [ClientLoginDestination] configuration; holds the corresponding Decompose component.
     */
    sealed interface Child {
        /**
         * Welcome / provider selection.
         *
         * @param component Child Decompose component for this step.
         */
        class Welcome(val component: LoginWelcomeComponent) : Child

        /**
         * Email + password login.
         *
         * @param component Child Decompose component for this step.
         */
        class LoginByEmail(val component: LoginByEmailComponent) : Child

        /**
         * Phone + SMS code login.
         *
         * @param component Child Decompose component for this step.
         */
        class LoginByPhone(val component: LoginByPhoneComponent) : Child

        /**
         * Email registration with verification.
         *
         * @param component Child Decompose component for this step.
         */
        class RegistrationByEmail(val component: RegistrationByEmailComponent) : Child

        /**
         * Reset password via email confirmation.
         *
         * @param component Child Decompose component for this step.
         */
        class ResetEmailPassword(val component: ResetEmailPasswordComponent) : Child

        /**
         * MFA/TOTP verification.
         *
         * @param component Child Decompose component for this step.
         */
        class LoginByTotp(val component: LoginByTotpComponent) : Child

        /**
         * Intercepts logins for accounts scheduled for deletion.
         *
         * @param component Child Decompose component for this step.
         */
        class PendingDeletion(val component: PendingDeletionComponent) : Child
    }
}
