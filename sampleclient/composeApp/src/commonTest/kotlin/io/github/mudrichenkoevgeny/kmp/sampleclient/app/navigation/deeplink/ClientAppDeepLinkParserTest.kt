package io.github.mudrichenkoevgeny.kmp.sampleclient.app.navigation.deeplink

import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.route.client.ClientAuthNavigation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.route.client.ClientUserNavigation
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
        val url = Url("https://kmp-platform.internal/user/sessions/$sessionId")
        val route = parser.parse(url)

        val userRoute = assertIs<ClientAppDeepLinkRoute.User>(route)
        val sessionDetailDest = assertIs<ClientUserNavigation.SessionDetail>(userRoute.destination)
        assertEquals(sessionId, sessionDetailDest.params.sessionId)
    }

    @Test
    fun `test welcome login deeplink parsing`() {
        val url = Url("https://kmp-platform.internal/auth/login")
        val route = parser.parse(url)

        val loginRoute = assertIs<ClientAppDeepLinkRoute.Login>(route)
        assertIs<ClientAuthNavigation.Main>(loginRoute.destination)
    }

    @Test
    fun `test phone login deeplink parsing with query param`() {
        val url = Url("https://kmp-platform.internal/auth/login/phone?phone_number=%2B1234567890")
        val route = parser.parse(url)

        val loginRoute = assertIs<ClientAppDeepLinkRoute.Login>(route)
        val phoneDest = assertIs<ClientAuthNavigation.LoginByPhone>(loginRoute.destination)
        assertEquals("+1234567890", phoneDest.params.phone)
    }

    @Test
    fun `test email registration deeplink parsing with query param`() {
        val url = Url("https://kmp-platform.internal/auth/registration?email=user%40example.com")
        val route = parser.parse(url)

        val loginRoute = assertIs<ClientAppDeepLinkRoute.Login>(route)
        val regDest = assertIs<ClientAuthNavigation.Registration>(loginRoute.destination)
        assertEquals("user@example.com", regDest.params.email)
    }

    @Test
    fun `test corrupted UUID falls back to parent list`() {
        val url = Url("https://kmp-platform.internal/user/sessions/corrupted-session-id")
        val route = parser.parse(url)

        val userRoute = assertIs<ClientAppDeepLinkRoute.User>(route)
        assertEquals(ClientUserNavigation.SessionList(), userRoute.destination)
    }

    @Test
    fun `test unknown route returns null`() {
        val url = Url("https://kmp-platform.internal/management/settings")
        val route = parser.parse(url)

        assertNull(route)
    }
}
