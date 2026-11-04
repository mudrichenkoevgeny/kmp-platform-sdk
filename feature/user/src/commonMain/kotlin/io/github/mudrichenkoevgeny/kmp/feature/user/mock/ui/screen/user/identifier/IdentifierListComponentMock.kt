package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.user.identifier

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.list.AddIdentifierDialogConfig
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.list.IdentifierListComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.list.IdentifierListScreenState
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId

@InternalApi
class IdentifierListComponentMock(
    initialState: IdentifierListScreenState = IdentifierListScreenState.Loading,
    override val isAddIdentifierSupported: Boolean = true,
    override val includeFilterPanel: Boolean = false,
    initialDialogSlot: ChildSlot<AddIdentifierDialogConfig, IdentifierListComponent.AddIdentifierDialogChild> = ChildSlot()
) : IdentifierListComponent {

    private val _state = MutableValue(initialState)
    override val state: Value<IdentifierListScreenState> = _state

    override val addIdentifierDialogSlotNavigation = SlotNavigation<AddIdentifierDialogConfig>()
    override val addIdentifierDialogSlot = MutableValue(initialDialogSlot)

    var refreshCalls: Int = 0
    var identifierClickCalls: MutableList<UserIdentifierId> = mutableListOf()
    var identifierDeletedCalls: MutableList<UserIdentifierId> = mutableListOf()
    var loadNextPageCalls: Int = 0
    var backCalls: Int = 0
    var addIdentifierClickCalls: Int = 0
    var addIdentifierSelectProviderCalls: MutableList<UserAuthProvider> = mutableListOf()
    var addIdentifierSubmitCalls: Int = 0
    var addIdentifierDismissCalls: Int = 0

    fun updateState(state: IdentifierListScreenState) {
        _state.value = state
    }

    override fun onRefresh() {
        refreshCalls++
    }

    override fun onLoadNextPage() {
        loadNextPageCalls++
    }

    override fun onToggleFilterPanel() {
    }

    override fun onSortChanged(sortState: ListingSortState) {
    }

    override fun onFilterChanged(filterId: String, filterState: ListingFilterState?) {
    }

    override fun onApplyFilters() {
    }

    override fun onIdentifierClick(identifierId: UserIdentifierId) {
        identifierClickCalls.add(identifierId)
    }

    override fun onIdentifierDeleted(identifierId: UserIdentifierId) {
        identifierDeletedCalls.add(identifierId)
    }

    override fun onAddIdentifierClick() {
        addIdentifierClickCalls++
    }

    override fun onAddIdentifierSelectProvider(authProvider: UserAuthProvider) {
        addIdentifierSelectProviderCalls.add(authProvider)
    }

    override fun onAddIdentifierEmailChanged(email: String) {
    }

    override fun onAddIdentifierPasswordChanged(password: String) {
    }

    override fun onAddIdentifierTogglePasswordVisibility() {
    }

    override fun onAddIdentifierPhoneChanged(phone: String) {
    }

    override fun onAddIdentifierCodeChanged(code: String) {
    }

    override fun onAddIdentifierSendCode() {
    }

    override fun onAddIdentifierSubmit() {
        addIdentifierSubmitCalls++
    }

    override fun onAddIdentifierDialogBack() {
    }

    override fun onAddIdentifierDialogDismiss() {
        addIdentifierDismissCalls++
    }

    override fun onBackClick() {
        backCalls++
    }
}
