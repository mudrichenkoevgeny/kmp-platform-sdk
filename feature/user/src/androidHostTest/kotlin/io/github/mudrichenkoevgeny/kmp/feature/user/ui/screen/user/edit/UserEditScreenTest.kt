package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.edit

import android.app.Application
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.user.edit.UserEditComponentMock
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.user.createTestUserPrivate
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK], application = Application::class)
class UserEditScreenTest {

    @Test
    fun content_displaysElements_andInvokesActions() = runComposeUiTest {
        val component = UserEditComponentMock(
            initialState = UserEditScreenState.Content(
                user = createTestUserPrivate(),
                nicknameInput = "johndoe",
                displayNameInput = "John Doe"
            )
        )
        setContent {
            ComponentTestHarness {
                UserEditScreen(component)
            }
        }

        onNodeWithTag(UserEditTestTags.TITLE).assertIsDisplayed()
        onNodeWithTag(UserEditTestTags.BACK_BUTTON).assertIsDisplayed()
        onNodeWithTag(UserEditTestTags.NICKNAME_INPUT).assertIsDisplayed()
        onNodeWithTag(UserEditTestTags.DISPLAY_NAME_INPUT).assertIsDisplayed()
        onNodeWithTag(UserEditTestTags.SAVE_BUTTON).assertIsDisplayed().performClick()

        assertEquals(1, component.saveCalls)
    }

    @Test
    fun error_displaysGlobalError() = runComposeUiTest {
        val component = UserEditComponentMock(
            initialState = UserEditScreenState.Error(error = CommonError.Unknown())
        )
        setContent {
            ComponentTestHarness {
                UserEditScreen(component)
            }
        }

        onNodeWithTag(UserEditTestTags.GLOBAL_ERROR_TEXT).assertIsDisplayed()
    }

    @Test
    fun backButton_invokesOnBackClick() = runComposeUiTest {
        val component = UserEditComponentMock()
        setContent {
            ComponentTestHarness {
                UserEditScreen(component)
            }
        }

        onNodeWithTag(UserEditTestTags.BACK_BUTTON).performClick()

        assertEquals(1, component.backCalls)
    }
}
