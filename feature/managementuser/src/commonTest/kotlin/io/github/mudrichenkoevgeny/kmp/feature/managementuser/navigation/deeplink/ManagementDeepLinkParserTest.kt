package io.github.mudrichenkoevgeny.kmp.feature.managementuser.navigation.deeplink

import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.ManagementDestination
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.ktor.http.Url
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class ManagementDeepLinkParserTest {

    private val parser = ManagementDeepLinkParser()
    private val formatter = ManagementUrlFormatter()

    @Test
    fun `test format and parse for Main`() {
        val dest = ManagementDestination.Main
        val urlString = formatter.format(dest)
        assertEquals("https://kmp-platform.internal/management", urlString)
        val parsed = parser.parse(Url(urlString))
        assertEquals(dest, parsed)
    }

    @Test
    fun `test format and parse for UserDetail`() {
        val userIdStr = UserId.generate().asHexDashString()
        val urlString = "https://kmp-platform.internal/management/users/$userIdStr"
        val parsed = parser.parse(Url(urlString))
        val userDetailDest = assertIs<ManagementDestination.UserDetail>(parsed)
        assertEquals(userIdStr, userDetailDest.userId.asHexDashString())
        assertEquals(urlString, formatter.format(userDetailDest))
    }

    @Test
    fun `test corrupted UUID falls back to parent list`() {
        val urlString = "https://kmp-platform.internal/management/users/invalid-uuid"
        val parsed = parser.parse(Url(urlString))
        assertEquals(ManagementDestination.GlobalUserList, parsed)
    }

    @Test
    fun `test query parameters with URL encoding for AuditEventList`() {
        val urlString = "https://kmp-platform.internal/management/audit?actorIds=actor%201&actorIds=actor%202"
        val parsed = parser.parse(Url(urlString))
        val auditDest = assertIs<ManagementDestination.AuditEventList>(parsed)
        assertEquals(listOf("actor 1", "actor 2"), auditDest.params.initialFilters?.actorIds)
    }

    @Test
    fun `test unrecognized path returns null if not management`() {
        val urlString = "https://kmp-platform.internal/profile"
        assertNull(parser.parse(Url(urlString)))
    }

    @Test
    fun `test settings dialog query parameter parsing`() {
        val urlString = "https://kmp-platform.internal/management/settings/auth?dialog=reset-confirm"
        val parsed = parser.parse(Url(urlString))
        val authDest = assertIs<ManagementDestination.EditAuthSettings>(parsed)
        assertEquals("reset-confirm", authDest.params.dialog)
    }
}
