package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.list

import kotlinx.serialization.Serializable

/**
 * Serializable configurations driving the Decompose child slot for the "Add Identifier" modal dialog wizard.
 */
@Serializable
sealed interface AddIdentifierDialogConfig {
    /** First step: selecting email, phone, or social provider. */
    @Serializable
    data object ProviderSelection : AddIdentifierDialogConfig

    /** Step 2a: entering new email address. */
    @Serializable
    data class EmailInput(
        val initialEmail: String = ""
    ) : AddIdentifierDialogConfig

    /** Step 2b: entering email verification OTP. */
    @Serializable
    data class EmailConfirm(
        val email: String,
        val resendTimerSeconds: Int = 0
    ) : AddIdentifierDialogConfig

    /** Step 3a: entering new phone number. */
    @Serializable
    data class PhoneInput(
        val initialPhone: String = ""
    ) : AddIdentifierDialogConfig

    /** Step 3b: entering phone verification OTP. */
    @Serializable
    data class PhoneConfirm(
        val phone: String,
        val resendTimerSeconds: Int = 0
    ) : AddIdentifierDialogConfig
}
