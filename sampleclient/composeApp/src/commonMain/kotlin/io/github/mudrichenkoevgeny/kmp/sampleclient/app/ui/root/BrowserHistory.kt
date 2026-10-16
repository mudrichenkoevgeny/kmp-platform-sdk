package io.github.mudrichenkoevgeny.kmp.sampleclient.app.ui.root

import androidx.compose.runtime.Composable
import io.github.mudrichenkoevgeny.kmp.sampleclient.app.ui.screen.main.MainScreenComponent

/**
 * Platform-specific browser history synchronization setup for web/Wasm navigation.
 * No-op on Android and non-browser platforms.
 *
 * @param mainComponent Root navigation component driving active screens and dialogs.
 * @param baseUrl Host domain base URL used for absolute path resolution.
 */
@Composable
expect fun SetupBrowserHistory(
    mainComponent: MainScreenComponent,
    baseUrl: String
)
