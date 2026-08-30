package io.github.mudrichenkoevgeny.kmp.samplemanagement.app

import androidx.compose.ui.window.ComposeUIViewController
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.IosUserAuthServices
import io.github.mudrichenkoevgeny.kmp.samplemanagement.BuildConfig
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.di.ManagementAppComponent
import platform.UIKit.UIViewController
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.ui.root.RootContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * iOS entry: creates [ManagementAppComponent], starts initialization on the main dispatcher, and returns a
 * [ComposeUIViewController] that displays [RootContent].
 *
 * @return Root view controller for the sample scene.
 */
fun MainViewController(): UIViewController {
    var controller: UIViewController? = null

    val appComponent = ManagementAppComponent(
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