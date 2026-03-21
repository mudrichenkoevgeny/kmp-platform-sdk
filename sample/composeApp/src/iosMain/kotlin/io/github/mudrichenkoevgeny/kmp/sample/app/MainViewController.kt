package io.github.mudrichenkoevgeny.kmp.sample.app

import androidx.compose.ui.window.ComposeUIViewController
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.IosUserAuthServices
import io.github.mudrichenkoevgeny.kmp.sample.BuildConfig
import io.github.mudrichenkoevgeny.kmp.sample.app.di.AppComponent
import platform.UIKit.UIViewController
import io.github.mudrichenkoevgeny.kmp.sample.app.ui.root.RootContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * iOS entry: creates [AppComponent], starts initialization on the main dispatcher, and returns a
 * [ComposeUIViewController] that displays [RootContent].
 *
 * @return Root view controller for the sample scene.
 */
fun MainViewController(): UIViewController {
    var controller: UIViewController? = null

    val appComponent = AppComponent(
        authServices = IosUserAuthServices(
            getRootController = { controller ?: error("Controller not initialized") },
            googleWebClientId = BuildConfig.GOOGLE_WEB_CLIENT_ID
        )
    )

    CoroutineScope(Dispatchers.Main).launch {
        appComponent.init()
    }

    val mainController = ComposeUIViewController {
        RootContent(appComponent)
    }

    controller = mainController

    return mainController
}