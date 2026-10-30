package io.github.mudrichenkoevgeny.kmp.feature.clientuser.navigation.deeplink

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.route.client.ClientAuthNavigation
import io.ktor.http.Url
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

@InternalApi
class ClientUserDeepLinkParserTest {

    private val parser = ClientUserDeepLinkParser()
    private val formatter = ClientUrlFormatter("https://kmp-platform.internal")

    @Test
    fun parse_returnsMain_forLoginWelcomeRoute() {
        val url = Url("https://kmp-platform.internal/auth/login")
        val result = parser.parse(url)

        assertIs<ClientAuthNavigation.Main>(result)
    }

    @Test
    fun parse_returnsLoginByEmail_withParsedQuery() {
        val url = Url("https://kmp-platform.internal/auth/login/email?email=test%40example.com")
        val result = parser.parse(url)

        val emailDest = assertIs<ClientAuthNavigation.LoginByEmail>(result)
        assertEquals("test@example.com", emailDest.params.email)
    }

    @Test
    fun parse_returnsLoginByPhone_withParsedQuery() {
        val url = Url("https://kmp-platform.internal/auth/login/phone?phone_number=%2B1234567890")
        val result = parser.parse(url)

        val phoneDest = assertIs<ClientAuthNavigation.LoginByPhone>(result)
        assertEquals("+1234567890", phoneDest.params.phone)
    }

    @Test
    fun parse_returnsRegistration_withParsedQuery() {
        val url = Url("https://kmp-platform.internal/auth/registration?email=reg%40example.com")
        val result = parser.parse(url)

        val regDest = assertIs<ClientAuthNavigation.Registration>(result)
        assertEquals("reg@example.com", regDest.params.email)
    }

    @Test
    fun parse_returnsResetPassword_forResetPasswordRoute() {
        val url = Url("https://kmp-platform.internal/auth/reset-password?email=reset%40example.com")
        val result = parser.parse(url)

        val resetDest = assertIs<ClientAuthNavigation.ResetPassword>(result)
        assertEquals("reset@example.com", resetDest.params.email)
    }

    @Test
    fun parse_returnsLoginByTotp_withMfaToken() {
        val url = Url("https://kmp-platform.internal/auth/login/totp?mfa_token=tok123")
        val result = parser.parse(url)

        val totpDest = assertIs<ClientAuthNavigation.LoginByTotp>(result)
        assertEquals("tok123", totpDest.params.mfaToken)
    }

    @Test
    fun parse_returnsPendingDeletion_forPendingDeletionRoute() {
        val url = Url("https://kmp-platform.internal/auth/login/pending-deletion")
        val result = parser.parse(url)

        assertIs<ClientAuthNavigation.PendingDeletion>(result)
    }

    @Test
    fun parse_returnsUnlockMethodSelection_forUnlockRoute() {
        val url = Url("https://kmp-platform.internal/auth/unlock")
        val result = parser.parse(url)

        assertIs<ClientAuthNavigation.UnlockMethodSelection>(result)
    }

    @Test
    fun parse_returnsNull_forUnknownRoute() {
        val url = Url("https://kmp-platform.internal/unknown/route")
        val result = parser.parse(url)

        assertNull(result)
    }

    @Test
    fun format_returnsFullUrl_forDestination() {
        val dest = ClientAuthNavigation.LoginByEmail()
        val formatted = formatter.format(dest)

        assertEquals("https://kmp-platform.internal/auth/login/email", formatted)
    }
}
