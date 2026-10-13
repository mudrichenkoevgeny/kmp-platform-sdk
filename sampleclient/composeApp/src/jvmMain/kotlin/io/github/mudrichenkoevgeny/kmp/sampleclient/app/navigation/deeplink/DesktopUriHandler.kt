package io.github.mudrichenkoevgeny.kmp.sampleclient.app.navigation.deeplink

import io.github.mudrichenkoevgeny.kmp.sampleclient.app.di.ClientAppComponent
import java.awt.Desktop

/**
 * Registers JVM Desktop OpenURIHandler to intercept native deep links and forward them to [ClientAppComponent].
 */
fun registerDesktopUriHandler(clientAppComponent: ClientAppComponent) {
    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.APP_OPEN_URI)) {
        Desktop.getDesktop().setOpenURIHandler { event ->
            clientAppComponent.handleDeepLink(event.uri.toString())
        }
    }
}
