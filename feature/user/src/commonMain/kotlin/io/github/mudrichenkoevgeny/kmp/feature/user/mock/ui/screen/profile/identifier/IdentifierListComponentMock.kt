package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.identifier

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.IdentifierListComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.IdentifierListScreenState
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId

@InternalApi
class IdentifierListComponentMock(
    initialState: IdentifierListScreenState = IdentifierListScreenState.Loading
) : IdentifierListComponent {

    private val _state = MutableValue(initialState)
    override val state: Value<IdentifierListScreenState> = _state

    var refreshCalls: Int = 0
    var deleteIdentifierCalls: MutableList<UserIdentifierId> = mutableListOf()
    var addEmailCalls: MutableList<String> = mutableListOf()
    var emailCodeChangedCalls: MutableList<String> = mutableListOf()
    var confirmAddEmailCalls: MutableList<String> = mutableListOf()
    var addPhoneCalls: MutableList<String> = mutableListOf()
    var phoneCodeChangedCalls: MutableList<String> = mutableListOf()
    var confirmAddPhoneCalls: Int = 0
    var cancelAddCalls: Int = 0
    var loadNextPageCalls: Int = 0
    var backCalls: Int = 0

    fun updateState(state: IdentifierListScreenState) {
        _state.value = state
    }

    override fun onRefresh() {
        refreshCalls++
    }

    override fun onDeleteIdentifierClick(identifierId: UserIdentifierId) {
        deleteIdentifierCalls.add(identifierId)
    }

    override fun onAddEmailClick(email: String) {
        addEmailCalls.add(email)
    }

    override fun onEmailCodeChanged(code: String) {
        emailCodeChangedCalls.add(code)
    }

    override fun onConfirmAddEmailClick(password: String) {
        confirmAddEmailCalls.add(password)
    }

    override fun onAddPhoneClick(phoneNumber: String) {
        addPhoneCalls.add(phoneNumber)
    }

    override fun onPhoneCodeChanged(code: String) {
        phoneCodeChangedCalls.add(code)
    }

    override fun onConfirmAddPhoneClick() {
        confirmAddPhoneCalls++
    }

    override fun onCancelAddClick() {
        cancelAddCalls++
    }

    override fun onLoadNextPage() {
        loadNextPageCalls++
    }

    override fun onBackClick() {
        backCalls++
    }
}
