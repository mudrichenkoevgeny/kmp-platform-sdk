package io.github.mudrichenkoevgeny.kmp.sampleclient.app.ui.root

import androidx.compose.runtime.Composable
import io.github.mudrichenkoevgeny.kmp.sampleclient.app.ui.screen.main.MainScreenComponent

/**
 * Android implementation of browser history synchronization: no-op on Android.
 *
 * @param mainComponent Root navigation component.
 * @param baseUrl Host domain base URL.
 */
@Composable
actual fun SetupBrowserHistory(
    mainComponent: MainScreenComponent,
    baseUrl: String
) {
}
