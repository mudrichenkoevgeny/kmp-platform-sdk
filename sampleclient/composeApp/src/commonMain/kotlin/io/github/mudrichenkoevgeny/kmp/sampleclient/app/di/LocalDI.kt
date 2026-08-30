package io.github.mudrichenkoevgeny.kmp.sampleclient.app.di

import androidx.compose.runtime.staticCompositionLocalOf

/**
 * [ClientAppComponent] exposed to composables under the sample root (tabs, profile, and similar).
 */
val LocalClientAppComponent = staticCompositionLocalOf<ClientAppComponent> {
    error("AppComponent not provided")
}