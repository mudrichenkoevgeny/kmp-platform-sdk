package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.auth.login.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.auth.login.ManagementLoginDestination
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.email.LoginByEmailComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.welcome.LoginWelcomeComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.resetpassword.ResetEmailPasswordComponent

/**
 * Root of the nested login flow for management: stack navigation over [ManagementLoginDestination] with restricted child components.
 */
interface ManagementLoginRootComponent {
    /**
     * Current navigation stack and active child; observed by [ManagementLoginRootScreen] to render the matching UI.
     *
     * @return reactive stack from Decompose ([Value] of [ChildStack]).
     */
    val stack: Value<ChildStack<ManagementLoginDestination, Child>>

    /**
     * Called when the host should close the login surface (dialog or bottom sheet dismissed).
     */
    fun onDismiss()

    /**
     * Active child for a [ManagementLoginDestination] configuration; holds the corresponding Decompose [component].
     */
    sealed interface Child {
        /**
         * Welcome / provider selection.
         *
         * @param component child Decompose component for this step.
         */
        class Welcome(val component: LoginWelcomeComponent) : Child

        /**
         * Email + password login.
         *
         * @param component child Decompose component for this step.
         */
        class LoginByEmail(val component: LoginByEmailComponent) : Child

        /**
         * Reset password via email confirmation.
         *
         * @param component child Decompose component for this step.
         */
        class ResetEmailPassword(val component: ResetEmailPasswordComponent) : Child
    }
}