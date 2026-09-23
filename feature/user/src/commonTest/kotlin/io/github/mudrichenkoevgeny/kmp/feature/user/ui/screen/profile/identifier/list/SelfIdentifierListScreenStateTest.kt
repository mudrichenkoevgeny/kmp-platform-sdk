package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.list

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.identifier.userIdentifierMock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class SelfIdentifierListScreenStateTest {

    @Test
    fun loading_isObject() {
        assertIs<SelfIdentifierListScreenState.Loading>(SelfIdentifierListScreenState.Loading)
    }

    @Test
    fun content_holdsIdentifiersAndStates() {
        val identifiers = listOf(userIdentifierMock())
        val paging = PaginationState(items = identifiers)
        val error = CommonError.Unknown()
        val state = SelfIdentifierListScreenState.Content(
            paging = paging,
            actionLoading = true,
            actionError = error,
            addEmailState = SelfIdentifierListScreenState.AddIdentifierState.EnteringCode(value = "test@test.com"),
            addPhoneState = SelfIdentifierListScreenState.AddIdentifierState.Idle
        )

        assertEquals(paging, state.paging)
        assertEquals(true, state.actionLoading)
        assertEquals(error, state.actionError)
        assertIs<SelfIdentifierListScreenState.AddIdentifierState.EnteringCode>(state.addEmailState)
        assertIs<SelfIdentifierListScreenState.AddIdentifierState.Idle>(state.addPhoneState)
    }

    @Test
    fun error_holdsAppError() {
        val error = CommonError.Unknown()
        val state = SelfIdentifierListScreenState.Error(error)

        assertEquals(error, state.error)
    }

    @Test
    fun addIdentifierState_idle_isObject() {
        assertIs<SelfIdentifierListScreenState.AddIdentifierState.Idle>(SelfIdentifierListScreenState.AddIdentifierState.Idle)
    }

    @Test
    fun addIdentifierState_enteringCode_holdsValue() {
        val value = "test@test.com"
        val code = "123456"
        val state = SelfIdentifierListScreenState.AddIdentifierState.EnteringCode(value = value, code = code)

        assertEquals(value, state.value)
        assertEquals(code, state.code)
    }
}
