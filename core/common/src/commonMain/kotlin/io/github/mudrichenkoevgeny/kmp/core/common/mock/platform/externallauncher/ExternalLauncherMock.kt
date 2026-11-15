package io.github.mudrichenkoevgeny.kmp.core.common.mock.platform.externallauncher

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.platform.externallauncher.ExternalLauncher

/**
 * Mock implementation of [ExternalLauncher] for testing and previews.
 */
@InternalApi
public class ExternalLauncherMock : ExternalLauncher {
    public val openedUrls: MutableList<String> = mutableListOf()

    override fun openUrl(url: String) {
        openedUrls.add(url)
    }

    override fun openMail(
        email: String,
        subject: String?,
        body: String?
    ): Unit = Unit

    override fun openFile(url: String): Unit = Unit
}
