package io.github.mudrichenkoevgeny.kmp.samplemanagement.app.di

import androidx.compose.runtime.staticCompositionLocalOf

/**
 * [ManagementAppComponent] exposed to composables under the sample root (tabs, profile, and similar).
 */
val LocalManagementAppComponent = staticCompositionLocalOf<ManagementAppComponent> {
    error("AppComponent not provided")
}