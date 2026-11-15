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
public interface IdentifierListComponent : IdentifierListOwner {

    /** Observable screen state flow. */
    public val state: Value<IdentifierListScreenState>

    /** Decompose child slot for the "Add Identifier" dialog wizard. */
    public val addIdentifierDialogSlot: Value<ChildSlot<AddIdentifierDialogConfig, AddIdentifierDialogChild>>

    /** Slot navigator for the add identifier dialog. */
    public val addIdentifierDialogSlotNavigation: SlotNavigation<AddIdentifierDialogConfig>

    /** Dialog child instances corresponding to each configuration step. */
    public sealed interface AddIdentifierDialogChild {
        public data object ProviderSelection : AddIdentifierDialogChild
        public data class EmailInput(val initialEmail: String) : AddIdentifierDialogChild
        public data class EmailConfirm(val email: String) : AddIdentifierDialogChild
        public data class PhoneInput(val initialPhone: String) : AddIdentifierDialogChild
        public data class PhoneConfirm(val phone: String) : AddIdentifierDialogChild
    }

    /** Whether adding a new identifier is available for this contour. */
    public val isAddIdentifierSupported: Boolean

    /** Whether filter panel features are included (administrative mode). */
    public val includeFilterPanel: Boolean

    /** Navigates back to previous screen. */
    public fun onBackClick()

    /** Refreshes current identifier list page. */
    public fun onRefresh()

    /** Triggers loading next page. */
    public fun onLoadNextPage()

    /** Toggles expansion state of filter options panel. */
    public fun onToggleFilterPanel()

    /** Called when active sort state changes. */
    public fun onSortChanged(sortState: ListingSortState)

    /** Called when an individual filter state changes. */
    public fun onFilterChanged(
        filterId: String,
        filterState: ListingFilterState?
    )

    /** Applies selected filters and re-fetches list. */
    public fun onApplyFilters()

    /** Navigates to identifier detail screen. */
    public fun onIdentifierClick(identifierId: UserIdentifierId)

    /** Opens the add identifier dialog. */
    public fun onAddIdentifierClick()

    /** Selects an auth provider from the dialog. */
    public fun onAddIdentifierSelectProvider(authProvider: UserAuthProvider)

    /** Updates email input in dialog. */
    public fun onAddIdentifierEmailChanged(email: String)

    /** Updates password input in dialog. */
    public fun onAddIdentifierPasswordChanged(password: String)

    /** Toggles password visibility in dialog. */
    public fun onAddIdentifierTogglePasswordVisibility()

    /** Updates phone number input in dialog. */
    public fun onAddIdentifierPhoneChanged(phone: String)

    /** Updates confirmation code input in dialog. */
    public fun onAddIdentifierCodeChanged(code: String)

    /** Sends confirmation code for the active dialog flow. */
    public fun onAddIdentifierSendCode()

    /** Submits adding the identifier with confirmed code/password. */
    public fun onAddIdentifierSubmit()

    /** Goes back to provider selection or closes dialog. */
    public fun onAddIdentifierDialogBack()

    /** Closes the add identifier dialog. */
    public fun onAddIdentifierDialogDismiss()
}
