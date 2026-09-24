package io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.identifier.item

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.identifier.userIdentifierMock
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.list.IdentifierListTestTags
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
class IdentifierItemTest {

    @Test
    fun rendersIdentifierDataAndTriggersClick() = runComposeUiTest {
        var clicks = 0
        val identifier = userIdentifierMock()

        setContent {
            CoreTheme {
                Surface {
                    Box(modifier = Modifier.padding(CoreTheme.dimens.paddingLarge)) {
                        IdentifierItem(
                            identifier = identifier,
                            onClick = { clicks++ },
                            isCurrentIdentifier = false
                        )
                    }
                }
            }
        }

        onNodeWithText("user@example.com").assertIsDisplayed()
        onNodeWithText("EMAIL").assertIsDisplayed()

        onNodeWithTag(IdentifierListTestTags.IDENTIFIER_ITEM_PREFIX + identifier.id.value).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, clicks)
    }

    @Test
    fun phoneIdentifier_rendersCorrectData() = runComposeUiTest {
        val identifier = userIdentifierMock().copy(
            userAuthProvider = UserAuthProvider.PHONE,
            identifier = "+1234567890",
            displayName = "+1234567890"
        )

        setContent {
            CoreTheme {
                Surface {
                    Box(modifier = Modifier.padding(CoreTheme.dimens.paddingLarge)) {
                        IdentifierItem(
                            identifier = identifier,
                            onClick = {},
                            isCurrentIdentifier = false
                        )
                    }
                }
            }
        }

        onNodeWithText("+1234567890").assertIsDisplayed()
        onNodeWithText("PHONE").assertIsDisplayed()
    }

    private companion object {
        const val EXPECTED_SINGLE_CALLBACK = 1
    }
}

