package io.github.mudrichenkoevgeny.kmp.core.common.ui.preview

import androidx.compose.ui.tooling.preview.Preview

/**
 * Specifications for screen size previews.
 */
public object PreviewScreenSizesSpecs {
    public const val NAME_MOBILE: String = "1. Mobile Phone"
    public const val NAME_TABLET: String = "2. Tablet"
    public const val NAME_DESKTOP: String = "3. Desktop / Web"

    public const val WIDTH_MOBILE: Int = 360
    public const val HEIGHT_MOBILE: Int = 740

    public const val WIDTH_TABLET: Int = 768
    public const val HEIGHT_TABLET: Int = 1024

    public const val WIDTH_DESKTOP: Int = 1280
    public const val HEIGHT_DESKTOP: Int = 800
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
