package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.detail

import android.app.Application
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.core.storage.mock.ui.screen.file.detail.StorageFileDetailComponentMock
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK], application = Application::class)
class StorageFileDetailScreenTest {

    @Test
    fun content_displaysTitleAndBackButtonAndButtons() = runComposeUiTest {
        val component = StorageFileDetailComponentMock()
        setContent {
            ComponentTestHarness {
                StorageFileDetailScreen(component = component)
            }
        }
        onNodeWithTag(StorageFileDetailTestTags.TITLE).assertIsDisplayed()
        onNodeWithTag(StorageFileDetailTestTags.BACK_BUTTON).assertIsDisplayed()
        onNodeWithTag(StorageFileDetailTestTags.COPY_URL_BUTTON).assertIsDisplayed()
        onNodeWithTag(StorageFileDetailTestTags.DELETE_BUTTON).assertIsDisplayed()
    }

    @Test
    fun content_clickBack_invokesCallback() = runComposeUiTest {
        val component = StorageFileDetailComponentMock()
        setContent {
            ComponentTestHarness {
                StorageFileDetailScreen(component = component)
            }
        }
        onNodeWithTag(StorageFileDetailTestTags.BACK_BUTTON).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.backCalls)
    }

    @Test
    fun content_clickDelete_invokesCallback() = runComposeUiTest {
        val component = StorageFileDetailComponentMock()
        setContent {
            ComponentTestHarness {
                StorageFileDetailScreen(component = component)
            }
        }
        onNodeWithTag(StorageFileDetailTestTags.DELETE_BUTTON).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.deleteCalls)
    }

    private companion object {
        const val EXPECTED_SINGLE_CALLBACK = 1
    }
}
