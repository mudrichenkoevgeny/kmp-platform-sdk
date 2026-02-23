package io.github.mudrichenkoevgeny.kmp.core.common.platform.externallauncher

interface ExternalLauncher {
    fun openUrl(url: String)
    fun openMail(email: String, subject: String? = null, body: String? = null)
    fun openFile(url: String)
}