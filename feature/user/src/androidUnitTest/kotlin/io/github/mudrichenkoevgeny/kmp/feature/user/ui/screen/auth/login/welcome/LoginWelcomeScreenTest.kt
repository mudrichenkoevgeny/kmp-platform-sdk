package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.welcome

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.MockAppErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoadingConfig
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.settings.AvailableAuthProviders
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserAuthProvider
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
class LoginWelcomeScreenTest {

    @Test
    fun loading_showsIndeterminateProgressAfterDefaultDelay() = runComposeUiTest {
        val component = FakeLoginWelcomeComponent(LoginWelcomeScreenState.Loading)
        setContent {
            LoginWelcomeScreenHarness(component)
        }
        mainClock.autoAdvance = false
        mainClock.advanceTimeBy(FullscreenLoadingConfig.DELAY_MILLIS + LOADING_EXTRA_DELAY_MS)
        onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertExists()
    }

    @Test
    fun initializationError_showsMessageAndRetry_invokesOnRetry() = runComposeUiTest {
        val component = FakeLoginWelcomeComponent(
            LoginWelcomeScreenState.InitializationError(
                error = CommonError.Unknown(isRetryable = true)
            )
        )
        setContent {
            LoginWelcomeScreenHarness(component)
        }
        onNodeWithText(MOCK_ERROR_MESSAGE).assertIsDisplayed()
        onNodeWithText(RETRY_LABEL).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.retryInitClicks)
    }

    @Test
    fun content_displaysTitlePrimaryButtonAndOrDividerWhenSecondaryPresent() = runComposeUiTest {
        val providers = AvailableAuthProviders(
            primary = listOf(UserAuthProvider.EMAIL),
            secondary = listOf(UserAuthProvider.GOOGLE)
        )
        val component = FakeLoginWelcomeComponent(
            LoginWelcomeScreenState.Content(availableAuthProviders = providers)
        )
        setContent {
            LoginWelcomeScreenHarness(component)
        }
        onNodeWithText(SIGN_IN_TITLE).assertIsDisplayed()
        onNodeWithText(SIGN_IN_WITH_EMAIL).assertIsDisplayed()
        onNodeWithText(OR_SIGN_IN_WITH).assertIsDisplayed()
    }

    @Test
    fun content_clickPrimaryEmail_invokesOnLoginClick() = runComposeUiTest {
        val providers = AvailableAuthProviders(
            primary = listOf(UserAuthProvider.EMAIL),
            secondary = emptyList()
        )
        val component = FakeLoginWelcomeComponent(
            LoginWelcomeScreenState.Content(availableAuthProviders = providers)
        )
        setContent {
            LoginWelcomeScreenHarness(component)
        }
        onNodeWithText(SIGN_IN_WITH_EMAIL).performClick()
        assertEquals(listOf(UserAuthProvider.EMAIL), component.loginClicks)
    }

    @Test
    fun content_inlineActionError_showsLocalizedMessage() = runComposeUiTest {
        val providers = AvailableAuthProviders(
            primary = listOf(UserAuthProvider.EMAIL),
            secondary = emptyList()
        )
        val component = FakeLoginWelcomeComponent(
            LoginWelcomeScreenState.Content(
                availableAuthProviders = providers,
                actionError = CommonError.Unknown()
            )
        )
        setContent {
            LoginWelcomeScreenHarness(component)
        }
        onNodeWithText(MOCK_ERROR_MESSAGE).assertIsDisplayed()
    }

    @Test
    fun content_legalLinksVisible_clickPrivacyAndTerms_invokesCallbacks() = runComposeUiTest {
        val providers = AvailableAuthProviders(
            primary = listOf(UserAuthProvider.EMAIL),
            secondary = emptyList()
        )
        val component = FakeLoginWelcomeComponent(
            LoginWelcomeScreenState.Content(
                availableAuthProviders = providers,
                privacyPolicyUrl = PRIVACY_URL,
                termsOfServiceUrl = TERMS_URL
            )
        )
        setContent {
            LoginWelcomeScreenHarness(component)
        }
        onNodeWithText(PRIVACY_POLICY_LABEL).performClick()
        onNodeWithText(TERMS_OF_SERVICE_LABEL).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.privacyPolicyClicks)
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.termsOfServiceClicks)
    }

    private companion object {
        const val LOADING_EXTRA_DELAY_MS = 50L
        const val EXPECTED_SINGLE_CALLBACK = 1

        /** Mirrors default `values/strings.xml` (tests assert visible UI copy). */
        const val SIGN_IN_TITLE = "Sign In"
        const val SIGN_IN_WITH_EMAIL = "Sign in with Email"
        const val OR_SIGN_IN_WITH = "Or sign in with"
        const val RETRY_LABEL = "Retry"
        const val PRIVACY_POLICY_LABEL = "Privacy Policy"
        const val TERMS_OF_SERVICE_LABEL = "Terms of service"

        const val PRIVACY_URL = "https://example.com/privacy"
        const val TERMS_URL = "https://example.com/terms"

        /** [MockAppErrorParser] always returns this string for any [AppError]. */
        const val MOCK_ERROR_MESSAGE = "Unknown Error"
    }
}

@Composable
private fun LoginWelcomeScreenHarness(component: LoginWelcomeComponent) {
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides MockAppErrorParser) {
            LoginWelcomeScreen(component)
        }
    }
}

private class FakeLoginWelcomeComponent(
    initial: LoginWelcomeScreenState
) : LoginWelcomeComponent {

    private val mutableState = MutableValue(initial)
    override val state: Value<LoginWelcomeScreenState> get() = mutableState

    var retryInitClicks: Int = 0
        private set

    val loginClicks: MutableList<UserAuthProvider> = mutableListOf()

    var privacyPolicyClicks: Int = 0
        private set

    var termsOfServiceClicks: Int = 0
        private set

    override fun onRetryInitClick() {
        retryInitClicks++
    }

    override fun onLoginClick(authProvider: UserAuthProvider) {
        loginClicks.add(authProvider)
    }

    override fun onPrivacyPolicyClick() {
        privacyPolicyClicks++
    }

    override fun onTermsOfServiceClick() {
        termsOfServiceClicks++
    }
}
