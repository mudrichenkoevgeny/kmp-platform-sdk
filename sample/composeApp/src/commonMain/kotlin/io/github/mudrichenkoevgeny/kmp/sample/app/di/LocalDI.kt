package io.github.mudrichenkoevgeny.kmp.sample.app.di

import androidx.compose.runtime.staticCompositionLocalOf

val LocalAppComponent = staticCompositionLocalOf<AppComponent> {
    error("AppComponent not provided")
}