package io.github.mudrichenkoevgeny.kmp.sample.app.ui.screen.profile

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.model.user.mockCurrentUser
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
class ProfileContentTest {

    @Test
    fun loading_showsIndeterminateProgress() = runComposeUiTest {
        setContent {
            MaterialTheme {
                ProfileContent(
                    state = ProfileScreenState.Loading,
                    onLoginClick = {}
                )
            }
        }
        onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertExists()
    }

    @Test
    fun unauthorized_showsMessageAndLogin_invokesCallback() = runComposeUiTest {
        var clicks = 0
        setContent {
            MaterialTheme {
                ProfileContent(
                    state = ProfileScreenState.Unauthorized,
                    onLoginClick = { clicks++ }
                )
            }
        }
        onNodeWithText(NOT_AUTHORIZED).assertIsDisplayed()
        onNodeWithText(LOGIN).assertIsDisplayed()
        onNodeWithText(LOGIN).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, clicks)
    }

    @Test
    fun content_showsAuthorizedMessage() = runComposeUiTest {
        setContent {
            MaterialTheme {
                ProfileContent(
                    state = ProfileScreenState.Content(user = mockCurrentUser()),
                    onLoginClick = {}
                )
            }
        }
        onNodeWithText(AUTHORIZED).assertIsDisplayed()
    }

    @Test
    fun error_showsErrorMessage() = runComposeUiTest {
        setContent {
            MaterialTheme {
                ProfileContent(
                    state = ProfileScreenState.Error(appError = CommonError.Unknown()),
                    onLoginClick = {}
                )
            }
        }
        onNodeWithText(ERROR_LABEL).assertIsDisplayed()
    }

    private companion object {
        const val EXPECTED_SINGLE_CALLBACK = 1
        const val NOT_AUTHORIZED = "Not authorized!"
        const val LOGIN = "Login"
        const val AUTHORIZED = "Authorized!"
        const val ERROR_LABEL = "Error"
    }
}
