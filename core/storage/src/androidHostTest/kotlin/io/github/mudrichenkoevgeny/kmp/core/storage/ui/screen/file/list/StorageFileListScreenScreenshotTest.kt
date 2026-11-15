package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list

import android.app.Application
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.v2.runComposeUiTest
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.core.storage.mock.ui.screen.file.list.StorageFileListComponentMock
import io.github.mudrichenkoevgeny.kmp.core.testing.ui.test.captureAppScreen
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.toFileIdOrThrow
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.storage.domain.model.file.createTestStorageFileSummary
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
class StorageFileListScreenScreenshotTest(
    private val stateName: String,
    private val state: StorageFileListScreenState
) {

    @Test
    fun capture() = runComposeUiTest {
        val mockComponent = StorageFileListComponentMock(initialState = state)

        setContent {
            ComponentTestHarness {
                Surface {
                    Box(modifier = Modifier.padding(CoreTheme.dimens.paddingLarge)) {
                        StorageFileListScreen(
                            component = mockComponent
                        )
                    }
                }
            }
        }

        onRoot().captureAppScreen(
            testInstance = this@StorageFileListScreenScreenshotTest,
            stateName = stateName
        )
    }

    companion object {
        @Suppress("Unused")
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun data(): Collection<Array<Any>> {
            val item1 = createTestStorageFileSummary(id = "00000000-0000-4000-8000-000000000001".toFileIdOrThrow())
            val item2 = createTestStorageFileSummary(id = "00000000-0000-4000-8000-000000000002".toFileIdOrThrow())
            val testItems = listOf(item1, item2)
            val contentState = StorageFileListScreenState.Content(
                paging = PaginationState(items = testItems)
            )
            return listOf(
                arrayOf("Loading", StorageFileListScreenState.Loading),
                arrayOf("Content", contentState)
            )
        }
    }
}
