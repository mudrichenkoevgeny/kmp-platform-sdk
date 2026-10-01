package io.github.mudrichenkoevgeny.kmp.core.common.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.tokens.GeneratedDesignTokens

/**
 * Centralized dimension data class used by shared UI components.
 */
@Immutable
data class CoreDimens(
    val paddingExtraSmall: Dp = GeneratedDesignTokens.Spacing.extraSmall,
    val paddingSmall: Dp = GeneratedDesignTokens.Spacing.small,
    val paddingMedium: Dp = GeneratedDesignTokens.Spacing.medium,
    val paddingLarge: Dp = GeneratedDesignTokens.Spacing.large,
    val headerHeight: Dp = GeneratedDesignTokens.Sizing.headerHeight,
    val iconSizeHeader: Dp = GeneratedDesignTokens.Sizing.iconSizeHeader,
    val iconButtonSize: Dp = GeneratedDesignTokens.Sizing.iconButtonSize,
    val actionButtonHeight: Dp = GeneratedDesignTokens.Sizing.actionButtonHeight,
    val actionButtonIconSize: Dp = GeneratedDesignTokens.Sizing.actionButtonIconSize,
    val elevationHeader: Dp = GeneratedDesignTokens.Elevation.header,
    val roundedCornerShape: Dp = GeneratedDesignTokens.Radius.small,
    val shadowElevation: Dp = GeneratedDesignTokens.Elevation.shadow,
    val rowHeight: Dp = GeneratedDesignTokens.Sizing.rowHeight,
    val progressIndicatorStrokeWidth: Dp = GeneratedDesignTokens.Sizing.progressIndicatorStrokeWidth,
    val progressIndicatorStrokeWidthSmall: Dp = GeneratedDesignTokens.Sizing.progressIndicatorStrokeWidthSmall,
    val progressIndicatorSizeSmall: Dp = GeneratedDesignTokens.Sizing.progressIndicatorSizeSmall,
    val progressIndicatorSizeLarge: Dp = GeneratedDesignTokens.Sizing.progressIndicatorSizeLarge,
    val qrCodeSize: Dp = GeneratedDesignTokens.Sizing.qrCodeSize,
    val previewContainerHeight: Dp = GeneratedDesignTokens.Sizing.previewContainerHeight,
    val dialogWidth: Dp = GeneratedDesignTokens.Sizing.dialogWidth,
    val dialogHeight: Dp = GeneratedDesignTokens.Sizing.dialogHeight,
    val maxFormWidth: Dp = GeneratedDesignTokens.Sizing.maxFormWidth,
    val maxContentWidth: Dp = GeneratedDesignTokens.Sizing.maxContentWidth,
    val maxButtonWidth: Dp = GeneratedDesignTokens.Sizing.maxButtonWidth,
    val navigationRailWidth: Dp = GeneratedDesignTokens.Sizing.navigationRailWidth
)

internal val LocalCoreDimens = staticCompositionLocalOf { CoreDimens() }
