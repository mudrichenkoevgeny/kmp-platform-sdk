package io.github.mudrichenkoevgeny.kmp.core.common.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Centralized dimension data class used by shared UI components.
 */
@Immutable
data class CoreDimens(
    val paddingSmall: Dp = 8.dp,
    val paddingMedium: Dp = 16.dp,
    val paddingLarge: Dp = 24.dp,
    val headerHeight: Dp = 64.dp,
    val iconSizeHeader: Dp = 32.dp,
    val iconButtonSize: Dp = 48.dp,
    val actionButtonHeight: Dp = 52.dp,
    val actionButtonIconSize: Dp = 20.dp,
    val elevationHeader: Dp = 2.dp,
    val roundedCornerShape: Dp = 8.dp,
    val shadowElevation: Dp = 4.dp,
    val rowHeight: Dp = 64.dp,
    val progressIndicatorStrokeWidth: Dp = 4.dp,
    val progressIndicatorStrokeWidthSmall: Dp = 2.dp,
    val progressIndicatorSizeSmall: Dp = 24.dp,
    val progressIndicatorSizeLarge: Dp = 52.dp,
    val qrCodeSize: Dp = 200.dp,
    val previewContainerHeight: Dp = 300.dp
)

internal val LocalCoreDimens = staticCompositionLocalOf { CoreDimens() }