package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.LoginDestination
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.email.LoginByEmailComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.phone.LoginByPhoneComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.welcome.LoginWelcomeComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.password.ResetEmailPasswordComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.registration.email.RegistrationByEmailComponent

/**
 * Root of the nested login flow: stack navigation over [LoginDestination] with one active child component.
 *
 * Implementations supply settings, security validation, and user auth use cases from the host-wired modules.
 */
interface LoginRootComponent {
    /**
     * Current navigation stack and active child; observed by [LoginRootScreen] to render the matching UI.
     *
     * @return reactive stack from Decompose ([Value] of [ChildStack]).
     */
    val stack: Value<ChildStack<LoginDestination, Child>>

    /**
     * Called when the host should close the login surface (dialog or bottom sheet dismissed).
     */
    fun onDismiss()

    /**
     * Active child for a [LoginDestination] configuration; holds the corresponding Decompose [component].
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
         * Phone + SMS code login.
         *
         * @param component child Decompose component for this step.
         */
        class LoginByPhone(val component: LoginByPhoneComponent) : Child

        /**
         * Email registration with verification.
         *
         * @param component child Decompose component for this step.
         */
        class RegistrationByEmail(val component: RegistrationByEmailComponent) : Child

        /**
         * Reset password via email confirmation.
         *
         * @param component child Decompose component for this step.
         */
        class ResetEmailPassword(val component: ResetEmailPasswordComponent) : Child
    }
}