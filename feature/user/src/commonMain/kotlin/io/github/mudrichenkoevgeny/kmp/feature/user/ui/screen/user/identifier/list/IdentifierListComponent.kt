package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.list

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId

/**
 * Universal Decompose component contract for user identifiers listing screens (client & management).
 */
interface IdentifierListComponent : IdentifierListOwner {

    /** Observable screen state flow. */
    val state: Value<IdentifierListScreenState>

    /** Decompose child slot for the "Add Identifier" dialog wizard. */
    val addIdentifierDialogSlot: Value<ChildSlot<AddIdentifierDialogConfig, AddIdentifierDialogChild>>

    /** Slot navigator for the add identifier dialog. */
    val addIdentifierDialogSlotNavigation: SlotNavigation<AddIdentifierDialogConfig>

    /** Dialog child instances corresponding to each configuration step. */
    sealed interface AddIdentifierDialogChild {
        data object ProviderSelection : AddIdentifierDialogChild
        data class EmailInput(val initialEmail: String) : AddIdentifierDialogChild
        data class EmailConfirm(val email: String) : AddIdentifierDialogChild
        data class PhoneInput(val initialPhone: String) : AddIdentifierDialogChild
        data class PhoneConfirm(val phone: String) : AddIdentifierDialogChild
    }

    /** Whether adding a new identifier is available for this contour. */
    val isAddIdentifierSupported: Boolean

    /** Whether filter panel features are included (administrative mode). */
    val includeFilterPanel: Boolean

    /** Navigates back to previous screen. */
    fun onBackClick()

    /** Refreshes current identifier list page. */
    fun onRefresh()

    /** Triggers loading next page. */
    fun onLoadNextPage()

    /** Toggles expansion state of filter options panel. */
    fun onToggleFilterPanel()

    /** Called when active sort state changes. */
    fun onSortChanged(sortState: ListingSortState)

    /** Called when an individual filter state changes. */
    fun onFilterChanged(filterId: String, filterState: ListingFilterState?)

    /** Applies selected filters and re-fetches list. */
    fun onApplyFilters()

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
}
