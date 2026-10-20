package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.main

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.main.MainManagementComponentMock
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertTrue

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
class MainManagementScreenTest {

    @Test
    fun rendersTitleAndButtonsAndTriggersCallbacks() = runComposeUiTest {
        var usersClicked = false
        var sessionsClicked = false
        var identifiersClicked = false
        var auditClicked = false
        var settingsClicked = false

        val component = object : MainManagementComponentMock() {
            override fun onGlobalUserListClick() { usersClicked = true }
            override fun onGlobalSessionListClick() { sessionsClicked = true }
            override fun onGlobalIdentifierListClick() { identifiersClicked = true }
            override fun onAuditEventListClick() { auditClicked = true }
            override fun onSettingsClick() { settingsClicked = true }
        }

        setContent {
            ComponentTestHarness {
                MainManagementScreen(component)
            }
        }

        onNodeWithTag(MainManagementTestTags.TITLE).assertIsDisplayed()

        onNodeWithTag(MainManagementTestTags.GLOBAL_USER_LIST_BUTTON).performScrollTo().performClick()
        assertTrue(usersClicked)

        onNodeWithTag(MainManagementTestTags.SESSIONS_BUTTON).performScrollTo().performClick()
        assertTrue(sessionsClicked)

        onNodeWithTag(MainManagementTestTags.IDENTIFIERS_BUTTON).performScrollTo().performClick()
        assertTrue(identifiersClicked)

        onNodeWithTag(MainManagementTestTags.AUDIT_EVENT_LIST_BUTTON).performScrollTo().performClick()
        assertTrue(auditClicked)

        onNodeWithTag(MainManagementTestTags.SETTINGS_BUTTON).performScrollTo().performClick()
        assertTrue(settingsClicked)
    }
}
