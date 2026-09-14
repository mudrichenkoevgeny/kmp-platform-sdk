package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading

import android.app.Application
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runComposeUiTest
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.captureAppScreen
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import kotlin.test.Test

@InternalApi
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(
    sdk = [ROBOLECTRIC_SDK],
    application = Application::class,
    qualifiers = RobolectricDeviceQualifiers.Pixel5
)
class FullscreenOverlayLoadingScreenshotTest {

    @Test
    fun capture() = runComposeUiTest {
        setContent {
            ComponentTestHarness {
                MaterialTheme {
                    Box(modifier = Modifier.fillMaxSize()) {
                        FullscreenOverlayLoading()
                    }
                }
            }
        }

        onRoot().captureAppScreen(
            testInstance = this@FullscreenOverlayLoadingScreenshotTest,
            stateName = "Default"
        )
    }
}
