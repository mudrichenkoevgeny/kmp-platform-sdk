package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.list

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.identifier.createTestUserIdentifierSummary
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
        val identifiers = listOf(createTestUserIdentifierSummary())
        val currentIdentifierId = UserIdentifierId.generate()
        val paging = PaginationState(items = identifiers)
        val error = CommonError.Unknown()
        val state = IdentifierListScreenState.Content(
            paging = paging,
            currentIdentifierId = currentIdentifierId,
            actionLoading = true,
            actionError = error
        )

        assertEquals(paging, state.paging)
        assertEquals(currentIdentifierId, state.currentIdentifierId)
        assertEquals(true, state.actionLoading)
        assertEquals(error, state.actionError)
    }

    @Test
    fun error_holdsAppError() {
        val error = CommonError.Unknown()
        val state = IdentifierListScreenState.Error(error)

        assertEquals(error, state.error)
    }
}
