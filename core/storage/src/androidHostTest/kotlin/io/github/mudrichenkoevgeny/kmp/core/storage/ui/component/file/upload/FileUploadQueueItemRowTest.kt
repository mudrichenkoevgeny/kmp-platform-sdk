package io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.category.StorageCategory
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.upload.UploadQueueItem
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.upload.UploadQueueItemStatus
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.upload.toUploadQueueItemIdOrThrow
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
class FileUploadQueueItemRowTest {

    @Test
    fun displaysQueueItemInfoCorrectly() = runComposeUiTest {
        val queueItem = UploadQueueItem(
            id = "00000000-0000-0000-0000-000000000001".toUploadQueueItemIdOrThrow(),
            fileName = "photo.jpg",
            fileSizeBytes = 512000L,
            mimeType = "image/jpeg",
            category = StorageCategory.IMAGE,
            status = UploadQueueItemStatus.IDLE
        )

        setContent {
            ComponentTestHarness {
                FileUploadQueueItemRow(
                    item = queueItem,
                    isUploading = false,
                    onRemove = {}
                )
            }
        }

        onNodeWithTag(StorageFileUploadAreaTestTags.QUEUE_ITEM).assertIsDisplayed()
        onNodeWithText("photo.jpg").assertIsDisplayed()
    }

    @Test
    fun onRemoveClick_triggersCallback() = runComposeUiTest {
        val sampleId = "00000000-0000-0000-0000-000000000001".toUploadQueueItemIdOrThrow()
        val queueItem = UploadQueueItem(
            id = sampleId,
            fileName = "photo.jpg",
            fileSizeBytes = 512000L,
            mimeType = "image/jpeg",
            category = StorageCategory.IMAGE,
            status = UploadQueueItemStatus.IDLE
        )
        var removeCalls = 0

        setContent {
            ComponentTestHarness {
                FileUploadQueueItemRow(
                    item = queueItem,
                    isUploading = false,
                    onRemove = { removeCalls++ }
                )
            }
        }

        onNodeWithTag(StorageFileUploadAreaTestTags.REMOVE_ITEM_BUTTON).performClick()
        assertEquals(1, removeCalls)
    }
}
