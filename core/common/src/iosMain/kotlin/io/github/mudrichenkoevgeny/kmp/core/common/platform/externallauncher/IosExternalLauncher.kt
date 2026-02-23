package io.github.mudrichenkoevgeny.kmp.core.common.platform.externallauncher

import platform.Foundation.NSURL
import platform.Foundation.NSURLComponents
import platform.Foundation.NSURLQueryItem
import platform.UIKit.UIApplication

class IosExternalLauncher : ExternalLauncher {

    override fun openUrl(url: String) {
        val nsUrl = NSURL.URLWithString(url)
        if (nsUrl != null) {
            UIApplication.sharedApplication.openURL(nsUrl)
        }
    }

    override fun openMail(email: String, subject: String?, body: String?) {
        val components = NSURLComponents.componentsWithString(MAIL_TO_DATA + email)

        val queryItems = mutableListOf<NSURLQueryItem>()
        subject?.let { queryItems.add(NSURLQueryItem(name = PARAM_SUBJECT, value = it)) }
        body?.let { queryItems.add(NSURLQueryItem(name = PARAM_BODY, value = it)) }

        if (queryItems.isNotEmpty()) {
            components?.setQueryItems(queryItems)
        }

        components?.URL?.let {
            UIApplication.sharedApplication.openURL(it)
        }
    }

    override fun openFile(url: String) {
        openUrl(url)
    }

    companion object {
        private const val MAIL_TO_DATA = "mailto:"
        private const val PARAM_SUBJECT = "subject"
        private const val PARAM_BODY = "body"
    }
}