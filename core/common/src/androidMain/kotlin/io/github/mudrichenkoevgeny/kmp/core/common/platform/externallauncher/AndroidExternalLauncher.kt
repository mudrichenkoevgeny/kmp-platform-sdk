package io.github.mudrichenkoevgeny.kmp.core.common.platform.externallauncher

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import io.github.mudrichenkoevgeny.kmp.core.common.platform.filesystem.AndroidFileOpener

class AndroidExternalLauncher(
    private val context: Context,
    private val fileOpener: AndroidFileOpener
) : ExternalLauncher {

    override fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri()).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    override fun openMail(email: String, subject: String?, body: String?) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = MAIL_TO_DATA.toUri()
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            subject?.let { putExtra(Intent.EXTRA_SUBJECT, it) }
            body?.let { putExtra(Intent.EXTRA_TEXT, it) }
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    override fun openFile(url: String) {
        val isWebResource = url.startsWith(URL_HTTP, ignoreCase = true) ||
                url.startsWith(URL_HTTPS, ignoreCase = true)

        if (isWebResource) {
            openUrl(url)
        } else {
            fileOpener.openLocalFile(url)
        }
    }

    companion object {
        const val MAIL_TO_DATA = "mailto:"
        const val URL_HTTP = "http://"
        const val URL_HTTPS = "https://"
    }
}