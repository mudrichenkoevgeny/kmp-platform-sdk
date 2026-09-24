package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing

import android.app.Application
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runComposeUiTest
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
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
class ListingHeaderBarScreenshotTest(
    private val stateName: String,
    private val state: PaginationState<String>
) {

    @Test
    fun capture() = runComposeUiTest {
        setContent {
            ComponentTestHarness {
                MaterialTheme {
                    Surface {
                        ListingHeaderBar(
                            state = state,
                            lazyListState = rememberLazyListState()
                        )
                    }
                }
            }
        }

        onRoot().captureAppScreen(
            testInstance = this@ListingHeaderBarScreenshotTest,
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
                    "MultiPage",
                    PaginationState(
                        items = listOf("item1", "item2"),
                        pageNumber = 1,
                        totalPages = 5,
                        totalCount = 100
                    )
                ),
                arrayOf(
                    "SinglePage",
                    PaginationState(
                        items = listOf("item1"),
                        pageNumber = 1,
                        totalPages = 1,
                        totalCount = 1
                    )
                )
            )
        }
    }
}
