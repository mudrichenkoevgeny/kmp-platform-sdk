package io.github.mudrichenkoevgeny.kmp.core.common.ui.preview

import androidx.compose.ui.tooling.preview.Preview

object FontScalePreviewSpecs {
    const val NAME_NORMAL = "1. Normal Font (1.0x)"
    const val NAME_LARGE = "2. Large Font (1.5x)"
    const val NAME_LARGEST = "3. Largest Font (2.0x)"
    const val GROUP = "Font Scale"

    const val SCALE_NORMAL = 1.0f
    const val SCALE_LARGE = 1.5f
    const val SCALE_LARGEST = 2.0f
}

@Preview(
    name = FontScalePreviewSpecs.NAME_NORMAL,
    group = FontScalePreviewSpecs.GROUP,
    fontScale = FontScalePreviewSpecs.SCALE_NORMAL,
    showBackground = true
)
@Preview(
    name = FontScalePreviewSpecs.NAME_LARGE,
    group = FontScalePreviewSpecs.GROUP,
    fontScale = FontScalePreviewSpecs.SCALE_LARGE,
    showBackground = true
)
@Preview(
    name = FontScalePreviewSpecs.NAME_LARGEST,
    group = FontScalePreviewSpecs.GROUP,
    fontScale = FontScalePreviewSpecs.SCALE_LARGEST,
    showBackground = true
)
annotation class FontScalePreviews