package io.github.mudrichenkoevgeny.kmp.samplemanagement.app.navigation.deeplink

import io.github.mudrichenkoevgeny.shared.foundation.core.audit.navigation.route.management.ManagementAuditNavigation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.route.client.ClientUserNavigation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.route.management.ManagementUsersNavigation
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
        val userDetailDest = assertIs<ManagementUsersNavigation.UserDetail>(managementRoute.destination)
        assertEquals(userId, userDetailDest.params.userId)
    }

    @Test
    fun `test valid user route parsing`() {
        val url = Url("https://kmp-platform.internal/user/totp")
        val route = parser.parse(url)

        val userRoute = assertIs<ManagementAppDeepLinkRoute.User>(route)
        assertEquals(ClientUserNavigation.TotpMain(), userRoute.destination)
    }

    @Test
    fun `test corrupted UUID falls back to parent list`() {
        val url = "https://kmp-platform.internal/management/users/invalid-uuid"
        val route = parser.parse(Url(url))

        val managementRoute = assertIs<ManagementAppDeepLinkRoute.Management>(route)
        assertEquals(ManagementUsersNavigation.UserList(), managementRoute.destination)
    }

    @Test
    fun `test query parameters parsing with URL decoding`() {
        val url = Url("https://kmp-platform.internal/management/audit?actor_id=actor%201&actor_id=actor%202")
        val route = parser.parse(url)

        val managementRoute = assertIs<ManagementAppDeepLinkRoute.Management>(route)
        val auditDest = assertIs<ManagementAuditNavigation.AuditEventList>(managementRoute.destination)
        assertEquals(listOf("actor 1", "actor 2"), auditDest.params.actorIds)
    }

    @Test
    fun `test unknown route returns null`() {
        val url = Url("https://kmp-platform.internal/unknown/path")
        val route = parser.parse(url)

        assertNull(route)
    }
}
