package io.github.mudrichenkoevgeny.kmp.core.common.mock.platform.externallauncher

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.platform.externallauncher.ExternalLauncher

@InternalApi
class ExternalLauncherMock : ExternalLauncher {
    val openedUrls = mutableListOf<String>()

    override fun openUrl(url: String) {
        openedUrls.add(url)
    }

    override fun openMail(email: String, subject: String?, body: String?) = Unit

    override fun openFile(url: String) = Unit
}