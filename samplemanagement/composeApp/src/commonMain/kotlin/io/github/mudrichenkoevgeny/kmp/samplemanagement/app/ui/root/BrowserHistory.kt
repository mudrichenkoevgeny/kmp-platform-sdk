package io.github.mudrichenkoevgeny.kmp.samplemanagement.app.ui.root

import androidx.compose.runtime.Composable
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.ui.screen.main.MainScreenComponent

/**
 * Platform-specific browser history synchronization setup for web/Wasm management portal navigation.
 * No-op on Android and non-browser platforms.
 *
 * @param mainComponent Root navigation component driving active management screens and dialogs.
 * @param baseUrl Host domain base URL used for absolute path resolution.
 */
@Composable
expect fun SetupBrowserHistory(
    mainComponent: MainScreenComponent,
    baseUrl: String
)
