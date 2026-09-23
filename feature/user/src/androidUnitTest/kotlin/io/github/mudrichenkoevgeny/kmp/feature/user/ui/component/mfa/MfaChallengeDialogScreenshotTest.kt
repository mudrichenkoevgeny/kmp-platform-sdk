package io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.mfa

import android.app.Application
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.window.DialogProperties
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.DialogPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.captureAppScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.network.httpclient.mfa.MfaChallengeRequest
import kotlinx.coroutines.CompletableDeferred
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import kotlin.test.Test

@InternalApi
@RunWith(ParameterizedRobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(
    sdk = [ROBOLECTRIC_SDK],
    application = Application::class,
    qualifiers = RobolectricDeviceQualifiers.Pixel5
)
class MfaChallengeDialogScreenshotTest(
    private val stateName: String
) {

    @Test
    fun capture() = runComposeUiTest {
        val completion = CompletableDeferred<String?>()
        val request = MfaChallengeRequest("test_token", completion)

        setContent {
            ComponentTestHarness {
                DialogPreviewContainer {
                    MfaChallengeDialog(
                        request = request,
                        onConfirm = {},
                        onCancel = {}
                    )
                }
            }
        }

        mainClock.autoAdvance = false
        mainClock.advanceTimeBy(300)

        onNode(isDialog()).captureAppScreen(
            testInstance = this@MfaChallengeDialogScreenshotTest,
            stateName = stateName
        )

        completion.cancel()
    }

    companion object {
        @Suppress("Unused")
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun data(): Collection<Array<Any>> = listOf(
            arrayOf("Default")
        )
    }
}