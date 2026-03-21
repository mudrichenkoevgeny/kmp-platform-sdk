package io.github.mudrichenkoevgeny.kmp.sample.app.ui.screen.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.mudrichenkoevgeny.kmp.sample.app.di.AppComponent

/**
 * Shown while [AppComponent] initialization is in progress (before the main graph is ready).
 */
@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}