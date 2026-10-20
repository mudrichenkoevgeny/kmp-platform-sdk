package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.welcome

import android.app.Application
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoadingConfig
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.login.welcome.LoginWelcomeComponentMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.AvailableAuthProviders
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK], application = Application::class)
class LoginWelcomeScreenTest {

    @Test
    fun loading_showsIndeterminateProgressAfterDefaultDelay() = runComposeUiTest {
        val component = LoginWelcomeComponentMock(LoginWelcomeScreenState.Loading)
        setContent {
            ComponentTestHarness {
                LoginWelcomeScreen(component)
            }
        }
        mainClock.autoAdvance = false
        mainClock.advanceTimeBy(FullscreenLoadingConfig.DELAY_MILLIS + LOADING_EXTRA_DELAY_MS)
        onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertExists()
    }

    @Test
    fun initializationError_showsMessageAndRetry_invokesOnRetry() = runComposeUiTest {
        val component = LoginWelcomeComponentMock(
            LoginWelcomeScreenState.InitializationError(
                error = CommonError.Unknown(isRetryable = true)
            )
        )
        setContent {
            ComponentTestHarness {
                LoginWelcomeScreen(component)
            }
        }
        onNodeWithText(MOCK_ERROR_MESSAGE).assertIsDisplayed()
        onNodeWithText(RETRY_LABEL).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.retryInitCalls)
    }

    @Test
    fun content_displaysTitlePrimaryButtonAndOrDividerWhenSecondaryPresent() = runComposeUiTest {
        val providers = AvailableAuthProviders(
            primary = listOf(UserAuthProvider.EMAIL),
            secondary = listOf(UserAuthProvider.GOOGLE)
        )
        val component = LoginWelcomeComponentMock(
            LoginWelcomeScreenState.Content(availableAuthProviders = providers)
        )
        setContent {
            ComponentTestHarness {
                LoginWelcomeScreen(component)
            }
        }
        onNodeWithTag(LoginWelcomeTestTags.TITLE).assertIsDisplayed()
        onNodeWithTag(LoginWelcomeTestTags.getAuthProviderTag(UserAuthProvider.EMAIL)).assertIsDisplayed()
        onNodeWithTag(LoginWelcomeTestTags.OR_DIVIDER).assertIsDisplayed()
        onNodeWithTag(LoginWelcomeTestTags.SECONDARY_PROVIDERS_GRID).assertIsDisplayed()
    }

    @Test
    fun content_clickPrimaryEmail_invokesOnLoginClick() = runComposeUiTest {
        val providers = AvailableAuthProviders(
            primary = listOf(UserAuthProvider.EMAIL),
            secondary = emptyList()
        )
        val component = LoginWelcomeComponentMock(
            LoginWelcomeScreenState.Content(availableAuthProviders = providers)
        )
        setContent {
            ComponentTestHarness {
                LoginWelcomeScreen(component)
            }
        }
        onNodeWithTag(LoginWelcomeTestTags.getAuthProviderTag(UserAuthProvider.EMAIL)).performClick()
        assertEquals(listOf(UserAuthProvider.EMAIL), component.loginClicks)
    }

    @Test
    fun content_inlineActionError_showsLocalizedMessage() = runComposeUiTest {
        val providers = AvailableAuthProviders(
            primary = listOf(UserAuthProvider.EMAIL),
            secondary = emptyList()
        )
        val component = LoginWelcomeComponentMock(
            LoginWelcomeScreenState.Content(
                availableAuthProviders = providers,
                actionError = CommonError.Unknown()
            )
        )
        setContent {
            ComponentTestHarness {
                LoginWelcomeScreen(component)
            }
        }
        onNodeWithTag(LoginWelcomeTestTags.ACTION_ERROR_TEXT).assertIsDisplayed()
    }

    @Test
    fun content_legalLinksVisible_clickPrivacyAndTerms_invokesCallbacks() = runComposeUiTest {
        val providers = AvailableAuthProviders(
            primary = listOf(UserAuthProvider.EMAIL),
            secondary = emptyList()
        )
        val component = LoginWelcomeComponentMock(
            LoginWelcomeScreenState.Content(
                availableAuthProviders = providers,
                privacyPolicyUrl = PRIVACY_URL,
                termsOfServiceUrl = TERMS_URL
            )
        )
        setContent {
            ComponentTestHarness {
                LoginWelcomeScreen(component)
            }
        }
        onNodeWithText(PRIVACY_POLICY_LABEL).performClick()
        onNodeWithText(TERMS_OF_SERVICE_LABEL).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.privacyPolicyCalls)
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.termsOfServiceCalls)
    }

    private companion object {
        const val LOADING_EXTRA_DELAY_MS = 50L
        const val EXPECTED_SINGLE_CALLBACK = 1

        const val RETRY_LABEL = "Retry"
        const val PRIVACY_POLICY_LABEL = "Privacy Policy"
        const val TERMS_OF_SERVICE_LABEL = "Terms of service"

        const val PRIVACY_URL = "https://example.com/privacy"
        const val TERMS_URL = "https://example.com/terms"

        const val MOCK_ERROR_MESSAGE = "Unknown Error"
    }
}