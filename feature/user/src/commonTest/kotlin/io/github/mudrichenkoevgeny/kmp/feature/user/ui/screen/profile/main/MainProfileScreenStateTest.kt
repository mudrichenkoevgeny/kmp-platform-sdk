package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.main

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.user.userDetailsMock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@InternalApi
class MainProfileScreenStateTest {

    @Test
    fun content_defaultParameters_setsExpectedFlags() {
        val userDetails = userDetailsMock()
        val state = MainProfileScreenState.Content(user = userDetails)

        assertEquals(userDetails, state.user)
        assertTrue(state.isAccountDeletionAvailable)
        assertFalse(state.actionLoading)
        assertNull(state.actionError)
    }

    @Test
    fun content_customParameters_setsExpectedValues() {
        val userDetails = userDetailsMock()
        val error = CommonError.Unknown()
        val state = MainProfileScreenState.Content(
            user = userDetails,
            isAccountDeletionAvailable = DELETION_NOT_AVAILABLE,
            actionLoading = ACTION_LOADING,
            actionError = error
        )

        assertEquals(userDetails, state.user)
        assertFalse(state.isAccountDeletionAvailable)
        assertTrue(state.actionLoading)
        assertEquals(error, state.actionError)
    }

    @Test
    fun error_holdsProvidedAppError() {
        val error = CommonError.Unknown()
        val state = MainProfileScreenState.Error(error = error)

        assertEquals(error, state.error)
    }

    private companion object {
        const val DELETION_NOT_AVAILABLE = false
        const val ACTION_LOADING = true
    }
}