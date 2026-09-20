package io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.ui.component.audit.item

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
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.mock.domain.model.event.auditEventMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.ui.screen.events.AuditEventsTestTags
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
class AuditItemTest {

    @Test
    fun rendersAuditEventAndTriggersClick() = runComposeUiTest {
        var clicks = 0
        val event = auditEventMock()

        setContent {
            CoreTheme {
                Surface {
                    Box(modifier = Modifier.padding(CoreTheme.dimens.paddingLarge)) {
                        AuditItem(
                            event = event,
                            onClick = { clicks++ }
                        )
                    }
                }
            }
        }

        onNodeWithText("ID: ${event.id.value}", substring = true).assertIsDisplayed()
        onNodeWithText("Status: SUCCESS", substring = true).assertIsDisplayed()

        onNodeWithTag(AuditEventsTestTags.AUDIT_ITEM_PREFIX + event.id.value).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, clicks)
    }

    private companion object {
        const val EXPECTED_SINGLE_CALLBACK = 1
    }
}
