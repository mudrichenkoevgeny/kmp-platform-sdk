package io.github.mudrichenkoevgeny.kmp.core.common.ui.preview

import androidx.compose.ui.tooling.preview.Preview

object ScreenSizePreviewSpecs {
    const val NAME_MOBILE = "1. Mobile Phone"
    const val NAME_TABLET = "2. Tablet"
    const val NAME_DESKTOP = "3. Desktop / Web"

    const val WIDTH_MOBILE = 360
    const val HEIGHT_MOBILE = 740

    const val WIDTH_TABLET = 768
    const val HEIGHT_TABLET = 1024

    const val WIDTH_DESKTOP = 1280
    const val HEIGHT_DESKTOP = 800
}

@Preview(
    name = ScreenSizePreviewSpecs.NAME_MOBILE,
    widthDp = ScreenSizePreviewSpecs.WIDTH_MOBILE,
    heightDp = ScreenSizePreviewSpecs.HEIGHT_MOBILE,
    showBackground = true
)
@Preview(
    name = ScreenSizePreviewSpecs.NAME_TABLET,
    widthDp = ScreenSizePreviewSpecs.WIDTH_TABLET,
    heightDp = ScreenSizePreviewSpecs.HEIGHT_TABLET,
    showBackground = true
)
@Preview(
    name = ScreenSizePreviewSpecs.NAME_DESKTOP,
    widthDp = ScreenSizePreviewSpecs.WIDTH_DESKTOP,
    heightDp = ScreenSizePreviewSpecs.HEIGHT_DESKTOP,
    showBackground = true
)
annotation class ScreenSizePreviews