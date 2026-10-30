package io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload

import android.app.Application
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runComposeUiTest
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.core.testing.ui.test.captureAppScreen
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.category.StorageCategory
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.upload.UploadQueueItem
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.upload.UploadQueueItemStatus
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.upload.toUploadQueueItemIdOrThrow
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
class FileUploadQueueItemRowScreenshotTest(
    private val stateName: String,
    private val item: UploadQueueItem,
    private val isUploading: Boolean
) {

    @Test
    fun capture() = runComposeUiTest {
        setContent {
            ComponentTestHarness {
                Surface {
                    Box(modifier = Modifier.padding(CoreTheme.dimens.paddingLarge)) {
                        FileUploadQueueItemRow(
                            item = item,
                            isUploading = isUploading,
                            onRemove = {}
                        )
                    }
                }
            }
        }

        onRoot().captureAppScreen(
            testInstance = this@FileUploadQueueItemRowScreenshotTest,
            stateName = stateName
        )
    }

    companion object {
        @Suppress("Unused")
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun data(): Collection<Array<Any?>> {
            val sampleId = "00000000-0000-0000-0000-000000000001".toUploadQueueItemIdOrThrow()
            return listOf(
                arrayOf(
                    "Idle",
                    UploadQueueItem(
                        id = sampleId,
                        fileName = "document.pdf",
                        fileSizeBytes = 2048576L,
                        mimeType = "application/pdf",
                        category = StorageCategory.DOCUMENT,
                        status = UploadQueueItemStatus.IDLE
                    ),
                    false
                ),
                arrayOf(
                    "Uploading",
                    UploadQueueItem(
                        id = sampleId,
                        fileName = "photo.jpg",
                        fileSizeBytes = 512000L,
                        mimeType = "image/jpeg",
                        category = StorageCategory.IMAGE,
                        status = UploadQueueItemStatus.UPLOADING,
                        progressPercent = 60
                    ),
                    true
                ),
                arrayOf(
                    "Completed",
                    UploadQueueItem(
                        id = sampleId,
                        fileName = "archive.zip",
                        fileSizeBytes = 10485760L,
                        mimeType = "application/zip",
                        category = StorageCategory.OTHER,
                        status = UploadQueueItemStatus.COMPLETED
                    ),
                    false
                )
            )
        }
    }
}
