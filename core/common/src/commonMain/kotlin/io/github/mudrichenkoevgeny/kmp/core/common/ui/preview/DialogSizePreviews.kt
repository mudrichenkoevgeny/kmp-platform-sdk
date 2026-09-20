package io.github.mudrichenkoevgeny.kmp.core.common.ui.preview

import androidx.compose.ui.tooling.preview.Preview

object DialogSizePreviewSpecs {
    const val NAME_COMPACT = "1. Compact Dialog"
    const val NAME_MOBILE_SHEET = "2. Mobile Dialog / Sheet"
    const val NAME_STANDARD = "3. Standard Dialog"

    const val WIDTH_COMPACT = 320
    const val HEIGHT_COMPACT = 480

    const val WIDTH_MOBILE_SHEET = 360
    const val HEIGHT_MOBILE_SHEET = 520

    const val WIDTH_STANDARD = 480
    const val HEIGHT_STANDARD = 520
}

@Preview(
    name = DialogSizePreviewSpecs.NAME_COMPACT,
    widthDp = DialogSizePreviewSpecs.WIDTH_COMPACT,
    heightDp = DialogSizePreviewSpecs.HEIGHT_COMPACT,
    showBackground = true
)
@Preview(
    name = DialogSizePreviewSpecs.NAME_MOBILE_SHEET,
    widthDp = DialogSizePreviewSpecs.WIDTH_MOBILE_SHEET,
    heightDp = DialogSizePreviewSpecs.HEIGHT_MOBILE_SHEET,
    showBackground = true
)
@Preview(
    name = DialogSizePreviewSpecs.NAME_STANDARD,
    widthDp = DialogSizePreviewSpecs.WIDTH_STANDARD,
    heightDp = DialogSizePreviewSpecs.HEIGHT_STANDARD,
    showBackground = true
)
annotation class DialogSizePreviews
