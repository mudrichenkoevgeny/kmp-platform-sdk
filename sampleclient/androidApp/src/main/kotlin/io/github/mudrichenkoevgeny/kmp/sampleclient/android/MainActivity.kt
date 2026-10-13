package io.github.mudrichenkoevgeny.kmp.sampleclient.android

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import io.github.mudrichenkoevgeny.kmp.sampleclient.app.di.ClientAppComponent
import io.github.mudrichenkoevgeny.kmp.sampleclient.app.navigation.deeplink.ClientAppDeepLinkParser
import io.github.mudrichenkoevgeny.kmp.sampleclient.app.navigation.deeplink.ClientAppDeepLinkRoute
import io.github.mudrichenkoevgeny.kmp.sampleclient.app.ui.root.RootContent
import io.ktor.http.Url

/**
 * Host activity: reads [ClientAppComponent] from [AndroidApp] and sets Compose content to [RootContent].
 */
class MainActivity : ComponentActivity() {
    private val deepLinkParser = ClientAppDeepLinkParser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appComponent = (application as AndroidApp).clientAppComponent

        var initialRoute: ClientAppDeepLinkRoute? = null
        intent.dataString?.let { urlString ->
            initialRoute = deepLinkParser.parse(Url(urlString))
        }
        intent.data = null

        enableEdgeToEdge()
        setContent {
            RootContent(
                clientAppComponent = appComponent,
                initialRoute = initialRoute
            )
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val appComponent = (application as AndroidApp).clientAppComponent

        intent.dataString?.let { urlString ->
            val route = deepLinkParser.parse(Url(urlString))
            if (route != null) {
                appComponent.mainScreenComponent?.handleDeepLink(route)
            }
        }
    }
}
