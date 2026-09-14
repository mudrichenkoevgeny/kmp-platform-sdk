package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing

import android.app.Application
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runComposeUiTest
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.captureAppScreen
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
class PagingFooterScreenshotTest(
    private val stateName: String,
    private val state: PaginationState<Any>
) {

    @Test
    fun capture() = runComposeUiTest {
        setContent {
            ComponentTestHarness {
                MaterialTheme {
                    Surface {
                        PagingFooter(state = state, onRetry = {})
                    }
                }
            }
        }

        onRoot().captureAppScreen(
            testInstance = this@PagingFooterScreenshotTest,
            stateName = stateName
        )
    }

    companion object {
        @Suppress("Unused")
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun data(): Collection<Array<Any>> {
            return listOf(
                arrayOf(
                    "Loading",
                    PaginationState<Any>(items = listOf(object {}), isNextPageLoading = true)
                ),
                arrayOf(
                    "Error",
                    PaginationState<Any>(items = listOf(object {}), error = CommonError.Unknown(isRetryable = true))
                )
            )
        }
    }
}
