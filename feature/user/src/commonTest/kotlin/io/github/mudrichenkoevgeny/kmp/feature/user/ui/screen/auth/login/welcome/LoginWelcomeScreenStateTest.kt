package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.welcome

import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.AvailableAuthProviders
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LoginWelcomeScreenStateTest {

    @Test
    fun content_validPrivacyPolicyUrl_trimsBlankToNull() {
        assertNull(loginWelcomeScreenStateContent(privacyPolicyUrl = null).validPrivacyPolicyUrl)
        assertNull(loginWelcomeScreenStateContent(privacyPolicyUrl = EMPTY_STRING).validPrivacyPolicyUrl)
        assertNull(loginWelcomeScreenStateContent(privacyPolicyUrl = BLANK_ONLY).validPrivacyPolicyUrl)
        assertEquals(VALID_URL, loginWelcomeScreenStateContent(privacyPolicyUrl = VALID_URL).validPrivacyPolicyUrl)
    }

    @Test
    fun content_validTermsOfServiceUrl_trimsBlankToNull() {
        assertNull(loginWelcomeScreenStateContent(termsOfServiceUrl = null).validTermsOfServiceUrl)
        assertNull(loginWelcomeScreenStateContent(termsOfServiceUrl = EMPTY_STRING).validTermsOfServiceUrl)
        assertEquals(VALID_URL, loginWelcomeScreenStateContent(termsOfServiceUrl = VALID_URL).validTermsOfServiceUrl)
    }

    @Test
    fun content_hasPrivacyPolicy_and_hasTermsOfService_followValidUrls() {
        assertFalse(loginWelcomeScreenStateContent(privacyPolicyUrl = null).hasPrivacyPolicy)
        assertTrue(loginWelcomeScreenStateContent(privacyPolicyUrl = VALID_URL).hasPrivacyPolicy)

        assertFalse(loginWelcomeScreenStateContent(termsOfServiceUrl = null).hasTermsOfService)
        assertTrue(loginWelcomeScreenStateContent(termsOfServiceUrl = VALID_URL).hasTermsOfService)
    }

    private fun loginWelcomeScreenStateContent(
        privacyPolicyUrl: String? = null,
        termsOfServiceUrl: String? = null
    ): LoginWelcomeScreenState.Content = LoginWelcomeScreenState.Content(
        availableAuthProviders = AvailableAuthProviders(
            primary = listOf(UserAuthProvider.EMAIL),
            secondary = emptyList()
        ),
        privacyPolicyUrl = privacyPolicyUrl,
        termsOfServiceUrl = termsOfServiceUrl
    )

    private companion object {
        const val EMPTY_STRING = ""
        const val BLANK_ONLY = "   "
        const val VALID_URL = "https://example.com/policy"
    }
}
