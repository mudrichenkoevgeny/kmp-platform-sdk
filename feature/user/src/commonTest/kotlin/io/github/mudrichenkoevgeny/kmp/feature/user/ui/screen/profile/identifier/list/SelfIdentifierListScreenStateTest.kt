package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.list

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.identifier.userIdentifierMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
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
        val currentIdentifierId = UserIdentifierId.generate()
        val paging = PaginationState(items = identifiers)
        val error = CommonError.Unknown()
        val state = SelfIdentifierListScreenState.Content(
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
        val state = SelfIdentifierListScreenState.Error(error)

        assertEquals(error, state.error)
    }
}
