package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.list

import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId

/**
 * Manages the self user identifiers lifecycle: listing and navigation to identifier details or add flow.
 */
interface SelfIdentifierListComponent : IdentifierListOwner {
    /** Reactive UI state. */
    val state: Value<SelfIdentifierListScreenState>

    /** Refreshes the identifier list. */
    fun onRefresh()

    /** Navigates to identifier detail screen. */
    fun onIdentifierClick(identifierId: UserIdentifierId)

    /** Opens the add identifier dialog. */
    fun onAddIdentifierClick()

    /** Selects an auth provider from the dialog. */
    fun onAddIdentifierSelectProvider(authProvider: UserAuthProvider)

    /** Updates email input in dialog. */
    fun onAddIdentifierEmailChanged(email: String)

    /** Updates password input in dialog. */
    fun onAddIdentifierPasswordChanged(password: String)

    /** Toggles password visibility in dialog. */
    fun onAddIdentifierTogglePasswordVisibility()

    /** Updates phone number input in dialog. */
    fun onAddIdentifierPhoneChanged(phone: String)

    /** Updates confirmation code input in dialog. */
    fun onAddIdentifierCodeChanged(code: String)

    /** Sends confirmation code for the active dialog flow. */
    fun onAddIdentifierSendCode()

    /** Submits adding the identifier with confirmed code/password. */
    fun onAddIdentifierSubmit()

    /** Goes back to provider selection or closes dialog. */
    fun onAddIdentifierDialogBack()

    /** Closes the add identifier dialog. */
    fun onAddIdentifierDialogDismiss()

    /** Requests the next page of identifiers if available. */
    fun onLoadNextPage()

    /** Navigates back. */
    fun onBackClick()
}
