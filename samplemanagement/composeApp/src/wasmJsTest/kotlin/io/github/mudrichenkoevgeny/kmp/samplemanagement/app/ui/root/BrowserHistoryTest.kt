package io.github.mudrichenkoevgeny.kmp.samplemanagement.app.ui.root

import io.github.mudrichenkoevgeny.shared.foundation.core.common.navigation.route.toFullUrl
import io.github.mudrichenkoevgeny.shared.foundation.feature.managementuser.navigation.route.management.ManagementMainNavigation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BrowserHistoryTest {

    @Test
    fun getAncestorPaths_returnsCorrectBreadcrumbAncestors() {
        val rootAncestors = getAncestorPaths("/")
        assertEquals(emptyList(), rootAncestors)

        val managementAncestors = getAncestorPaths("/management")
        assertEquals(listOf("/"), managementAncestors)

        val auditDetailAncestors = getAncestorPaths("/management/audit/123-xyz")
        assertEquals(listOf("/", "/management", "/management/audit"), auditDetailAncestors)

        val queryAndHashAncestors = getAncestorPaths("/management/users/456-abc?filter=active#section")
        assertEquals(listOf("/", "/management", "/management/users"), queryAndHashAncestors)
    }

    @Test
    fun isTopLevelRootPath_correctlyIdentifiesManagementRootPaths() {
        assertTrue(isTopLevelRootPath("/"))
        assertTrue(isTopLevelRootPath("/management"))
        assertTrue(isTopLevelRootPath("/user"))
        assertTrue(isTopLevelRootPath("/management?tab=overview"))
        assertFalse(isTopLevelRootPath("/management/audit/123"))
    }

    @Test
    fun formatCanonicalPath_formatsRouteUrlToCanonicalBrowserPath() {
        val baseUrl = "https://kmp-platform.internal"
        val route = ManagementMainNavigation.Main
        val fullUrl = route.toFullUrl(baseUrl)

        val canonicalPath = formatCanonicalPath(fullUrl)
        assertEquals("/management", canonicalPath)
    }
}
