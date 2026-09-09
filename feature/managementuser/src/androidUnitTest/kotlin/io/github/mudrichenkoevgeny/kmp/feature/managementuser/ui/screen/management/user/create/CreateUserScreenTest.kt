package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.create

import android.app.Application
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.user.create.CreateUserComponentMock
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK], application = Application::class)
class CreateUserScreenTest {

    @Test
    fun content_displaysFormFields() = runComposeUiTest {
        val component = CreateUserComponentMock()
        setContent {
            ComponentTestHarness {
                CreateUserScreen(component)
            }
        }
        onNodeWithTag(CreateUserTestTags.TITLE).assertIsDisplayed()
        onNodeWithTag(CreateUserTestTags.BACK_BUTTON).assertIsDisplayed()
        onNodeWithTag(CreateUserTestTags.EMAIL_INPUT).assertIsDisplayed()
        onNodeWithTag(CreateUserTestTags.PASSWORD_INPUT).assertIsDisplayed()
        onNodeWithTag(CreateUserTestTags.ROLE_INPUT).assertIsDisplayed()
        onNodeWithTag(CreateUserTestTags.STATUS_INPUT).performScrollTo().assertIsDisplayed()
        onNodeWithTag(CreateUserTestTags.AUTHORITY_LEVEL_INPUT).performScrollTo().assertIsDisplayed()
        onNodeWithTag(CreateUserTestTags.CREATE_BUTTON).performScrollTo().assertIsDisplayed()
    }

    @Test
    fun content_typeEmail_updatesComponent() = runComposeUiTest {
        val component = CreateUserComponentMock()
        setContent {
            ComponentTestHarness {
                CreateUserScreen(component)
            }
        }
        onNodeWithTag(CreateUserTestTags.EMAIL_INPUT).performTextInput("newuser@example.com")
        assertEquals("newuser@example.com", component.lastEmailChanged)
    }

    @Test
    fun content_clickCreate_invokesCallback() = runComposeUiTest {
        val component = CreateUserComponentMock()
        setContent {
            ComponentTestHarness {
                CreateUserScreen(component)
            }
        }
        onNodeWithTag(CreateUserTestTags.CREATE_BUTTON).performScrollTo().performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.createCalls)
    }

    @Test
    fun content_clickBack_invokesCallback() = runComposeUiTest {
        val component = CreateUserComponentMock()
        setContent {
            ComponentTestHarness {
                CreateUserScreen(component)
            }
        }
        onNodeWithTag(CreateUserTestTags.BACK_BUTTON).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.backCalls)
    }

    @Test
    fun error_displaysErrorText() = runComposeUiTest {
        val component = CreateUserComponentMock(
            initialState = CreateUserScreenState(error = CommonError.Unknown()),
        )
        setContent {
            ComponentTestHarness {
                CreateUserScreen(component)
            }
        }
        onNodeWithTag(CreateUserTestTags.ERROR_TEXT).performScrollTo().assertIsDisplayed()
    }

    private companion object {
        const val EXPECTED_SINGLE_CALLBACK = 1
    }
}
