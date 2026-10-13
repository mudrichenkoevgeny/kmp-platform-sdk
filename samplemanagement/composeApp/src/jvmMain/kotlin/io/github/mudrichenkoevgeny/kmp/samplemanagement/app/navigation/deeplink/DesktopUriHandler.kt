package io.github.mudrichenkoevgeny.kmp.samplemanagement.app.navigation.deeplink

import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.di.ManagementAppComponent
import java.awt.Desktop

/**
 * Registers JVM Desktop OpenURIHandler to intercept native deep links and forward them to [ManagementAppComponent].
 */
fun registerDesktopUriHandler(managementAppComponent: ManagementAppComponent) {
    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.APP_OPEN_URI)) {
        Desktop.getDesktop().setOpenURIHandler { event ->
            managementAppComponent.handleDeepLink(event.uri.toString())
        }
    }
}
