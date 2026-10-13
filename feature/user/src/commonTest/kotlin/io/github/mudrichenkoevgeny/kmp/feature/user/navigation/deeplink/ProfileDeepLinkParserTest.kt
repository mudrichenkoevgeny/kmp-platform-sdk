package io.github.mudrichenkoevgeny.kmp.feature.user.navigation.deeplink

import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.ProfileDestination
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.ktor.http.Url
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class ProfileDeepLinkParserTest {

    private val parser = ProfileDeepLinkParser()
    private val formatter = ProfileUrlFormatter()

    @Test
    fun `test format and parse for Main`() {
        val dest = ProfileDestination.Main
        val urlString = formatter.format(dest)
        assertEquals("https://kmp-platform.internal/profile", urlString)
        val parsed = parser.parse(Url(urlString))
        assertEquals(dest, parsed)
    }

    @Test
    fun `test format and parse for SessionDetail`() {
        val sessionIdStr = UserSessionId.generate().asHexDashString()
        val urlString = "https://kmp-platform.internal/profile/sessions/$sessionIdStr"
        val parsed = parser.parse(Url(urlString))
        val sessionDetailDest = assertIs<ProfileDestination.SessionDetail>(parsed)
        assertEquals(sessionIdStr, sessionDetailDest.sessionId.asHexDashString())
        assertEquals(urlString, formatter.format(sessionDetailDest))
    }

    @Test
    fun `test corrupted UUID falls back to parent list`() {
        val urlString = "https://kmp-platform.internal/profile/sessions/invalid-uuid"
        val parsed = parser.parse(Url(urlString))
        assertEquals(ProfileDestination.Sessions, parsed)
    }

    @Test
    fun `test format and parse for TotpMain setup`() {
        val token = "test-mfa-token-123"
        val urlString = "https://kmp-platform.internal/profile/totp/setup?token=$token"
        val parsed = parser.parse(Url(urlString))
        val totpDest = assertIs<ProfileDestination.TotpMain>(parsed)
        assertEquals(token, totpDest.params.token)
    }

    @Test
    fun `test unrecognized path returns null if not profile`() {
        val urlString = "https://kmp-platform.internal/management"
        assertNull(parser.parse(Url(urlString)))
    }
}
