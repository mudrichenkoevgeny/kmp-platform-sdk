package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.identifier.userIdentifierMock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class IdentifierListScreenStateTest {

    @Test
    fun loading_isObject() {
        assertIs<IdentifierListScreenState.Loading>(IdentifierListScreenState.Loading)
    }

    @Test
    fun content_holdsIdentifiersAndStates() {
        val identifiers = listOf(userIdentifierMock())
        val paging = PaginationState(items = identifiers)
        val error = CommonError.Unknown()
        val state = IdentifierListScreenState.Content(
            paging = paging,
            actionLoading = true,
            actionError = error,
            addEmailState = IdentifierListScreenState.AddIdentifierState.EnteringCode(value = "test@test.com"),
            addPhoneState = IdentifierListScreenState.AddIdentifierState.Idle
        )

        assertEquals(paging, state.paging)
        assertEquals(true, state.actionLoading)
        assertEquals(error, state.actionError)
        assertIs<IdentifierListScreenState.AddIdentifierState.EnteringCode>(state.addEmailState)
        assertIs<IdentifierListScreenState.AddIdentifierState.Idle>(state.addPhoneState)
    }

    @Test
    fun error_holdsAppError() {
        val error = CommonError.Unknown()
        val state = IdentifierListScreenState.Error(error)

        assertEquals(error, state.error)
    }

    @Test
    fun addIdentifierState_idle_isObject() {
        assertIs<IdentifierListScreenState.AddIdentifierState.Idle>(IdentifierListScreenState.AddIdentifierState.Idle)
    }

    @Test
    fun addIdentifierState_enteringCode_holdsValue() {
        val value = "test@test.com"
        val code = "123456"
        val state = IdentifierListScreenState.AddIdentifierState.EnteringCode(value = value, code = code)

        assertEquals(value, state.value)
        assertEquals(code, state.code)
    }
}
