package io.github.mudrichenkoevgeny.kmp.core.common.ui.preview

import androidx.compose.ui.tooling.preview.Preview

object ThemePreviewSpecs {
    const val NAME_LIGHT = "1. Light Theme"
    const val NAME_DARK = "2. Dark Theme"
    const val GROUP = "Theme"

    const val UI_MODE_LIGHT = 0x10
    const val UI_MODE_DARK = 0x20
}

@Preview(
    name = ThemePreviewSpecs.NAME_LIGHT,
    group = ThemePreviewSpecs.GROUP,
    uiMode = ThemePreviewSpecs.UI_MODE_LIGHT,
    showBackground = true
)
@Preview(
    name = ThemePreviewSpecs.NAME_DARK,
    group = ThemePreviewSpecs.GROUP,
    uiMode = ThemePreviewSpecs.UI_MODE_DARK,
    showBackground = true
)
annotation class ThemePreviews