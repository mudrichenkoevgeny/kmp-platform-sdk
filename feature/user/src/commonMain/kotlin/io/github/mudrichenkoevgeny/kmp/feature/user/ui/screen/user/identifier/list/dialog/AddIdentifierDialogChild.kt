package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.list.dialog

/**
 * Dialog child instances corresponding to each configuration step.
 */
public sealed interface AddIdentifierDialogChild {
    /** Provider selection step. */
    public data object ProviderSelection : AddIdentifierDialogChild

    /** Email input step. */
    public data class EmailInput(val initialEmail: String) : AddIdentifierDialogChild

    /** Email confirmation step. */
    public data class EmailConfirm(val email: String) : AddIdentifierDialogChild

    /** Phone input step. */
    public data class PhoneInput(val initialPhone: String) : AddIdentifierDialogChild

    /** Phone confirmation step. */
    public data class PhoneConfirm(val phone: String) : AddIdentifierDialogChild
}
