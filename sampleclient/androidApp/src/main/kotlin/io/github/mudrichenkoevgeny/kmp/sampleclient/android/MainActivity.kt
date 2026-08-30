package io.github.mudrichenkoevgeny.kmp.sampleclient.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import io.github.mudrichenkoevgeny.kmp.sampleclient.app.di.ClientAppComponent
import io.github.mudrichenkoevgeny.kmp.sampleclient.app.ui.root.RootContent

/**
 * Host activity: reads [ClientAppComponent] from [AndroidApp] and sets Compose content to [RootContent].
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appComponent = (application as AndroidApp).clientAppComponent

        enableEdgeToEdge()
        setContent {
            RootContent(appComponent)
        }
    }
}