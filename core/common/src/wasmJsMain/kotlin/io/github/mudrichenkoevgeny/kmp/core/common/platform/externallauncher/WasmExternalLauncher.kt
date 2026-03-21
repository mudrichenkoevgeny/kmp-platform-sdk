package io.github.mudrichenkoevgeny.kmp.core.common.platform.externallauncher

import kotlinx.browser.window

/**
 * Host `encodeURIComponent` binding for building `mailto:` query segments (subject/body) safely.
 *
 * @param s Raw string to encode for use in a URI component.
 */
external fun encodeURIComponent(s: String): String

/**
 * Wasm/JS [ExternalLauncher] that uses [window.open] (`_blank` for generic URLs, `_self` for `mailto:`).
 *
 * [openFile] delegates to [openUrl] because the browser cannot open arbitrary host filesystem paths like Android.
 */
class WasmExternalLauncher : ExternalLauncher {

    override fun openUrl(url: String) {
        window.open(url, target = TARGET_BLANK)
    }

    override fun openMail(email: String, subject: String?, body: String?) {
        val mailto = StringBuilder("$MAIL_TO_DATA$email")

        val params = mutableListOf<String>()
        subject?.let { params.add("$PARAM_SUBJECT=${encodeURIComponent(it)}") }
        body?.let { params.add("$PARAM_BODY=${encodeURIComponent(it)}") }

        if (params.isNotEmpty()) {
            mailto.append(QUERY_SEPARATOR).append(params.joinToString(PARAM_SEPARATOR))
        }

        window.open(mailto.toString(), target = TARGET_SELF)
    }

    override fun openFile(url: String) {
        openUrl(url)
    }

    companion object {
        private const val MAIL_TO_DATA = "mailto:"
        private const val TARGET_BLANK = "_blank"
        private const val TARGET_SELF = "_self"

        private const val PARAM_SUBJECT = "subject"
        private const val PARAM_BODY = "body"

        private const val QUERY_SEPARATOR = "?"
        private const val PARAM_SEPARATOR = "&"
    }
}