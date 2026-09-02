package io.github.mudrichenkoevgeny.kmp.samplemanagement.app.ui.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

/**
 * Placeholder home tab content for the sample.
 *
 * @param screenComponent Tab component instance (reserved for future state wiring).
 */
@Composable
fun HomeScreen(screenComponent: HomeScreenComponent) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Home Screen",
            modifier = Modifier.testTag(HomeScreenTestTags.TITLE)
        )
    }
}

internal object HomeScreenTestTags {
    const val TITLE = "HomeScreen_Title"
}