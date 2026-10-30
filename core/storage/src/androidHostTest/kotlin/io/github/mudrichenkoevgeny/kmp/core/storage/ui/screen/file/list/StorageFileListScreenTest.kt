package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list

import android.app.Application
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.core.storage.mock.ui.screen.file.list.StorageFileListComponentMock
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.storage.domain.model.file.createTestStorageFileSummary
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK], application = Application::class)
class StorageFileListScreenTest {

    @Test
    fun content_displaysHeaderAndTitle() = runComposeUiTest {
        val testItems = listOf(createTestStorageFileSummary())
        val contentState = StorageFileListScreenState.Content(
            paging = PaginationState(items = testItems)
        )
        val component = StorageFileListComponentMock(initialState = contentState)

        setContent {
            ComponentTestHarness {
                StorageFileListScreen(component = component)
            }
        }

        onNodeWithTag(StorageFileListTestTags.TITLE).assertIsDisplayed()
        onNodeWithTag(StorageFileListTestTags.BACK_BUTTON).assertIsDisplayed()
        onNodeWithTag(StorageFileListTestTags.FILE_LIST).assertIsDisplayed()
    }
}
