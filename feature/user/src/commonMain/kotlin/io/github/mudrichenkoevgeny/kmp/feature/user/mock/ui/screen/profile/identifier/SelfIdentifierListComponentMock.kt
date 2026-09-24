package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.identifier

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.list.SelfIdentifierListComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.list.SelfIdentifierListScreenState
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId

@InternalApi
class SelfIdentifierListComponentMock(
    initialState: SelfIdentifierListScreenState = SelfIdentifierListScreenState.Loading
) : SelfIdentifierListComponent {

    private val _state = MutableValue(initialState)
    override val state: Value<SelfIdentifierListScreenState> = _state

    var refreshCalls: Int = 0
    var identifierClickCalls: MutableList<UserIdentifierId> = mutableListOf()
    var addIdentifierCalls: Int = 0
    var selectProviderCalls = mutableListOf<UserAuthProvider>()
    var emailChangeCalls = mutableListOf<String>()
    var passwordChangeCalls = mutableListOf<String>()
    var togglePasswordVisibilityCalls = 0
    var phoneChangeCalls = mutableListOf<String>()
    var codeChangeCalls = mutableListOf<String>()
    var sendCodeCalls = 0
    var submitCalls = 0
    var dialogBackCalls = 0
    var dialogDismissCalls = 0
    var loadNextPageCalls: Int = 0
    var backCalls: Int = 0

    fun updateState(state: SelfIdentifierListScreenState) {
        _state.value = state
    }

    override fun onRefresh() {
        refreshCalls++
    }

    override fun onIdentifierClick(identifierId: UserIdentifierId) {
        identifierClickCalls.add(identifierId)
    }

    override fun onAddIdentifierClick() {
        addIdentifierCalls++
    }

    override fun onAddIdentifierSelectProvider(authProvider: UserAuthProvider) {
        selectProviderCalls.add(authProvider)
    }

    override fun onAddIdentifierEmailChanged(email: String) {
        emailChangeCalls.add(email)
    }

    override fun onAddIdentifierPasswordChanged(password: String) {
        passwordChangeCalls.add(password)
    }

    override fun onAddIdentifierTogglePasswordVisibility() {
        togglePasswordVisibilityCalls++
    }

    override fun onAddIdentifierPhoneChanged(phone: String) {
        phoneChangeCalls.add(phone)
    }

    override fun onAddIdentifierCodeChanged(code: String) {
        codeChangeCalls.add(code)
    }

    override fun onAddIdentifierSendCode() {
        sendCodeCalls++
    }

    override fun onAddIdentifierSubmit() {
        submitCalls++
    }

    override fun onAddIdentifierDialogBack() {
        dialogBackCalls++
    }

    override fun onAddIdentifierDialogDismiss() {
        dialogDismissCalls++
    }

    override fun onIdentifierDeleted(identifierId: UserIdentifierId) {
        val current = _state.value as? SelfIdentifierListScreenState.Content ?: return
        val updated = current.paging.items.filterNot { it.id == identifierId }
        _state.value = current.copy(paging = current.paging.copy(items = updated))
    }

    override fun onLoadNextPage() {
        loadNextPageCalls++
    }

    override fun onBackClick() {
        backCalls++
    }
}
