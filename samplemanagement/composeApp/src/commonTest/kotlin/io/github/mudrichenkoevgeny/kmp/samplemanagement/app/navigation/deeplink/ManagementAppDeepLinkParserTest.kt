package io.github.mudrichenkoevgeny.kmp.samplemanagement.app.navigation.deeplink

import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.ManagementDestination
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.ProfileDestination
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.ktor.http.Url
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class ManagementAppDeepLinkParserTest {

    private val parser = ManagementAppDeepLinkParser()

    @Test
    fun `test valid management route parsing`() {
        val userId = UserId.generate().asHexDashString()
        val url = Url("https://kmp-platform.internal/management/users/$userId")
        val route = parser.parse(url)

        val managementRoute = assertIs<ManagementAppDeepLinkRoute.Management>(route)
        val userDetailDest = assertIs<ManagementDestination.UserDetail>(managementRoute.destination)
        assertEquals(userId, userDetailDest.userId.asHexDashString())
    }

    @Test
    fun `test valid profile route parsing`() {
        val url = Url("https://kmp-platform.internal/profile/totp")
        val route = parser.parse(url)

        val profileRoute = assertIs<ManagementAppDeepLinkRoute.Profile>(route)
        assertEquals(ProfileDestination.TotpMain(), profileRoute.destination)
    }

    @Test
    fun `test corrupted UUID falls back to parent list`() {
        val url = Url("https://kmp-platform.internal/management/users/invalid-uuid")
        val route = parser.parse(url)

        val managementRoute = assertIs<ManagementAppDeepLinkRoute.Management>(route)
        assertEquals(ManagementDestination.GlobalUserList, managementRoute.destination)
    }

    @Test
    fun `test query parameters parsing with URL decoding`() {
        val url = Url("https://kmp-platform.internal/management/audit?actorIds=actor%201&actorIds=actor%202")
        val route = parser.parse(url)

        val managementRoute = assertIs<ManagementAppDeepLinkRoute.Management>(route)
        val auditDest = assertIs<ManagementDestination.AuditEventList>(managementRoute.destination)
        assertEquals(listOf("actor 1", "actor 2"), auditDest.params.initialFilters?.actorIds)
    }

    @Test
    fun `test unknown route returns null`() {
        val url = Url("https://kmp-platform.internal/unknown/path")
        val route = parser.parse(url)

        assertNull(route)
    }
}
