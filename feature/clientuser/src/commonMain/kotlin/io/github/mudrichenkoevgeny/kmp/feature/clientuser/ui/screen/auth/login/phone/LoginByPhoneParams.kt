package io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.phone

import kotlinx.serialization.Serializable

/**
 * Screen destination parameters for phone login.
 *
 * @param phone Optional pre-filled phone number.
 * @param isConfirmStep True if navigating directly into code confirmation step.
 */
@Serializable
data class LoginByPhoneParams(
    val phone: String? = null,
    val isConfirmStep: Boolean = false
)
