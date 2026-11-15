package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.list

import kotlinx.serialization.Serializable

/**
 * Serializable configurations driving the Decompose child slot for the "Add Identifier" modal dialog wizard.
 */
@Serializable
public sealed interface AddIdentifierDialogConfig {
    /** First step: selecting email, phone, or social provider. */
    @Serializable
    public data object ProviderSelection : AddIdentifierDialogConfig

    /** Step 2a: entering new email address. */
    @Serializable
    public data class EmailInput(
        val initialEmail: String = ""
    ) : AddIdentifierDialogConfig

    /** Step 2b: entering email verification OTP. */
    @Serializable
    public data class EmailConfirm(
        val email: String,
        val resendTimerSeconds: Int = 0
    ) : AddIdentifierDialogConfig

    /** Step 3a: entering new phone number. */
    @Serializable
    public data class PhoneInput(
        val initialPhone: String = ""
    ) : AddIdentifierDialogConfig

    /** Step 3b: entering phone verification OTP. */
    @Serializable
    public data class PhoneConfirm(
        val phone: String,
        val resendTimerSeconds: Int = 0
    ) : AddIdentifierDialogConfig
}
