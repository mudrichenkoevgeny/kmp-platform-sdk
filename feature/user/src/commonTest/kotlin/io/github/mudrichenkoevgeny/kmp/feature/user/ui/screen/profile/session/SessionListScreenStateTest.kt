package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.session.userSessionMock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class SessionListScreenStateTest {

    @Test
    fun loading_isObject() {
        assertIs<SessionListScreenState.Loading>(SessionListScreenState.Loading)
    }

    @Test
    fun content_holdsSessionsAndActions() {
        val sessions = listOf(userSessionMock())
        val paging = PaginationState(items = sessions)
        val error = CommonError.Unknown()
        val state = SessionListScreenState.Content(
            paging = paging,
            actionLoading = true,
            actionError = error
        )

        assertEquals(paging, state.paging)
        assertEquals(true, state.actionLoading)
        assertEquals(error, state.actionError)
    }

    @Test
    fun error_holdsAppError() {
        val error = CommonError.Unknown()
        val state = SessionListScreenState.Error(error)

        assertEquals(error, state.error)
    }
}
