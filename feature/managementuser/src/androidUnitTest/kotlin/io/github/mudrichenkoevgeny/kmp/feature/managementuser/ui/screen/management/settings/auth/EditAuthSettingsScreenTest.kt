package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.auth

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.settings.auth.EditAuthSettingsComponentMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
class EditAuthSettingsScreenTest {

    @Test
    fun rendersContentStateAndFormFields() = runComposeUiTest {
        val sampleContent = EditAuthSettingsScreenState.Content(
            enabledProviders = setOf(UserAuthProvider.EMAIL, UserAuthProvider.GOOGLE),
            maxTotalIdentifiers = "10",
            maxEmailIdentifiers = "5",
            maxPhoneIdentifiers = "5",
            maxIdentifiersPerExternalProvider = "2",
            maxActiveSessionsForOpenUser = "3",
            maxActiveSessionsForManagementUser = "5",
            accessTokenExpirationSeconds = "3600",
            refreshTokenExpirationSeconds = "86400",
            accountDeletionGracePeriodSeconds = "604800",
            accountDeletionCheckIntervalSeconds = "86400"
        )
        val component = EditAuthSettingsComponentMock(initialState = sampleContent)

        setContent {
            ComponentTestHarness {
                EditAuthSettingsScreen(component)
            }
        }

        onNodeWithTag(EditAuthSettingsTestTags.TITLE).assertIsDisplayed()
        onNodeWithTag(EditAuthSettingsTestTags.SAVE_BUTTON).assertExists()
    }
}