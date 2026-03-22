package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
class FullscreenLoadingTest {

    @Test
    fun zeroDelay_showsIndeterminateProgressImmediately() = runComposeUiTest {
        setContent {
            MaterialTheme {
                FullscreenLoading(delayMillis = 0L)
            }
        }
        onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertIsDisplayed()
    }

    @Test
    fun defaultDelay_showsIndeterminateProgressAfterConfiguredDelay() = runComposeUiTest {
        setContent {
            MaterialTheme {
                FullscreenLoading()
            }
        }
        mainClock.autoAdvance = false
        mainClock.advanceTimeBy(FullscreenLoadingConfig.DELAY_MILLIS + LOADING_EXTRA_DELAY_MS)
        onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertExists()
    }

    private companion object {
        const val LOADING_EXTRA_DELAY_MS = 50L
    }
}
