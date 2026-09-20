package io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.session.item

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.session.userSessionMock
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
class SessionItemTest {

    @Test
    fun rendersSessionDataAndTriggersRevoke() = runComposeUiTest {
        var revokeClicks = 0
        val session = userSessionMock()

        setContent {
            CoreTheme {
                Surface {
                    Box(modifier = Modifier.padding(CoreTheme.dimens.paddingLarge)) {
                        SessionItem(
                            session = session,
                            onRevokeClick = { revokeClicks++ },
                            enabled = true
                        )
                    }
                }
            }
        }

        onNodeWithText("MockUserAgent/1.0").assertIsDisplayed()
        onNodeWithText("127.0.0.1", substring = true).assertIsDisplayed()

        onNodeWithText("Revoke").performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, revokeClicks)
    }

    @Test
    fun disabled_revokeButtonIsNotEnabled() = runComposeUiTest {
        val session = userSessionMock()

        setContent {
            CoreTheme {
                Surface {
                    Box(modifier = Modifier.padding(CoreTheme.dimens.paddingLarge)) {
                        SessionItem(
                            session = session,
                            onRevokeClick = {},
                            enabled = false
                        )
                    }
                }
            }
        }

        onNodeWithText("Revoke").assertIsNotEnabled()
    }

    private companion object {
        const val EXPECTED_SINGLE_CALLBACK = 1
    }
}
