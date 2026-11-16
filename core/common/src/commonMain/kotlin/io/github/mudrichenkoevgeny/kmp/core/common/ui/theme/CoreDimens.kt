package io.github.mudrichenkoevgeny.kmp.core.common.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.tokens.GeneratedDesignTokens

internal val LocalCoreDimens = staticCompositionLocalOf { CoreDimens() }

/**
 * Centralized dimension data class used by shared UI components.
 */
@Immutable
public data class CoreDimens(
    public val paddingExtraSmall: Dp = GeneratedDesignTokens.Spacing.extraSmall,
    public val paddingSmall: Dp = GeneratedDesignTokens.Spacing.small,
    public val paddingMedium: Dp = GeneratedDesignTokens.Spacing.medium,
    public val paddingLarge: Dp = GeneratedDesignTokens.Spacing.large,
    public val headerHeight: Dp = GeneratedDesignTokens.Sizing.headerHeight,
    public val iconSizeSmall: Dp = 14.dp,
    public val iconSizeMedium: Dp = 20.dp,
    public val iconSizeLarge: Dp = 24.dp,
    public val iconSizeXLarge: Dp = 36.dp,
    public val iconSizeHeader: Dp = GeneratedDesignTokens.Sizing.iconSizeHeader,
    public val iconButtonSize: Dp = GeneratedDesignTokens.Sizing.iconButtonSize,
    public val actionButtonHeight: Dp = GeneratedDesignTokens.Sizing.actionButtonHeight,
    public val actionButtonIconSize: Dp = GeneratedDesignTokens.Sizing.actionButtonIconSize,
    public val elevationHeader: Dp = GeneratedDesignTokens.Elevation.header,
    public val roundedCornerShape: Dp = GeneratedDesignTokens.Radius.small,
    public val shadowElevation: Dp = GeneratedDesignTokens.Elevation.shadow,
    public val rowHeight: Dp = GeneratedDesignTokens.Sizing.rowHeight,
    public val borderWidthThin: Dp = 1.dp,
    public val borderWidthThick: Dp = 2.dp,
    public val previewThumbnailSize: Dp = 48.dp,
    public val progressIndicatorStrokeWidth: Dp = GeneratedDesignTokens.Sizing.progressIndicatorStrokeWidth,
    public val progressIndicatorStrokeWidthSmall: Dp = GeneratedDesignTokens.Sizing.progressIndicatorStrokeWidthSmall,
    public val progressIndicatorSizeSmall: Dp = GeneratedDesignTokens.Sizing.progressIndicatorSizeSmall,
    public val progressIndicatorSizeLarge: Dp = GeneratedDesignTokens.Sizing.progressIndicatorSizeLarge,
    public val qrCodeSize: Dp = GeneratedDesignTokens.Sizing.qrCodeSize,
    public val previewContainerHeight: Dp = GeneratedDesignTokens.Sizing.previewContainerHeight,
    public val dialogWidth: Dp = GeneratedDesignTokens.Sizing.dialogWidth,
    public val dialogHeight: Dp = GeneratedDesignTokens.Sizing.dialogHeight,
    public val maxFormWidth: Dp = GeneratedDesignTokens.Sizing.maxFormWidth,
    public val maxContentWidth: Dp = GeneratedDesignTokens.Sizing.maxContentWidth,
    public val maxButtonWidth: Dp = GeneratedDesignTokens.Sizing.maxButtonWidth,
    public val navigationRailWidth: Dp = GeneratedDesignTokens.Sizing.navigationRailWidth
)
