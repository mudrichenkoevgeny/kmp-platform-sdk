package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.user.identifier

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.list.IdentifierListComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.list.IdentifierListScreenState
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.list.dialog.AddIdentifierDialogChild
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.list.dialog.AddIdentifierDialogConfig
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId

/**
 * Mock implementation of [IdentifierListComponent] for Compose previews and testing.
 */
@InternalApi
public class IdentifierListComponentMock(
    initialState: IdentifierListScreenState = IdentifierListScreenState.Loading,
    public override val isAddIdentifierSupported: Boolean = true,
    public override val includeFilterPanel: Boolean = false,
    initialDialogSlot: ChildSlot<AddIdentifierDialogConfig, AddIdentifierDialogChild> =
        ChildSlot()
) : IdentifierListComponent {

    private val _state: MutableValue<IdentifierListScreenState> = MutableValue(initialState)
    public override val state: Value<IdentifierListScreenState> = _state

    public override val addIdentifierDialogSlotNavigation: SlotNavigation<AddIdentifierDialogConfig> =
        SlotNavigation<AddIdentifierDialogConfig>()
    public override val addIdentifierDialogSlot:
        Value<ChildSlot<AddIdentifierDialogConfig, AddIdentifierDialogChild>> =
        MutableValue(initialDialogSlot)

    public var refreshCalls: Int = 0
    public var identifierClickCalls: MutableList<UserIdentifierId> = mutableListOf()
    public var identifierDeletedCalls: MutableList<UserIdentifierId> = mutableListOf()
    public var loadNextPageCalls: Int = 0
    public var backCalls: Int = 0
    public var addIdentifierClickCalls: Int = 0
    public var addIdentifierSelectProviderCalls: MutableList<UserAuthProvider> = mutableListOf()
    public var addIdentifierSubmitCalls: Int = 0
    public var addIdentifierDismissCalls: Int = 0

    /**
     * Updates the component state value.
     *
     * @param state The new state value.
     */
    public fun updateState(state: IdentifierListScreenState) {
        _state.value = state
    }

    public override fun onRefresh() {
        refreshCalls++
    }

    public override fun onLoadNextPage() {
        loadNextPageCalls++
    }

    public override fun onToggleFilterPanel(): Unit = Unit

    public override fun onSortChanged(sortState: ListingSortState): Unit = Unit

    public override fun onFilterChanged(
        filterId: String,
        filterState: ListingFilterState?
    ): Unit = Unit

    public override fun onApplyFilters(): Unit = Unit

    public override fun onIdentifierClick(identifierId: UserIdentifierId) {
        identifierClickCalls.add(identifierId)
    }

    public override fun onIdentifierDeleted(identifierId: UserIdentifierId) {
        identifierDeletedCalls.add(identifierId)
    }

    public override fun onAddIdentifierClick() {
        addIdentifierClickCalls++
    }

    public override fun onAddIdentifierSelectProvider(authProvider: UserAuthProvider) {
        addIdentifierSelectProviderCalls.add(authProvider)
    }

    public override fun onAddIdentifierEmailChanged(email: String): Unit = Unit

    public override fun onAddIdentifierPasswordChanged(password: String): Unit = Unit

    public override fun onAddIdentifierTogglePasswordVisibility(): Unit = Unit

    public override fun onAddIdentifierPhoneChanged(phone: String): Unit = Unit

    public override fun onAddIdentifierCodeChanged(code: String): Unit = Unit

    public override fun onAddIdentifierSendCode(): Unit = Unit

    public override fun onAddIdentifierSubmit() {
        addIdentifierSubmitCalls++
    }

    public override fun onAddIdentifierDialogBack(): Unit = Unit

    public override fun onAddIdentifierDialogDismiss() {
        addIdentifierDismissCalls++
    }

    public override fun onBackClick() {
        backCalls++
    }
}
