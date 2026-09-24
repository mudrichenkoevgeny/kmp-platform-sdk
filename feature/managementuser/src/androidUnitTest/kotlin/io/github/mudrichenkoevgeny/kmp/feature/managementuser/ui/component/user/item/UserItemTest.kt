package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.component.user.item

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
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.globallist.GlobalUserListTestTags
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.user.userDetailsMock
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
class UserItemTest {

    @Test
    fun rendersUserDetailsAndTriggersClick() = runComposeUiTest {
        var clicks = 0
        val user = userDetailsMock()

        setContent {
            CoreTheme {
                Surface {
                    Box(modifier = Modifier.padding(CoreTheme.dimens.paddingLarge)) {
                        UserItem(
                            user = user,
                            onClick = { clicks++ }
                        )
                    }
                }
            }
        }

        onNodeWithText(user.id.value.toString(), substring = true).assertIsDisplayed()
        onNodeWithText(user.role.name, substring = true).assertIsDisplayed()
        onNodeWithText(user.accountStatus.name, substring = true).assertIsDisplayed()

        onNodeWithTag(GlobalUserListTestTags.USER_ITEM_PREFIX + user.id.value).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, clicks)
    }

    private companion object {
        const val EXPECTED_SINGLE_CALLBACK = 1
    }
}
