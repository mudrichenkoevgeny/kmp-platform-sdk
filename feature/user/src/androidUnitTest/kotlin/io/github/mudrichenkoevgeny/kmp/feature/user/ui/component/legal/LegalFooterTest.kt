package io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.legal

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
class LegalFooterTest {

    @Test
    fun bothHidden_rendersNothing() = runComposeUiTest {
        setContent {
            MaterialTheme {
                LegalFooter(
                    isPrivacyPolicyVisible = false,
                    isTermsOfServiceVisible = false,
                    onPrivacyPolicyClick = {},
                    onTermsOfServiceClick = {}
                )
            }
        }
        assertFailsWith<AssertionError> {
            onNodeWithText(PRIVACY_POLICY_LABEL).assertIsDisplayed()
        }
    }

    @Test
    fun privacyOnly_showsPrefixAndPrivacy_invokesPrivacyCallback() = runComposeUiTest {
        var privacyClicks = 0
        var termsClicks = 0
        setContent {
            MaterialTheme {
                LegalFooter(
                    isPrivacyPolicyVisible = true,
                    isTermsOfServiceVisible = false,
                    onPrivacyPolicyClick = { privacyClicks++ },
                    onTermsOfServiceClick = { termsClicks++ }
                )
            }
        }
        onNodeWithText(LEGAL_PREFIX, substring = true).assertIsDisplayed()
        onNodeWithText(PRIVACY_POLICY_LABEL).assertIsDisplayed()
        onNodeWithText(PRIVACY_POLICY_LABEL).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, privacyClicks)
        assertEquals(0, termsClicks)
    }

    @Test
    fun termsOnly_showsPrefixAndTerms_invokesTermsCallback() = runComposeUiTest {
        var privacyClicks = 0
        var termsClicks = 0
        setContent {
            MaterialTheme {
                LegalFooter(
                    isPrivacyPolicyVisible = false,
                    isTermsOfServiceVisible = true,
                    onPrivacyPolicyClick = { privacyClicks++ },
                    onTermsOfServiceClick = { termsClicks++ }
                )
            }
        }
        onNodeWithText(LEGAL_PREFIX, substring = true).assertIsDisplayed()
        onNodeWithText(TERMS_OF_SERVICE_LABEL).assertIsDisplayed()
        onNodeWithText(TERMS_OF_SERVICE_LABEL).performClick()
        assertEquals(0, privacyClicks)
        assertEquals(EXPECTED_SINGLE_CALLBACK, termsClicks)
    }

    @Test
    fun bothVisible_showsPrefixConjunctionAndLinks_invokesBothCallbacks() = runComposeUiTest {
        var privacyClicks = 0
        var termsClicks = 0
        setContent {
            MaterialTheme {
                LegalFooter(
                    isPrivacyPolicyVisible = true,
                    isTermsOfServiceVisible = true,
                    onPrivacyPolicyClick = { privacyClicks++ },
                    onTermsOfServiceClick = { termsClicks++ }
                )
            }
        }
        onNodeWithText(LEGAL_PREFIX, substring = true).assertIsDisplayed()
        onNodeWithText(PRIVACY_POLICY_LABEL).assertIsDisplayed()
        onNodeWithText(AND_CONNECTOR).assertIsDisplayed()
        onNodeWithText(TERMS_OF_SERVICE_LABEL).assertIsDisplayed()
        onNodeWithText(PRIVACY_POLICY_LABEL).performClick()
        onNodeWithText(TERMS_OF_SERVICE_LABEL).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, privacyClicks)
        assertEquals(EXPECTED_SINGLE_CALLBACK, termsClicks)
    }

    private companion object {
        const val EXPECTED_SINGLE_CALLBACK = 1

        /** Mirrors `values/strings.xml` (assert visible copy). */
        const val LEGAL_PREFIX = "By signing in, you agree to our"
        const val PRIVACY_POLICY_LABEL = "Privacy Policy"
        const val TERMS_OF_SERVICE_LABEL = "Terms of service"

        const val AND_CONNECTOR = " and "
    }
}
