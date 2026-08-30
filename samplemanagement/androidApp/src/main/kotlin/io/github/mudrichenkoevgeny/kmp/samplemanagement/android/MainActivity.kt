package io.github.mudrichenkoevgeny.kmp.samplemanagement.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.di.ManagementAppComponent
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.ui.root.RootContent

/**
 * Host activity: reads [ManagementAppComponent] from [AndroidApp] and sets Compose content to [RootContent].
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appComponent = (application as AndroidApp).managementAppComponent

        enableEdgeToEdge()
        setContent {
            RootContent(appComponent)
        }
    }
}