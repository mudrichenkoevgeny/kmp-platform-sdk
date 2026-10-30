package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.settings

import android.app.Application
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.core.storage.mock.ui.screen.settings.EditStorageSettingsComponentMock
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK], application = Application::class)
class EditStorageSettingsScreenTest {

    @Test
    fun content_displaysFormFields() = runComposeUiTest {
        val component = EditStorageSettingsComponentMock()
        setContent {
            ComponentTestHarness {
                EditStorageSettingsScreen(component = component)
            }
        }
        onNodeWithTag(EditStorageSettingsTestTags.TITLE).assertIsDisplayed()
        onNodeWithTag(EditStorageSettingsTestTags.BACK_BUTTON).assertIsDisplayed()
        onNodeWithTag(EditStorageSettingsTestTags.RESET_BUTTON).assertIsDisplayed()
        onNodeWithTag(EditStorageSettingsTestTags.MIN_UPLOAD_SIZE_BYTES_INPUT).assertIsDisplayed()
        onNodeWithTag(EditStorageSettingsTestTags.MAX_UPLOAD_SIZE_BYTES_INPUT).assertIsDisplayed()
        onNodeWithTag(EditStorageSettingsTestTags.TOTAL_STORAGE_QUOTA_FOR_OPEN_USER_BYTES_INPUT).assertIsDisplayed()
        onNodeWithTag(EditStorageSettingsTestTags.TOTAL_STORAGE_QUOTA_FOR_MANAGEMENT_USER_BYTES_INPUT).assertIsDisplayed()
        onNodeWithTag(EditStorageSettingsTestTags.PRESIGNED_URL_EXPIRATION_SECONDS_INPUT).assertIsDisplayed()
        onNodeWithTag(EditStorageSettingsTestTags.MAX_PENDING_UPLOADS_FOR_OPEN_USER_INPUT).assertIsDisplayed()
        onNodeWithTag(EditStorageSettingsTestTags.MAX_PENDING_UPLOADS_FOR_MANAGEMENT_USER_INPUT).assertIsDisplayed()
        onNodeWithTag(EditStorageSettingsTestTags.IS_DEDUPLICATION_ENABLED_CHECKBOX).performScrollTo().assertIsDisplayed()
        onNodeWithTag(EditStorageSettingsTestTags.DELETED_FILES_CLEANUP_INTERVAL_SECONDS_INPUT).performScrollTo().assertIsDisplayed()
        onNodeWithTag(EditStorageSettingsTestTags.SAVE_BUTTON).performScrollTo().assertIsDisplayed()
    }

    @Test
    fun content_clickBack_invokesCallback() = runComposeUiTest {
        val component = EditStorageSettingsComponentMock()
        setContent {
            ComponentTestHarness {
                EditStorageSettingsScreen(component = component)
            }
        }
        onNodeWithTag(EditStorageSettingsTestTags.BACK_BUTTON).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.backCalls)
    }

    @Test
    fun content_clickSave_invokesCallback() = runComposeUiTest {
        val component = EditStorageSettingsComponentMock()
        setContent {
            ComponentTestHarness {
                EditStorageSettingsScreen(component = component)
            }
        }
        onNodeWithTag(EditStorageSettingsTestTags.SAVE_BUTTON).performScrollTo().performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.saveCalls)
    }

    private companion object {
        const val EXPECTED_SINGLE_CALLBACK = 1
    }
}
