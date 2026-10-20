package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.session.createTestUserSessionSummary
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class SelfSessionListScreenStateTest {

    @Test
    fun loading_isObject() {
        assertIs<SelfSessionListScreenState.Loading>(SelfSessionListScreenState.Loading)
    }

    @Test
    fun content_holdsSessionsAndActions() {
        val sessions = listOf(createTestUserSessionSummary())
        val paging = PaginationState(items = sessions)
        val error = CommonError.Unknown()
        val state = SelfSessionListScreenState.Content(
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
        val state = SelfSessionListScreenState.Error(error)

        assertEquals(error, state.error)
    }
}