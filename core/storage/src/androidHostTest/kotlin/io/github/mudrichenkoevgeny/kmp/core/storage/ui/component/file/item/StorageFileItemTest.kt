package io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.item

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.core.storage.localization.LocalStorageStrings
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.status.StorageFileStatus
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.storage.domain.model.file.createTestStorageFileSummary
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
class StorageFileItemTest {

    @Test
    fun displaysFileSummaryDataCorrectly() = runComposeUiTest {
        val testFile = createTestStorageFileSummary()

        setContent {
            ComponentTestHarness {
                StorageFileItem(
                    file = testFile,
                    status = StorageFileStatus.READY
                )
            }
        }

        onNodeWithTag(StorageFileItemTestTags.ITEM_PREFIX + testFile.id.asHexDashString()).assertIsDisplayed()
        onNodeWithText(testFile.fileName).assertIsDisplayed()
        onNodeWithText(testFile.mimeType).assertIsDisplayed()
    }

    @Test
    fun onFileClick_triggersCallback() = runComposeUiTest {
        val testFile = createTestStorageFileSummary()
        var clickCalls = 0

        setContent {
            ComponentTestHarness {
                StorageFileItem(
                    file = testFile,
                    onFileClick = { clickCalls++ }
                )
            }
        }

        onNodeWithTag(StorageFileItemTestTags.ITEM_PREFIX + testFile.id.asHexDashString()).performClick()
        assertEquals(1, clickCalls)
    }

    @Test
    fun onDeleteClick_triggersCallback() = runComposeUiTest {
        val testFile = createTestStorageFileSummary()
        var deleteCalls = 0
        var deleteButtonText = ""

        setContent {
            ComponentTestHarness {
                deleteButtonText = LocalStorageStrings.current.ui.deleteFile
                StorageFileItem(
                    file = testFile,
                    status = StorageFileStatus.READY,
                    onDeleteClick = { deleteCalls++ }
                )
            }
        }

        onNodeWithText(deleteButtonText).performClick()
        assertEquals(1, deleteCalls)
    }
}
