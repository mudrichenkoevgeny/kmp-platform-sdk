package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.security

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.settings.security.EditSecuritySettingsComponentMock
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
class EditSecuritySettingsScreenTest {

    @Test
    fun rendersFormFieldsAndSaveButton() = runComposeUiTest {
        val sampleContent = EditSecuritySettingsScreenState.Content(
            recentAuthenticationValiditySecondsForOpenUser = "300",
            recentAuthenticationValiditySecondsForManagementUser = "300",
            mfaTokenExpirationSeconds = "300",
            passwordMinLength = "8",
            passwordRequireLetter = true,
            passwordRequireUpperCase = false,
            passwordRequireLowerCase = false,
            passwordRequireDigit = true,
            passwordRequireSpecialChar = false,
            commonPasswords = "password,123456",
            accountLockoutMaxFailedPasswordAttempts = "5",
            accountLockoutMaxFailedOtpAttempts = "5",
            accountLockoutMaxFailedTotpAttempts = "5",
            accountLockoutFailedAttemptsWindowSeconds = "300",
            accountLockoutDurationSeconds = "300",
            accountLockoutIndefiniteLockoutThreshold = "3",
            accountLockoutIsSelfServiceUnlockEnabled = true,
            accountLockoutCheckIntervalSeconds = "60",
            refreshTokenRotationGracePeriodSeconds = "30",
            openIpBlacklistEnabled = false,
            openIpBlacklist = "",
            openIpWhitelistEnabled = false,
            openIpWhitelist = "",
            managementIpBlacklistEnabled = false,
            managementIpBlacklist = "",
            managementIpWhitelistEnabled = false,
            managementIpWhitelist = "",
            otpRetryAfterSeconds = "60",
            otpNumberOfSymbols = "6",
            otpExpirationSeconds = "300",
            maxRequestsPerPeriod = "100",
            rateLimitPeriodSeconds = "60"
        )
        val component = EditSecuritySettingsComponentMock(initialState = sampleContent)

        setContent {
            ComponentTestHarness {
                EditSecuritySettingsScreen(component)
            }
        }

        onNodeWithTag(EditSecuritySettingsTestTags.TITLE).assertIsDisplayed()
        onNodeWithTag(EditSecuritySettingsTestTags.SAVE_BUTTON).assertExists()
    }
}
