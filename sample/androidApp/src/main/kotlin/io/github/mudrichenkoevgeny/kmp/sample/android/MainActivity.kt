package io.github.mudrichenkoevgeny.kmp.sample.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import io.github.mudrichenkoevgeny.kmp.sample.app.ui.root.RootContent

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