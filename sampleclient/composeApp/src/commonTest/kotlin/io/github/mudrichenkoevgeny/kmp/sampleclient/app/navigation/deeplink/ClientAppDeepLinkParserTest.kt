package io.github.mudrichenkoevgeny.kmp.sampleclient.app.navigation.deeplink

import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.ClientLoginDestination
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.ProfileDestination
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.ktor.http.Url
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class ClientAppDeepLinkParserTest {

    private val parser = ClientAppDeepLinkParser()

    @Test
    fun `test valid profile route parsing`() {
        val sessionId = UserSessionId.generate().asHexDashString()
        val url = Url("https://kmp-platform.internal/profile/sessions/$sessionId")
        val route = parser.parse(url)

        val profileRoute = assertIs<ClientAppDeepLinkRoute.Profile>(route)
        val sessionDetailDest = assertIs<ProfileDestination.SessionDetail>(profileRoute.destination)
        assertEquals(sessionId, sessionDetailDest.sessionId.asHexDashString())
    }

    @Test
    fun `test phone login deeplink parsing with query param`() {
        val url = Url("https://kmp-platform.internal/login/phone?phone=%2B1234567890")
        val route = parser.parse(url)

        val loginRoute = assertIs<ClientAppDeepLinkRoute.Login>(route)
        val phoneDest = assertIs<ClientLoginDestination.LoginByPhone>(loginRoute.destination)
        assertEquals("+1234567890", phoneDest.params.phone)
        assertEquals(false, phoneDest.params.isConfirmStep)
    }

    @Test
    fun `test phone login confirm step deeplink parsing`() {
        val url = Url("https://kmp-platform.internal/login/phone/confirm?phone=%2B1234567890")
        val route = parser.parse(url)

        val loginRoute = assertIs<ClientAppDeepLinkRoute.Login>(route)
        val phoneDest = assertIs<ClientLoginDestination.LoginByPhone>(loginRoute.destination)
        assertEquals("+1234567890", phoneDest.params.phone)
        assertEquals(true, phoneDest.params.isConfirmStep)
    }

    @Test
    fun `test email registration deeplink parsing with query param`() {
        val url = Url("https://kmp-platform.internal/register/email?email=user%40example.com")
        val route = parser.parse(url)

        val loginRoute = assertIs<ClientAppDeepLinkRoute.Login>(route)
        val regDest = assertIs<ClientLoginDestination.RegistrationByEmail>(loginRoute.destination)
        assertEquals("user@example.com", regDest.params.email)
        assertEquals(false, regDest.params.isConfirmStep)
    }

    @Test
    fun `test email registration confirm step deeplink parsing`() {
        val url = Url("https://kmp-platform.internal/register/email/confirm?email=user%40example.com")
        val route = parser.parse(url)

        val loginRoute = assertIs<ClientAppDeepLinkRoute.Login>(route)
        val regDest = assertIs<ClientLoginDestination.RegistrationByEmail>(loginRoute.destination)
        assertEquals("user@example.com", regDest.params.email)
        assertEquals(true, regDest.params.isConfirmStep)
    }

    @Test
    fun `test corrupted UUID falls back to parent list`() {
        val url = Url("https://kmp-platform.internal/profile/sessions/corrupted-session-id")
        val route = parser.parse(url)

        val profileRoute = assertIs<ClientAppDeepLinkRoute.Profile>(route)
        assertEquals(ProfileDestination.Sessions, profileRoute.destination)
    }

    @Test
    fun `test unknown route returns null`() {
        val url = Url("https://kmp-platform.internal/management/settings")
        val route = parser.parse(url)

        assertNull(route)
    }
}
