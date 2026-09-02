package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.pendingdeletion

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PendingDeletionScreenStateTest {

    @Test
    fun defaultParameters_setsExpectedFlags() {
        val state = PendingDeletionScreenState()

        assertFalse(state.actionLoading)
        assertNull(state.actionError)
    }

    @Test
    fun customParameters_setsExpectedValues() {
        val error = CommonError.Unknown()
        val state = PendingDeletionScreenState(
            actionLoading = true,
            actionError = error
        )

        assertTrue(state.actionLoading)
        assertEquals(error, state.actionError)
    }
}
