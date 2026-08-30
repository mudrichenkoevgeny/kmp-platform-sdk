package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.auth.login

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
}