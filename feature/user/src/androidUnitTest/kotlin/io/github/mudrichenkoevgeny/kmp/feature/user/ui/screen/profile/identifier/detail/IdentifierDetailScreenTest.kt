package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.detail

import android.app.Application
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.identifier.userIdentifierMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.identifier.detail.IdentifierDetailComponentMock
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK], application = Application::class)
class IdentifierDetailScreenTest {

    @Test
    fun displaysIdentifierDetails_andInvokesDelete() = runComposeUiTest {
        val identifier = userIdentifierMock()
        val component = IdentifierDetailComponentMock(
            initialState = IdentifierDetailScreenState.Content(
                identifier = identifier,
                isCurrentIdentifier = false,
                canChangePassword = true,
                canDeletePassword = false
            )
        )

        setContent {
            ComponentTestHarness {
                IdentifierDetailScreen(component)
            }
        }

        onNodeWithTag(IdentifierDetailTestTags.TITLE).assertIsDisplayed()
        onNodeWithTag(IdentifierDetailTestTags.BACK_BUTTON).assertIsDisplayed()
        onNodeWithTag(IdentifierDetailTestTags.CARD).assertIsDisplayed()
        onNodeWithTag(IdentifierDetailTestTags.CHANGE_PASSWORD_BUTTON).performScrollTo().assertIsDisplayed().performClick()
        assertEquals(1, component.changePasswordCalls)

        onNodeWithTag(IdentifierDetailTestTags.DELETE_BUTTON).performScrollTo().assertIsDisplayed().performClick()
        assertEquals(1, component.deleteIdentifierRequestedCalls)
    }

    @Test
    fun currentIdentifier_hidesDeleteButton() = runComposeUiTest {
        val identifier = userIdentifierMock()
        val component = IdentifierDetailComponentMock(
            initialState = IdentifierDetailScreenState.Content(
                identifier = identifier,
                isCurrentIdentifier = true,
                canChangePassword = false,
                canDeletePassword = false
            )
        )

        setContent {
            ComponentTestHarness {
                IdentifierDetailScreen(component)
            }
        }

        onNodeWithTag(IdentifierDetailTestTags.DELETE_BUTTON).assertDoesNotExist()
    }

    @Test
    fun error_showsGlobalErrorNode() = runComposeUiTest {
        val component = IdentifierDetailComponentMock(
            initialState = IdentifierDetailScreenState.Error(error = CommonError.Unknown())
        )

        setContent {
            ComponentTestHarness {
                IdentifierDetailScreen(component)
            }
        }

        onNodeWithTag(IdentifierDetailTestTags.GLOBAL_ERROR).assertIsDisplayed()
    }

    @Test
    fun backButton_invokesOnBackClick() = runComposeUiTest {
        val component = IdentifierDetailComponentMock(
            initialState = IdentifierDetailScreenState.Content(
                identifier = userIdentifierMock(),
                isCurrentIdentifier = false
            )
        )

        setContent {
            ComponentTestHarness {
                IdentifierDetailScreen(component)
            }
        }

        onNodeWithTag(IdentifierDetailTestTags.BACK_BUTTON).performClick()

        assertEquals(1, component.backCalls)
    }
}
