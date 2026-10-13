package io.github.mudrichenkoevgeny.kmp.samplemanagement.android

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.di.ManagementAppComponent
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.navigation.deeplink.ManagementAppDeepLinkParser
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.navigation.deeplink.ManagementAppDeepLinkRoute
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.ui.root.RootContent
import io.ktor.http.Url

/**
 * Host activity: reads [ManagementAppComponent] from [AndroidApp] and sets Compose content to [RootContent].
 */
class MainActivity : ComponentActivity() {
    private val deepLinkParser = ManagementAppDeepLinkParser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appComponent = (application as AndroidApp).managementAppComponent

        var initialRoute: ManagementAppDeepLinkRoute? = null
        intent.dataString?.let { urlString ->
            initialRoute = deepLinkParser.parse(Url(urlString))
        }
        intent.data = null

        enableEdgeToEdge()
        setContent {
            RootContent(
                managementAppComponent = appComponent,
                initialRoute = initialRoute
            )
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val appComponent = (application as AndroidApp).managementAppComponent

        intent.dataString?.let { urlString ->
            val route = deepLinkParser.parse(Url(urlString))
            if (route != null) {
                appComponent.mainScreenComponent?.handleDeepLink(route)
            }
        }
    }
}
