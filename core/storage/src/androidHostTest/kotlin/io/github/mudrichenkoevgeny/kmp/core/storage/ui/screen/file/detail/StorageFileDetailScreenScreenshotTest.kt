package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.detail

import android.app.Application
import androidx.compose.material3.Surface
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runComposeUiTest
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.core.storage.mock.ui.screen.file.detail.StorageFileDetailComponentMock
import io.github.mudrichenkoevgeny.kmp.core.testing.ui.test.captureAppScreen
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
class StorageFileDetailScreenScreenshotTest(
    private val stateName: String,
    private val state: StorageFileDetailScreenState
) {

    @Test
    fun capture() = runComposeUiTest {
        val mockComponent = StorageFileDetailComponentMock(initialState = state)

        setContent {
            ComponentTestHarness {
                Surface {
                    StorageFileDetailScreen(component = mockComponent)
                }
            }
        }

        onRoot().captureAppScreen(
            testInstance = this@StorageFileDetailScreenScreenshotTest,
            stateName = stateName
        )
    }

    companion object {
        @Suppress("Unused")
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun data(): Collection<Array<Any>> {
            return listOf(
                arrayOf("Loading", StorageFileDetailScreenState.Loading),
                arrayOf("Error", StorageFileDetailScreenState.Error(error = CommonError.Unknown())),
                arrayOf("Content", StorageFileDetailComponentMock().state.value)
            )
        }
    }
}
