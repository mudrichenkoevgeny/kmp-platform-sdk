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
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.MockAppErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

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
        onNodeWithText(MOCK_ERROR_MESSAGE).assertIsDisplayed()
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
        onNodeWithText(MOCK_ERROR_MESSAGE).assertIsDisplayed()
        onNodeWithText(RETRY_LABEL).assertIsDisplayed()
        onNodeWithText(RETRY_LABEL).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, retries)
    }

    private companion object {
        const val EXPECTED_SINGLE_CALLBACK = 1

        /** [MockAppErrorParser] always returns this string for any error. */
        const val MOCK_ERROR_MESSAGE = "Unknown Error"

        /** Mirrors default `values/strings.xml`. */
        const val RETRY_LABEL = "Retry"
    }
}

@Composable
private fun FullscreenErrorHarness(
    error: AppError,
    onRetry: () -> Unit
) {
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides MockAppErrorParser) {
            FullscreenError(error = error, onRetry = onRetry)
        }
    }
}
