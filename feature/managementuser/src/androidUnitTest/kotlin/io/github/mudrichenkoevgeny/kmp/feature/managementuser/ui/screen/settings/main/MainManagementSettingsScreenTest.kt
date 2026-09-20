package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.main

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.settings.main.MainManagementSettingsComponentMock
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertTrue

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
class MainManagementSettingsScreenTest {

    @Test
    fun rendersTitleAndButtonsAndTriggersCallbacks() = runComposeUiTest {
        var usersClicked = false
        var auditClicked = false
        var authClicked = false
        var globalClicked = false
        var securityClicked = false

        val component = object : MainManagementSettingsComponentMock() {
            override fun onUsersManagementClick() { usersClicked = true }
            override fun onAuditLogsClick() { auditClicked = true }
            override fun onEditAuthSettingsClick() { authClicked = true }
            override fun onEditGlobalSettingsClick() { globalClicked = true }
            override fun onEditSecuritySettingsClick() { securityClicked = true }
        }

        setContent {
            ComponentTestHarness {
                MainManagementSettingsScreen(component)
            }
        }

        onNodeWithTag(MainManagementSettingsTestTags.TITLE).assertIsDisplayed()

        onNodeWithTag(MainManagementSettingsTestTags.USERS_MANAGEMENT_BUTTON).performClick()
        assertTrue(usersClicked)

        onNodeWithTag(MainManagementSettingsTestTags.AUDIT_LOGS_BUTTON).performClick()
        assertTrue(auditClicked)

        onNodeWithTag(MainManagementSettingsTestTags.EDIT_AUTH_SETTINGS_BUTTON).performClick()
        assertTrue(authClicked)

        onNodeWithTag(MainManagementSettingsTestTags.EDIT_GLOBAL_SETTINGS_BUTTON).performClick()
        assertTrue(globalClicked)

        onNodeWithTag(MainManagementSettingsTestTags.EDIT_SECURITY_SETTINGS_BUTTON).performClick()
        assertTrue(securityClicked)
    }
}
