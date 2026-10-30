package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.upload

import android.app.Application
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Surface
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runComposeUiTest
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.core.storage.mock.ui.component.file.upload.StorageFileUploadComponentMock
import io.github.mudrichenkoevgeny.kmp.core.storage.mock.ui.screen.upload.StorageFileUploadScreenComponentMock
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload.StorageFileUploadState
import io.github.mudrichenkoevgeny.kmp.core.testing.ui.test.captureAppScreen
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.ui.component.fileupload.StorageFileUploadUiConfig
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
class StorageFileUploadScreenScreenshotTest(
    private val stateName: String,
    private val enabled: Boolean
) {

    @Test
    fun capture() = runComposeUiTest {
        val mockScreenComponent = StorageFileUploadScreenComponentMock(
            uploadComponent = StorageFileUploadComponentMock(
                initialState = StorageFileUploadState(config = StorageFileUploadUiConfig())
            )
        )

        setContent {
            ComponentTestHarness {
                Surface {
                    Box {
                        StorageFileUploadScreen(
                            component = mockScreenComponent,
                            enabled = enabled
                        )
                    }
                }
            }
        }

        onRoot().captureAppScreen(
            testInstance = this@StorageFileUploadScreenScreenshotTest,
            stateName = stateName
        )
    }

    companion object {
        @Suppress("Unused")
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun data(): Collection<Array<Any>> {
            return listOf(
                arrayOf("Default", true),
                arrayOf("Disabled", false)
            )
        }
    }
}
