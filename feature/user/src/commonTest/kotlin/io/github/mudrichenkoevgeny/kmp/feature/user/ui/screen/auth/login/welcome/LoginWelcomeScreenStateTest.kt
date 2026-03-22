package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.welcome

import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.settings.AvailableAuthProviders
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserAuthProvider
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LoginWelcomeScreenStateTest {

    @Test
    fun content_validPrivacyPolicyUrl_trimsBlankToNull() {
        assertNull(content(privacyPolicyUrl = null).validPrivacyPolicyUrl)
        assertNull(content(privacyPolicyUrl = EMPTY_STRING).validPrivacyPolicyUrl)
        assertNull(content(privacyPolicyUrl = BLANK_ONLY).validPrivacyPolicyUrl)
        assertEquals(VALID_URL, content(privacyPolicyUrl = VALID_URL).validPrivacyPolicyUrl)
    }

    @Test
    fun content_validTermsOfServiceUrl_trimsBlankToNull() {
        assertNull(content(termsOfServiceUrl = null).validTermsOfServiceUrl)
        assertNull(content(termsOfServiceUrl = EMPTY_STRING).validTermsOfServiceUrl)
        assertEquals(VALID_URL, content(termsOfServiceUrl = VALID_URL).validTermsOfServiceUrl)
    }

    @Test
    fun content_hasPrivacyPolicy_and_hasTermsOfService_followValidUrls() {
        assertFalse(content(privacyPolicyUrl = null).hasPrivacyPolicy)
        assertTrue(content(privacyPolicyUrl = VALID_URL).hasPrivacyPolicy)

        assertFalse(content(termsOfServiceUrl = null).hasTermsOfService)
        assertTrue(content(termsOfServiceUrl = VALID_URL).hasTermsOfService)
    }

    private fun content(
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
