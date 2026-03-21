package io.github.mudrichenkoevgeny.kmp.core.common.platform.externallauncher

/**
 * Platform abstraction for opening external resources.
 *
 * SDK/UI code should depend on this interface instead of platform-specific APIs.
 */
interface ExternalLauncher {
    /**
     * Opens a web URL (for example, in the system browser).
     *
     * @param url URL to open.
     */
    fun openUrl(url: String)

    /**
     * Opens an email draft in the mail client.
     *
     * @param email Recipient address.
     * @param subject Optional subject.
     * @param body Optional body.
     */
    fun openMail(email: String, subject: String? = null, body: String? = null)

    /**
     * Opens a file/resource represented by a URL.
     *
     * @param url Location of the file or resource to open.
     */
    fun openFile(url: String)
}