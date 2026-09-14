package io.github.mudrichenkoevgeny.kmp.core.common.ui.preview

import androidx.compose.ui.tooling.preview.Preview

object ComponentSizePreviewSpecs {
    const val NAME_STANDARD = "1. Standard Component"
    const val NAME_NARROW = "2. Narrow Component"

    const val WIDTH_STANDARD = 400
    const val WIDTH_NARROW = 280
}

@Preview(
    name = ComponentSizePreviewSpecs.NAME_STANDARD,
    widthDp = ComponentSizePreviewSpecs.WIDTH_STANDARD,
    showBackground = true
)
@Preview(
    name = ComponentSizePreviewSpecs.NAME_NARROW,
    widthDp = ComponentSizePreviewSpecs.WIDTH_NARROW,
    showBackground = true
)
annotation class ComponentSizePreviews