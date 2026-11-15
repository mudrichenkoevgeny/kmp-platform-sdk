package io.github.mudrichenkoevgeny.kmp.core.common.ui.preview

import androidx.compose.ui.tooling.preview.Preview

/**
 * Specifications for font scale previews.
 */
public object PreviewFontScalesSpecs {
    public const val NAME_NORMAL: String = "1. Normal Font (1.0x)"
    public const val NAME_LARGE: String = "2. Large Font (1.5x)"
    public const val NAME_LARGEST: String = "3. Largest Font (2.0x)"
    public const val GROUP: String = "Font Scale"

    public const val SCALE_NORMAL: Float = 1.0f
    public const val SCALE_LARGE: Float = 1.5f
    public const val SCALE_LARGEST: Float = 2.0f
}

/**
 * Multi-preview annotation for font scale previews across normal, large, and largest font scales.
 */
@Preview(
    name = PreviewFontScalesSpecs.NAME_NORMAL,
    group = PreviewFontScalesSpecs.GROUP,
    fontScale = PreviewFontScalesSpecs.SCALE_NORMAL,
    showBackground = true
)
@Preview(
    name = PreviewFontScalesSpecs.NAME_LARGE,
    group = PreviewFontScalesSpecs.GROUP,
    fontScale = PreviewFontScalesSpecs.SCALE_LARGE,
    showBackground = true
)
@Preview(
    name = PreviewFontScalesSpecs.NAME_LARGEST,
    group = PreviewFontScalesSpecs.GROUP,
    fontScale = PreviewFontScalesSpecs.SCALE_LARGEST,
    showBackground = true
)
public annotation class PreviewFontScales
