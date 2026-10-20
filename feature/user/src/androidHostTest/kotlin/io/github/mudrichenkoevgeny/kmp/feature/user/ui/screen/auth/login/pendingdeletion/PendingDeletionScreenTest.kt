package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.pendingdeletion

import android.app.Application
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.login.pendingdeletion.PendingDeletionComponentMock
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK], application = Application::class)
class PendingDeletionScreenTest {

    @Test
    fun displaysElements_andInvokesActions() = runComposeUiTest {
        val component = PendingDeletionComponentMock()
        setContent {
            ComponentTestHarness {
                PendingDeletionScreen(component)
            }
        }

        onNodeWithTag(PendingDeletionTestTags.TITLE).assertIsDisplayed()
        onNodeWithTag(PendingDeletionTestTags.CARD_TITLE).assertIsDisplayed()
        onNodeWithTag(PendingDeletionTestTags.CARD_DESC).assertIsDisplayed()

        onNodeWithTag(PendingDeletionTestTags.RESTORE_BUTTON).assertIsDisplayed().performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.restoreAccountCalls)

        onNodeWithTag(PendingDeletionTestTags.SIGN_OUT_BUTTON).assertIsDisplayed().performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.signOutCalls)
    }

    @Test
    fun showsActionError() = runComposeUiTest {
        val component = PendingDeletionComponentMock(
            initialState = PendingDeletionScreenState(actionError = CommonError.Unknown())
        )
        setContent {
            ComponentTestHarness {
                PendingDeletionScreen(component)
            }
        }
        onNodeWithTag(PendingDeletionTestTags.ACTION_ERROR_TEXT).assertIsDisplayed()
    }

    private companion object {
        const val EXPECTED_SINGLE_CALLBACK = 1
    }
}
