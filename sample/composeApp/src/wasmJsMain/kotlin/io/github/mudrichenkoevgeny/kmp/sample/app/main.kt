package io.github.mudrichenkoevgeny.kmp.sample.app

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import io.github.mudrichenkoevgeny.kmp.sample.app.di.AppComponent
import kotlinx.browser.document
import io.github.mudrichenkoevgeny.kmp.sample.app.ui.root.RootContent
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val appComponent = AppComponent(platformContext = null)

    val container = document.getElementById("ComposeTarget")
        ?: error("Element not found")

    MainScope().launch {
        appComponent.init()

        ComposeViewport(container) {
            RootContent(appComponent)
        }
    }
}