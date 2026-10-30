package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.upload

import android.app.Application
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.core.storage.mock.ui.screen.upload.StorageFileUploadScreenComponentMock
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload.StorageFileUploadAreaTestTags
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK], application = Application::class)
class StorageFileUploadScreenTest {

    @Test
    fun content_displaysTitleAndBackButtonAndDropzone() = runComposeUiTest {
        val component = StorageFileUploadScreenComponentMock()
        setContent {
            ComponentTestHarness {
                StorageFileUploadScreen(component = component)
            }
        }
        onNodeWithTag(StorageFileUploadTestTags.TITLE).assertIsDisplayed()
        onNodeWithTag(StorageFileUploadTestTags.BACK_BUTTON).assertIsDisplayed()
        onNodeWithTag(StorageFileUploadAreaTestTags.DROPZONE).assertIsDisplayed()
    }

    @Test
    fun content_clickBack_invokesCallback() = runComposeUiTest {
        val component = StorageFileUploadScreenComponentMock()
        setContent {
            ComponentTestHarness {
                StorageFileUploadScreen(component = component)
            }
        }
        onNodeWithTag(StorageFileUploadTestTags.BACK_BUTTON).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.backCalls)
    }

    @Test
    fun content_clickDropzone_invokesSelectFilesCallback() = runComposeUiTest {
        val component = StorageFileUploadScreenComponentMock()
        setContent {
            ComponentTestHarness {
                StorageFileUploadScreen(component = component)
            }
        }
        onNodeWithTag(StorageFileUploadAreaTestTags.DROPZONE).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.selectFilesCalls)
    }

    private companion object {
        const val EXPECTED_SINGLE_CALLBACK = 1
    }
}
