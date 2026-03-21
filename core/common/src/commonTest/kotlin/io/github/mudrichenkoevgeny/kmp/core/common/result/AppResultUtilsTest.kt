package io.github.mudrichenkoevgeny.kmp.core.common.result

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class AppResultUtilsTest {

    @Test
    fun `onSuccess executes block for Success`() {
        val result: AppResult<Int> = AppResult.Success(1)

        var called = false
        result.onSuccess { value ->
            called = true
            assertEquals(1, value)
        }

        assertTrue(called)
    }

    @Test
    fun `onError executes block for Error`() {
        val error: AppError = CommonError.Unknown(isRetryable = false)
        val result: AppResult<Int> = AppResult.Error(error)

        var called = false
        result.onError { e ->
            called = true
            assertSame(e, error)
        }

        assertTrue(called)
    }

    @Test
    fun `flatMap transforms Success and propagates Error`() {
        val error: AppError = CommonError.Unknown(isRetryable = false)

        val successResult = AppResult.Success(1).flatMap { AppResult.Success(it + 1) }
        assertEquals(2, (successResult as AppResult.Success).data)

        val propagated = AppResult.Error(error).flatMap { AppResult.Success(0) }
        val propagatedError = (propagated as AppResult.Error).error
        assertSame(propagatedError, error)
    }

    @Test
    fun `mapSuccess maps Success and converts exceptions to ContractViolation`() {
        val exception = IllegalStateException("boom")

        val result = AppResult.Success(1).mapSuccess {
            throw exception
        }

        val error = (result as AppResult.Error).error
        val contractViolation = error as CommonError.ContractViolation
        assertSame(contractViolation.throwable, exception)
    }

    @Test
    fun `flatMapSuccess only transforms Success`() {
        val successResult = AppResult.Success(1).flatMapSuccess { AppResult.Success(it + 1) }
        assertEquals(2, (successResult as AppResult.Success).data)

        val error = CommonError.Unknown(isRetryable = false)
        val propagated = AppResult.Error(error).flatMapSuccess { AppResult.Success(0) }
        val propagatedError = (propagated as AppResult.Error).error
        assertSame(propagatedError, error)
    }
}

