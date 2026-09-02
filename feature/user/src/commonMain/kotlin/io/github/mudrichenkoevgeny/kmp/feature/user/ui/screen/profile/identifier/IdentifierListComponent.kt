package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier

import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId

/**
 * Manages the identifiers lifecycle: listing, adding email/phone, and deleting identifiers.
 */
interface IdentifierListComponent {
    /** Reactive UI state. */
    val state: Value<IdentifierListScreenState>

    /** Refreshes the identifier list. */
    fun onRefresh()

    /** Deletes an identifier. */
    fun onDeleteIdentifierClick(identifierId: UserIdentifierId)

    /** Starts the add email process. */
    fun onAddEmailClick(email: String)

    /** Updates the confirmation code for email addition. */
    fun onEmailCodeChanged(code: String)

    /** Finalizes adding email with code. */
    fun onConfirmAddEmailClick(password: String)

    /** Starts the add phone process. */
    fun onAddPhoneClick(phoneNumber: String)

    /** Updates the confirmation code for phone addition. */
    fun onPhoneCodeChanged(code: String)

    /** Finalizes adding phone with code. */
    fun onConfirmAddPhoneClick()

    /** Cancels any add flow. */
    fun onCancelAddClick()

    /** Requests the next page of identifiers if available. */
    fun onLoadNextPage()

    /** Navigates back. */
    fun onBackClick()
}
