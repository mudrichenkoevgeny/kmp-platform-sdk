package io.github.mudrichenkoevgeny.kmp.core.common.ui.preview

import androidx.compose.ui.tooling.preview.Preview

/**
 * Specifications for dialog size previews.
 */
internal object PreviewDialogSizesSpecs {
    const val NAME_COMPACT: String = "1. Compact Dialog"
    const val NAME_MOBILE_SHEET: String = "2. Mobile Dialog / Sheet"
    const val NAME_STANDARD: String = "3. Standard Dialog"

    const val WIDTH_COMPACT: Int = 320
    const val HEIGHT_COMPACT: Int = 480

    const val WIDTH_MOBILE_SHEET: Int = 360
    const val HEIGHT_MOBILE_SHEET: Int = 520

    const val WIDTH_STANDARD: Int = 480
    const val HEIGHT_STANDARD: Int = 520
}

/**
 * Multi-preview annotation for dialog size previews across compact, mobile sheet, and standard sizes.
 */
@Preview(
    name = PreviewDialogSizesSpecs.NAME_COMPACT,
    widthDp = PreviewDialogSizesSpecs.WIDTH_COMPACT,
    heightDp = PreviewDialogSizesSpecs.HEIGHT_COMPACT,
    showBackground = true
)
@Preview(
    name = PreviewDialogSizesSpecs.NAME_MOBILE_SHEET,
    widthDp = PreviewDialogSizesSpecs.WIDTH_MOBILE_SHEET,
    heightDp = PreviewDialogSizesSpecs.HEIGHT_MOBILE_SHEET,
    showBackground = true
)
@Preview(
    name = PreviewDialogSizesSpecs.NAME_STANDARD,
    widthDp = PreviewDialogSizesSpecs.WIDTH_STANDARD,
    heightDp = PreviewDialogSizesSpecs.HEIGHT_STANDARD,
    showBackground = true
)
public annotation class PreviewDialogSizes
