package io.github.mudrichenkoevgeny.kmp.core.common.ui.preview

import androidx.compose.ui.tooling.preview.Preview

/**
 * Specifications for screen size previews.
 */
internal object PreviewScreenSizesSpecs {
    const val NAME_MOBILE: String = "1. Mobile Phone"
    const val NAME_TABLET: String = "2. Tablet"
    const val NAME_DESKTOP: String = "3. Desktop / Web"

    const val WIDTH_MOBILE: Int = 360
    const val HEIGHT_MOBILE: Int = 740

    const val WIDTH_TABLET: Int = 768
    const val HEIGHT_TABLET: Int = 1024

    const val WIDTH_DESKTOP: Int = 1280
    const val HEIGHT_DESKTOP: Int = 800
}

/**
 * Multi-preview annotation for screen size previews across mobile, tablet, and desktop viewports.
 */
@Preview(
    name = PreviewScreenSizesSpecs.NAME_MOBILE,
    widthDp = PreviewScreenSizesSpecs.WIDTH_MOBILE,
    heightDp = PreviewScreenSizesSpecs.HEIGHT_MOBILE,
    showBackground = true
)
@Preview(
    name = PreviewScreenSizesSpecs.NAME_TABLET,
    widthDp = PreviewScreenSizesSpecs.WIDTH_TABLET,
    heightDp = PreviewScreenSizesSpecs.HEIGHT_TABLET,
    showBackground = true
)
@Preview(
    name = PreviewScreenSizesSpecs.NAME_DESKTOP,
    widthDp = PreviewScreenSizesSpecs.WIDTH_DESKTOP,
    heightDp = PreviewScreenSizesSpecs.HEIGHT_DESKTOP,
    showBackground = true
)
public annotation class PreviewScreenSizes
