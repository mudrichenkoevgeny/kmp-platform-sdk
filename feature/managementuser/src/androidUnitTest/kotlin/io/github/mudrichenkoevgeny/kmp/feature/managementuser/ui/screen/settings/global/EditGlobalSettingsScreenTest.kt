package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.global

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.settings.global.EditGlobalSettingsComponentMock
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
class EditGlobalSettingsScreenTest {

    @Test
    fun rendersFormFieldsAndSaveButton() = runComposeUiTest {
        val sampleContent = EditGlobalSettingsScreenState.Content(
            privacyPolicyUrl = "https://example.com/privacy",
            termsOfServiceUrl = "https://example.com/terms",
            contactSupportEmail = "support@example.com",
            minVersionAndroid = "1.0.0",
            minVersionIos = "1.0.0",
            minVersionWeb = "1.0.0",
            minVersionDesktop = "1.0.0",
            isTracingEnabled = true,
            isMetricsEnabled = true,
            isVerboseLoggingEnabled = false
        )
        val component = EditGlobalSettingsComponentMock(initialState = sampleContent)

        setContent {
            ComponentTestHarness {
                EditGlobalSettingsScreen(component)
            }
        }

        onNodeWithTag(EditGlobalSettingsTestTags.TITLE).assertIsDisplayed()
        onNodeWithTag(EditGlobalSettingsTestTags.SAVE_BUTTON).assertExists()
    }
}
