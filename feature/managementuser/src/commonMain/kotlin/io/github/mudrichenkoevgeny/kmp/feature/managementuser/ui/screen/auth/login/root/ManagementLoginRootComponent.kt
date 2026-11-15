package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.auth.login.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.email.LoginByEmailComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.pendingdeletion.PendingDeletionComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.totp.LoginByTotpComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.welcome.LoginWelcomeComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.resetpassword.ResetEmailPasswordComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.root.UnlockRootComponent
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.route.management.ManagementAuthNavigation

/**
 * Root of the nested login flow for management: stack navigation over [ManagementAuthNavigation] with restricted child components.
 */
public interface ManagementLoginRootComponent {
    /**
     * Current navigation stack and active child; observed by [ManagementLoginRootScreen] to render the matching UI.
     *
     * @return reactive stack from Decompose ([Value] of [ChildStack]).
     */
    public val stack: Value<ChildStack<ManagementAuthNavigation, Child>>

    /**
     * Navigates to the specified [ManagementAuthNavigation] destination.
     *
     * @param destination Destination route.
     */
    public fun navigateTo(destination: ManagementAuthNavigation)

    /**
     * Called when the host should close the login surface (dialog or bottom sheet dismissed).
     */
    public fun onDismiss()

    /**
     * Active child for a [ManagementAuthNavigation] configuration; holds the corresponding Decompose component.
     */
    public sealed interface Child {
        /**
         * Welcome / provider selection.
         *
         * @param component Child Decompose component for this step.
         */
        public class Welcome(public val component: LoginWelcomeComponent) : Child

        /**
         * Email + password login.
         *
         * @param component Child Decompose component for this step.
         */
        public class LoginByEmail(public val component: LoginByEmailComponent) : Child

        /**
         * Reset password via email confirmation.
         *
         * @param component Child Decompose component for this step.
         */
        public class ResetEmailPassword(public val component: ResetEmailPasswordComponent) : Child

        /**
         * MFA/TOTP verification.
         *
         * @param component Child Decompose component for this step.
         */
        public class LoginByTotp(public val component: LoginByTotpComponent) : Child

        /**
         * Intercepts logins for accounts scheduled for deletion.
         *
         * @param component Child Decompose component for this step.
         */
        public class PendingDeletion(public val component: PendingDeletionComponent) : Child

        /**
         * Account unlock flow for locked accounts.
         *
         * @param component Child Decompose component for this step.
         */
        public class AccountUnlock(public val component: UnlockRootComponent) : Child
    }
}
