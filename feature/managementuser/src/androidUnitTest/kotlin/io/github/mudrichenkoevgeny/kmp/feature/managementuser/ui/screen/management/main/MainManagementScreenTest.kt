package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.main

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
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
        var auditClicked = false
        var authClicked = false
        var globalClicked = false
        var securityClicked = false

        val component = object : MainManagementComponentMock() {
            override fun onGlobalUserListClick() { usersClicked = true }
            override fun onAuditEventListClick() { auditClicked = true }
            override fun onEditAuthSettingsClick() { authClicked = true }
            override fun onEditGlobalSettingsClick() { globalClicked = true }
            override fun onEditSecuritySettingsClick() { securityClicked = true }
        }

        setContent {
            ComponentTestHarness {
                MainManagementScreen(component)
            }
        }

        onNodeWithTag(MainManagementTestTags.TITLE).assertIsDisplayed()

        onNodeWithTag(MainManagementTestTags.GLOBAL_USER_LIST_BUTTON).performClick()
        assertTrue(usersClicked)

        onNodeWithTag(MainManagementTestTags.AUDIT_EVENT_LIST_BUTTON).performClick()
        assertTrue(auditClicked)

        onNodeWithTag(MainManagementTestTags.EDIT_AUTH_SETTINGS_BUTTON).performClick()
        assertTrue(authClicked)

        onNodeWithTag(MainManagementTestTags.EDIT_GLOBAL_SETTINGS_BUTTON).performClick()
        assertTrue(globalClicked)

        onNodeWithTag(MainManagementTestTags.EDIT_SECURITY_SETTINGS_BUTTON).performClick()
        assertTrue(securityClicked)
    }
}