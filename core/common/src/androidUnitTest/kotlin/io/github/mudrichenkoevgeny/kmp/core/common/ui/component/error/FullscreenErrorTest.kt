package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.error

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
class FullscreenErrorTest {

    @Test
    fun nonRetryable_showsLocalizedMessageWithoutRetry() = runComposeUiTest {
        setContent {
            FullscreenErrorHarness(
                error = CommonError.Unknown(isRetryable = false),
                onRetry = {}
            )
        }
        onNodeWithText(ERROR_MESSAGE_MOCK).assertIsDisplayed()
        assertFailsWith<AssertionError> {
            onNodeWithText(RETRY_LABEL).assertIsDisplayed()
        }
    }

    @Test
    fun retryable_showsMessageAndRetry_invokesOnRetry() = runComposeUiTest {
        var retries = 0
        setContent {
            FullscreenErrorHarness(
                error = CommonError.Unknown(isRetryable = true),
                onRetry = { retries++ }
            )
        }
        onNodeWithText(ERROR_MESSAGE_MOCK).assertIsDisplayed()
        onNodeWithText(RETRY_LABEL).assertIsDisplayed()
        onNodeWithText(RETRY_LABEL).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, retries)
    }

    private companion object {
        const val EXPECTED_SINGLE_CALLBACK = 1

        /** [AppErrorParserMock] always returns this string for any error. */
        const val ERROR_MESSAGE_MOCK = "Unknown Error"

        /** Mirrors default `values/strings.xml`. */
        const val RETRY_LABEL = "Retry"
    }
}

@InternalApi
@Composable
private fun FullscreenErrorHarness(
    error: AppError,
    onRetry: () -> Unit
) {
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
            FullscreenError(error = error, onRetry = onRetry)
        }
    }
}
