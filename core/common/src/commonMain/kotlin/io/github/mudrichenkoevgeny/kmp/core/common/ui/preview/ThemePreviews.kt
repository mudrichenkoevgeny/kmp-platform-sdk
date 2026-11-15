package io.github.mudrichenkoevgeny.kmp.core.common.ui.preview

import androidx.compose.ui.tooling.preview.Preview

/**
 * Specifications for theme previews.
 */
public object PreviewThemesSpecs {
    public const val NAME_LIGHT: String = "1. Light Theme"
    public const val NAME_DARK: String = "2. Dark Theme"
    public const val GROUP: String = "Theme"

    public const val UI_MODE_LIGHT: Int = 0x10
    public const val UI_MODE_DARK: Int = 0x20
}

/**
 * Multi-preview annotation for light and dark theme previews.
 */
@Preview(
    name = PreviewThemesSpecs.NAME_LIGHT,
    group = PreviewThemesSpecs.GROUP,
    uiMode = PreviewThemesSpecs.UI_MODE_LIGHT,
    showBackground = true
)
@Preview(
    name = PreviewThemesSpecs.NAME_DARK,
    group = PreviewThemesSpecs.GROUP,
    uiMode = PreviewThemesSpecs.UI_MODE_DARK,
    showBackground = true
)
public annotation class PreviewThemes
