package io.github.mudrichenkoevgeny.kmp.sample.app.di

import androidx.compose.runtime.staticCompositionLocalOf

/**
 * [AppComponent] exposed to composables under the sample root (tabs, profile, and similar).
 */
val LocalAppComponent = staticCompositionLocalOf<AppComponent> {
    error("AppComponent not provided")
}