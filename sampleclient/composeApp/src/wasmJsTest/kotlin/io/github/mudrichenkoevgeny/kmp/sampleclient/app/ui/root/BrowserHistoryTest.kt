package io.github.mudrichenkoevgeny.kmp.sampleclient.app.ui.root

import io.github.mudrichenkoevgeny.shared.foundation.core.common.navigation.route.toFullUrl
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.route.client.ClientUserNavigation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.user.session.detail.SessionDetailParams
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BrowserHistoryTest {

    @Test
    fun getAncestorPaths_returnsCorrectBreadcrumbAncestors() {
        val rootAncestors = getAncestorPaths("/")
        assertEquals(emptyList(), rootAncestors)

        val userAncestors = getAncestorPaths("/user")
        assertEquals(listOf("/"), userAncestors)

        val sessionDetailAncestors = getAncestorPaths("/user/sessions/123-abc")
        assertEquals(listOf("/", "/user", "/user/sessions"), sessionDetailAncestors)

        val queryAndHashAncestors = getAncestorPaths("/user/sessions/123-abc?tab=details#top")
        assertEquals(listOf("/", "/user", "/user/sessions"), queryAndHashAncestors)
    }

    @Test
    fun isTopLevelRootPath_correctlyIdentifiesRootPaths() {
        assertTrue(isTopLevelRootPath("/"))
        assertTrue(isTopLevelRootPath("/user"))
        assertTrue(isTopLevelRootPath("/user?param=1"))
        assertFalse(isTopLevelRootPath("/user/sessions"))
        assertFalse(isTopLevelRootPath("/settings"))
    }

    @Test
    fun formatCanonicalPath_formatsRouteUrlToCanonicalBrowserPath() {
        val baseUrl = "https://kmp-platform.internal"
        val route = ClientUserNavigation.SessionDetail(
            SessionDetailParams(sessionId = "00000000-0000-0000-0000-000000000001")
        )
        val fullUrl = route.toFullUrl(baseUrl)

        val canonicalPath = formatCanonicalPath(fullUrl)
        assertEquals("/user/sessions/00000000-0000-0000-0000-000000000001", canonicalPath)
    }
}
