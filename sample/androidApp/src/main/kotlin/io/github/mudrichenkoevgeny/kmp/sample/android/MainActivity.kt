package io.github.mudrichenkoevgeny.kmp.sample.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import io.github.mudrichenkoevgeny.kmp.sample.app.di.AppComponent
import io.github.mudrichenkoevgeny.kmp.sample.app.ui.root.RootContent

/**
 * Host activity: reads [AppComponent] from [AndroidApp] and sets Compose content to [RootContent].
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appComponent = (application as AndroidApp).appComponent

        enableEdgeToEdge()
        setContent {
            RootContent(appComponent)
        }
    }
}