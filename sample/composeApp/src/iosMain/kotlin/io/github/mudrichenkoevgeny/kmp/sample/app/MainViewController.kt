package io.github.mudrichenkoevgeny.kmp.sample.app

import androidx.compose.ui.window.ComposeUIViewController
import io.github.mudrichenkoevgeny.kmp.sample.app.di.AppComponent
import platform.UIKit.UIViewController
import io.github.mudrichenkoevgeny.kmp.sample.app.ui.root.RootContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun MainViewController(): UIViewController {
    val appComponent = AppComponent()

    CoroutineScope(Dispatchers.Main).launch {
        appComponent.init()
    }
    return ComposeUIViewController { RootContent(appComponent) }
}