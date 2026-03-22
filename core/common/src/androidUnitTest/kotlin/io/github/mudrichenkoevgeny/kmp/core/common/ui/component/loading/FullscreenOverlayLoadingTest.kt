package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.dp
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
class FullscreenOverlayLoadingTest {

    @Test
    fun showsIndeterminateProgressCenteredInParent() = runComposeUiTest {
        setContent {
            MaterialTheme {
                Box(modifier = Modifier.size(PARENT_SIZE_DP.dp)) {
                    FullscreenOverlayLoading()
                }
            }
        }
        onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertIsDisplayed()
    }

    private companion object {
        const val PARENT_SIZE_DP = 240
    }
}
