package io.github.mudrichenkoevgeny.kmp.feature.managementuser.navigation.deeplink

import io.github.mudrichenkoevgeny.shared.foundation.core.audit.navigation.route.management.ManagementAuditNavigation
import io.github.mudrichenkoevgeny.shared.foundation.core.common.navigation.route.management.ManagementMainNavigation
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.navigation.route.management.ManagementSettingsNavigation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.route.management.ManagementAuthSettingsNavigation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.route.management.ManagementSelfUserNavigation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.route.management.ManagementUsersNavigation
import io.ktor.http.Url
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class ManagementUserDeepLinkParserTest {

    private val parser = ManagementUserDeepLinkParser()
    private val formatter = ManagementUrlFormatter("https://kmp-platform.internal")

    @Test
    fun `test format and parse for Main`() {
        val dest = ManagementMainNavigation.Main
        val urlString = formatter.format(dest)
        assertEquals("https://kmp-platform.internal", urlString)
        val parsed = parser.parse(Url(urlString))
        assertEquals(dest, parsed)
    }

    @Test
    fun `test format and parse for UserDetail`() {
        val userIdStr = UserId.generate().asHexDashString()
        val urlString = "https://kmp-platform.internal/management/users/$userIdStr"
        val parsed = parser.parse(Url(urlString))
        val userDetailDest = assertIs<ManagementUsersNavigation.UserDetail>(parsed)
        assertEquals(userIdStr, userDetailDest.params.userId)
    }

    @Test
    fun `test corrupted UUID falls back to parent list`() {
        val urlString = "https://kmp-platform.internal/management/users/invalid-uuid"
        val parsed = parser.parse(Url(urlString))
        assertEquals(ManagementUsersNavigation.UserList(), parsed)
    }

    @Test
    fun `test query parameters with URL encoding for AuditEventList`() {
        val urlString = "https://kmp-platform.internal/management/audit?actor_id=actor%201&actor_id=actor%202"
        val parsed = parser.parse(Url(urlString))
        val auditDest = assertIs<ManagementAuditNavigation.AuditEventList>(parsed)
        assertEquals(listOf("actor 1", "actor 2"), auditDest.params.actorIds)
    }

    @Test
    fun `test settings path parsing`() {
        val urlString = "https://kmp-platform.internal/management/settings/auth"
        val parsed = parser.parse(Url(urlString))
        assertIs<ManagementAuthSettingsNavigation.AuthSettings>(parsed)
    }

    @Test
    fun `test settings hub and global settings parsing`() {
        val hubUrl = "https://kmp-platform.internal/management/settings"
        val hubParsed = parser.parse(Url(hubUrl))
        assertIs<ManagementSettingsNavigation.Main>(hubParsed)

        val globalUrl = "https://kmp-platform.internal/management/settings/global"
        val globalParsed = parser.parse(Url(globalUrl))
        assertIs<ManagementSettingsNavigation.GlobalSettings>(globalParsed)
    }

    @Test
    fun `test staff self-service user profile parsing`() {
        val selfUrl = "https://kmp-platform.internal/user"
        val selfParsed = parser.parse(Url(selfUrl))
        assertIs<ManagementSelfUserNavigation.Main>(selfParsed)

        val sessionsUrl = "https://kmp-platform.internal/user/sessions"
        val sessionsParsed = parser.parse(Url(sessionsUrl))
        assertIs<ManagementSelfUserNavigation.SessionList>(sessionsParsed)
    }
}
