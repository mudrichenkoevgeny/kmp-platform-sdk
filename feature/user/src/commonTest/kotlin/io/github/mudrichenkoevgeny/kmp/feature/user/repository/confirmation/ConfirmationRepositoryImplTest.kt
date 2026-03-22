package io.github.mudrichenkoevgeny.kmp.feature.user.repository.confirmation

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.ConfirmationType
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.HasRetryDelay
import io.github.mudrichenkoevgeny.kmp.core.common.testsupport.MutableEpochTestClock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ConfirmationRepositoryImplTest {

    private data class PayloadWithDelay(override val retryAfterSeconds: Int) : HasRetryDelay

    @Test
    fun getRemainingDelay_returnsZero_whenNeverBlocked() {
        val clock = MutableEpochTestClock(BASE_MS)
        val repo = ConfirmationRepositoryImpl(clock)

        assertEquals(
            0,
            repo.getRemainingDelay(ConfirmationType.REGISTRATION_EMAIL, IDENTIFIER_A)
        )
    }

    @Test
    fun executeWithTimer_runsActionAndReturnsSuccess_whenNoCooldown() = runTest {
        val clock = MutableEpochTestClock(BASE_MS)
        val repo = ConfirmationRepositoryImpl(clock)
        var invoked = false

        val result = repo.executeWithTimer(ConfirmationType.LOGIN_PHONE, IDENTIFIER_A) {
            invoked = true
            AppResult.Success("ok")
        }

        assertTrue(invoked)
        assertEquals("ok", assertIs<AppResult.Success<String>>(result).data)
    }

    @Test
    fun executeWithTimer_recordsCooldown_whenSuccessImplementsHasRetryDelay() = runTest {
        val clock = MutableEpochTestClock(BASE_MS)
        val repo = ConfirmationRepositoryImpl(clock)

        val first = repo.executeWithTimer(ConfirmationType.PASSWORD_RESET_EMAIL, IDENTIFIER_A) {
            AppResult.Success(PayloadWithDelay(retryAfterSeconds = COOLDOWN_SEC))
        }
        assertIs<AppResult.Success<*>>(first)

        assertEquals(COOLDOWN_SEC, repo.getRemainingDelay(ConfirmationType.PASSWORD_RESET_EMAIL, IDENTIFIER_A))
    }

    @Test
    fun getRemainingDelay_usesWholeSecondsTruncatedTowardZero() = runTest {
        val clock = MutableEpochTestClock(BASE_MS)
        val repo = ConfirmationRepositoryImpl(clock)

        repo.executeWithTimer(ConfirmationType.REGISTRATION_EMAIL, IDENTIFIER_A) {
            AppResult.Success(PayloadWithDelay(retryAfterSeconds = COOLDOWN_SEC))
        }

        clock.advanceMilliseconds(2_500L)

        assertEquals(7, repo.getRemainingDelay(ConfirmationType.REGISTRATION_EMAIL, IDENTIFIER_A))
    }

    @Test
    fun executeWithTimer_returnsTooManyConfirmation_whenStillBlocked() = runTest {
        val clock = MutableEpochTestClock(BASE_MS)
        val repo = ConfirmationRepositoryImpl(clock)

        repo.executeWithTimer(ConfirmationType.ADD_EMAIL, IDENTIFIER_A) {
            AppResult.Success(PayloadWithDelay(retryAfterSeconds = COOLDOWN_SEC))
        }

        var secondInvocation = false
        val blocked = repo.executeWithTimer(ConfirmationType.ADD_EMAIL, IDENTIFIER_A) {
            secondInvocation = true
            AppResult.Success(Unit)
        }

        assertFalse(secondInvocation)
        val err = assertIs<AppResult.Error>(blocked).error
        val tooMany = assertIs<UserError.TooManyConfirmationRequests>(err)
        assertEquals(COOLDOWN_SEC, tooMany.retryAfterSeconds)
    }

    @Test
    fun executeWithTimer_doesNotRecordCooldown_whenActionReturnsError() = runTest {
        val clock = MutableEpochTestClock(BASE_MS)
        val repo = ConfirmationRepositoryImpl(clock)

        val failure = repo.executeWithTimer(ConfirmationType.LOGIN_PHONE, IDENTIFIER_A) {
            AppResult.Error(UserError.TooManyConfirmationRequests(retryAfterSeconds = 1))
        }
        assertIs<AppResult.Error>(failure)

        assertEquals(0, repo.getRemainingDelay(ConfirmationType.LOGIN_PHONE, IDENTIFIER_A))

        var ran = false
        val second = repo.executeWithTimer(ConfirmationType.LOGIN_PHONE, IDENTIFIER_A) {
            ran = true
            AppResult.Success(Unit)
        }
        assertTrue(ran)
        assertIs<AppResult.Success<Unit>>(second)
    }

    @Test
    fun executeWithTimer_doesNotRecordCooldown_whenSuccessWithoutHasRetryDelay() = runTest {
        val clock = MutableEpochTestClock(BASE_MS)
        val repo = ConfirmationRepositoryImpl(clock)

        val result = repo.executeWithTimer(ConfirmationType.ADD_PHONE, IDENTIFIER_A) {
            AppResult.Success(42)
        }
        assertIs<AppResult.Success<Int>>(result)

        assertEquals(0, repo.getRemainingDelay(ConfirmationType.ADD_PHONE, IDENTIFIER_A))
    }

    @Test
    fun cooldownIsScopedPerConfirmationTypeAndIdentifier() = runTest {
        val clock = MutableEpochTestClock(BASE_MS)
        val repo = ConfirmationRepositoryImpl(clock)

        repo.executeWithTimer(ConfirmationType.REGISTRATION_EMAIL, IDENTIFIER_A) {
            AppResult.Success(PayloadWithDelay(retryAfterSeconds = COOLDOWN_SEC))
        }

        var otherRan = false
        val other = repo.executeWithTimer(ConfirmationType.REGISTRATION_EMAIL, IDENTIFIER_B) {
            otherRan = true
            AppResult.Success(Unit)
        }
        assertTrue(otherRan)
        assertIs<AppResult.Success<Unit>>(other)

        var otherTypeRan = false
        val otherType = repo.executeWithTimer(ConfirmationType.PASSWORD_RESET_EMAIL, IDENTIFIER_A) {
            otherTypeRan = true
            AppResult.Success(Unit)
        }
        assertTrue(otherTypeRan)
        assertIs<AppResult.Success<Unit>>(otherType)
    }

    @Test
    fun executeWithTimer_allowsRetry_afterCooldownElapsed() = runTest {
        val clock = MutableEpochTestClock(BASE_MS)
        val repo = ConfirmationRepositoryImpl(clock)

        repo.executeWithTimer(ConfirmationType.LOGIN_PHONE, IDENTIFIER_A) {
            AppResult.Success(PayloadWithDelay(retryAfterSeconds = 2))
        }
        assertEquals(2, repo.getRemainingDelay(ConfirmationType.LOGIN_PHONE, IDENTIFIER_A))

        clock.advanceMilliseconds(2_500L)
        assertEquals(0, repo.getRemainingDelay(ConfirmationType.LOGIN_PHONE, IDENTIFIER_A))

        var ranAgain = false
        val again = repo.executeWithTimer(ConfirmationType.LOGIN_PHONE, IDENTIFIER_A) {
            ranAgain = true
            AppResult.Success("done")
        }
        assertTrue(ranAgain)
        assertEquals("done", assertIs<AppResult.Success<String>>(again).data)
    }

    private companion object {
        private const val BASE_MS = 1_000_000L
        private const val COOLDOWN_SEC = 10
        private const val IDENTIFIER_A = "user-a@example.com"
        private const val IDENTIFIER_B = "user-b@example.com"
    }
}
