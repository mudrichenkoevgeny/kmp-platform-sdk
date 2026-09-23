package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.detail

import android.app.Application
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.session.userSessionMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.session.detail.SessionDetailComponentMock
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK], application = Application::class)
class SessionDetailScreenTest {

    @Test
    fun displaysSessionDetails_andInvokesRevoke() = runComposeUiTest {
        val session = userSessionMock()
        val component = SessionDetailComponentMock(
            initialState = SessionDetailScreenState.Content(
                session = session,
                isCurrentSession = false
            )
        )

        setContent {
            ComponentTestHarness {
                SessionDetailScreen(component)
            }
        }

        onNodeWithTag(SessionDetailTestTags.TITLE).assertIsDisplayed()
        onNodeWithTag(SessionDetailTestTags.BACK_BUTTON).assertIsDisplayed()
        onNodeWithTag(SessionDetailTestTags.SESSION_CARD).assertIsDisplayed()
        onNodeWithTag(SessionDetailTestTags.IDENTIFIER_DISPLAY_NAME).assertIsDisplayed()
        onNodeWithTag(SessionDetailTestTags.AUTH_PROVIDER).assertIsDisplayed()
        onNodeWithTag(SessionDetailTestTags.DEVICE_NAME).assertIsDisplayed()
        onNodeWithTag(SessionDetailTestTags.CLIENT_TYPE).assertIsDisplayed()
        onNodeWithTag(SessionDetailTestTags.LANGUAGE).assertIsDisplayed()
        onNodeWithTag(SessionDetailTestTags.APP_VERSION).assertIsDisplayed()
        onNodeWithTag(SessionDetailTestTags.OS_VERSION).assertIsDisplayed()
        onNodeWithTag(SessionDetailTestTags.IP_ADDRESS).assertIsDisplayed()
        onNodeWithTag(SessionDetailTestTags.LAST_ACCESSED_AT).assertIsDisplayed()
        onNodeWithTag(SessionDetailTestTags.CREATED_AT).assertIsDisplayed()
        onNodeWithTag(SessionDetailTestTags.REVOKE_BUTTON).assertIsDisplayed().performClick()

        assertEquals(1, component.revokeSessionCalls)
    }

    @Test
    fun currentSession_hidesRevokeButton() = runComposeUiTest {
        val session = userSessionMock()
        val component = SessionDetailComponentMock(
            initialState = SessionDetailScreenState.Content(
                session = session,
                isCurrentSession = true
            )
        )

        setContent {
            ComponentTestHarness {
                SessionDetailScreen(component)
            }
        }

        onNodeWithTag(SessionDetailTestTags.REVOKE_BUTTON).assertDoesNotExist()
    }

    @Test
    fun error_showsGlobalErrorTextNode() = runComposeUiTest {
        val component = SessionDetailComponentMock(
            initialState = SessionDetailScreenState.Error(error = CommonError.Unknown())
        )

        setContent {
            ComponentTestHarness {
                SessionDetailScreen(component)
            }
        }

        onNodeWithTag(SessionDetailTestTags.GLOBAL_ERROR).assertIsDisplayed()
    }

    @Test
    fun backButton_invokesOnBackClick() = runComposeUiTest {
        val component = SessionDetailComponentMock(
            initialState = SessionDetailScreenState.Content(
                session = userSessionMock(),
                isCurrentSession = false
            )
        )

        setContent {
            ComponentTestHarness {
                SessionDetailScreen(component)
            }
        }

        onNodeWithTag(SessionDetailTestTags.BACK_BUTTON).performClick()

        assertEquals(1, component.backCalls)
    }
}
