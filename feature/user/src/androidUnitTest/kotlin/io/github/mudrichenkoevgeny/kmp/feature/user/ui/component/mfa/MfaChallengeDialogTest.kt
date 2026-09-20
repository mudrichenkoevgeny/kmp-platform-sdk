package io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.mfa

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.DialogPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.feature.user.network.httpclient.mfa.MfaChallengeRequest
import kotlinx.coroutines.CompletableDeferred
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
class MfaChallengeDialogTest {

    @Test
    fun rendersDialogAndSubmitsCodeOnConfirm() = runComposeUiTest {
        var submittedCode: String? = null
        var cancelCount = 0
        val request = MfaChallengeRequest("test_token", CompletableDeferred())

        setContent {
            DialogPreviewContainer {
                MfaChallengeDialog(
                    request = request,
                    onConfirm = { submittedCode = it },
                    onCancel = { cancelCount++ }
                )
            }
        }

        onNodeWithTag(MfaChallengeDialogTestTags.TITLE).assertIsDisplayed()
        onNodeWithTag(MfaChallengeDialogTestTags.DESC).assertIsDisplayed()

        onNodeWithTag(MfaChallengeDialogTestTags.CONFIRM_BUTTON).assertIsNotEnabled()

        onNodeWithTag(MfaChallengeDialogTestTags.CODE_INPUT).performTextInput("123456")

        onNodeWithTag(MfaChallengeDialogTestTags.CONFIRM_BUTTON).performClick()

        assertEquals("123456", submittedCode)
        assertEquals(0, cancelCount)
    }

    @Test
    fun triggersCancelCallbackOnCancel() = runComposeUiTest {
        var cancelCount = 0
        val request = MfaChallengeRequest("test_token", CompletableDeferred())

        setContent {
            DialogPreviewContainer {
                MfaChallengeDialog(
                    request = request,
                    onConfirm = {},
                    onCancel = { cancelCount++ }
                )
            }
        }

        onNodeWithTag(MfaChallengeDialogTestTags.CANCEL_BUTTON).performClick()

        assertEquals(1, cancelCount)
    }
}
