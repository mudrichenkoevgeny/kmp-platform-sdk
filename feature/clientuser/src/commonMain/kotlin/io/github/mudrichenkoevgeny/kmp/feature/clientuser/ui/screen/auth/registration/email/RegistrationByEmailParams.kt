package io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.registration.email

import kotlinx.serialization.Serializable

/**
 * Screen destination parameters for email registration.
 *
 * @param email Optional pre-filled email address.
 * @param isConfirmStep True if navigating directly into code confirmation step.
 */
@Serializable
data class RegistrationByEmailParams(
    val email: String? = null,
    val isConfirmStep: Boolean = false
)
