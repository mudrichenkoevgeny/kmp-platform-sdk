package io.github.mudrichenkoevgeny.kmp.core.common.ui.preview

import androidx.compose.ui.tooling.preview.Preview

/**
 * Specifications for component size previews.
 */
public object PreviewComponentSizesSpecs {
    public const val NAME_STANDARD: String = "1. Standard Component"
    public const val NAME_NARROW: String = "2. Narrow Component"

    public const val WIDTH_STANDARD: Int = 400
    public const val WIDTH_NARROW: Int = 280
}

/**
 * Multi-preview annotation for component size previews across standard and narrow widths.
 */
@Preview(
    name = PreviewComponentSizesSpecs.NAME_STANDARD,
    widthDp = PreviewComponentSizesSpecs.WIDTH_STANDARD,
    showBackground = true
)
@Preview(
    name = PreviewComponentSizesSpecs.NAME_NARROW,
    widthDp = PreviewComponentSizesSpecs.WIDTH_NARROW,
    showBackground = true
)
public annotation class PreviewComponentSizes
