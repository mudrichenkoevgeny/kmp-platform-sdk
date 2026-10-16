package io.github.mudrichenkoevgeny.kmp.feature.user.navigation.deeplink

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.totp.TotpStep
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.route.client.ClientUserNavigation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.user.session.detail.SessionDetailParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.user.totp.main.TotpMainParams
import io.ktor.http.Url
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

@InternalApi
class UserDeepLinkParserTest {

    private val parser = UserDeepLinkParser()
    private val formatter = UserUrlFormatter("https://kmp-platform.internal")

    @Test
    fun format_and_parse_forMain() {
        val dest = ClientUserNavigation.Main
        val urlString = formatter.format(dest)
        assertEquals("https://kmp-platform.internal/user", urlString)
        val parsed = parser.parse(Url(urlString))
        assertEquals(dest, parsed)
    }

    @Test
    fun format_and_parse_forSessionDetail() {
        val sessionIdStr = UserSessionId.generate().asHexDashString()
        val dest = ClientUserNavigation.SessionDetail(SessionDetailParams(sessionId = sessionIdStr))
        val urlString = formatter.format(dest)
        val parsed = parser.parse(Url(urlString))
        val sessionDetailDest = assertIs<ClientUserNavigation.SessionDetail>(parsed)
        assertEquals(sessionIdStr, sessionDetailDest.params.sessionId)
    }

    @Test
    fun parse_corruptedUuid_fallsBackToParentList() {
        val urlString = "https://kmp-platform.internal/user/sessions/invalid-uuid"
        val parsed = parser.parse(Url(urlString))
        assertEquals(ClientUserNavigation.SessionList(), parsed)
    }

    @Test
    fun format_and_parse_forTotpMainSetup() {
        val dest = ClientUserNavigation.TotpMain(TotpMainParams(totpStep = TotpStep.SETUP))
        val urlString = formatter.format(dest)
        val parsed = parser.parse(Url(urlString))
        val totpDest = assertIs<ClientUserNavigation.TotpMain>(parsed)
        assertEquals(TotpStep.SETUP, totpDest.params.totpStep)
    }

    @Test
    fun parse_unrecognizedPath_returnsNull() {
        val urlString = "https://kmp-platform.internal/management"
        assertNull(parser.parse(Url(urlString)))
    }
}
