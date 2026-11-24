package io.github.mudrichenkoevgeny.kmp.core.common.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.tokens.PlatformDesignTokens

internal val LocalCoreDimens = staticCompositionLocalOf { CoreDimens() }

/**
 * Centralized dimension data class used by shared UI components.
 */
@Immutable
public data class CoreDimens(
    public val paddingExtraSmall: Dp = PlatformDesignTokens.Spacing.extraSmall,
    public val paddingSmall: Dp = PlatformDesignTokens.Spacing.small,
    public val paddingMedium: Dp = PlatformDesignTokens.Spacing.medium,
    public val paddingLarge: Dp = PlatformDesignTokens.Spacing.large,
    public val headerHeight: Dp = PlatformDesignTokens.Sizing.headerHeight,
    public val iconSizeSmall: Dp = 14.dp,
    public val iconSizeMedium: Dp = 20.dp,
    public val iconSizeLarge: Dp = 24.dp,
    public val iconSizeXLarge: Dp = 36.dp,
    public val iconSizeHeader: Dp = PlatformDesignTokens.Sizing.iconSizeHeader,
    public val iconButtonSize: Dp = PlatformDesignTokens.Sizing.iconButtonSize,
    public val actionButtonHeight: Dp = PlatformDesignTokens.Sizing.actionButtonHeight,
    public val actionButtonIconSize: Dp = PlatformDesignTokens.Sizing.actionButtonIconSize,
    public val elevationHeader: Dp = PlatformDesignTokens.Elevation.header,
    public val roundedCornerShape: Dp = PlatformDesignTokens.Radius.small,
    public val shadowElevation: Dp = PlatformDesignTokens.Elevation.shadow,
    public val rowHeight: Dp = PlatformDesignTokens.Sizing.rowHeight,
    public val borderWidthThin: Dp = 1.dp,
    public val borderWidthThick: Dp = 2.dp,
    public val previewThumbnailSize: Dp = 48.dp,
    public val progressIndicatorStrokeWidth: Dp = PlatformDesignTokens.Sizing.progressIndicatorStrokeWidth,
    public val progressIndicatorStrokeWidthSmall: Dp = PlatformDesignTokens.Sizing.progressIndicatorStrokeWidthSmall,
    public val progressIndicatorSizeSmall: Dp = PlatformDesignTokens.Sizing.progressIndicatorSizeSmall,
    public val progressIndicatorSizeLarge: Dp = PlatformDesignTokens.Sizing.progressIndicatorSizeLarge,
    public val qrCodeSize: Dp = PlatformDesignTokens.Sizing.qrCodeSize,
    public val previewContainerHeight: Dp = PlatformDesignTokens.Sizing.previewContainerHeight,
    public val dialogWidth: Dp = PlatformDesignTokens.Sizing.dialogWidth,
    public val dialogHeight: Dp = PlatformDesignTokens.Sizing.dialogHeight,
    public val maxFormWidth: Dp = PlatformDesignTokens.Sizing.maxFormWidth,
    public val maxContentWidth: Dp = PlatformDesignTokens.Sizing.maxContentWidth,
    public val maxButtonWidth: Dp = PlatformDesignTokens.Sizing.maxButtonWidth,
    public val navigationRailWidth: Dp = PlatformDesignTokens.Sizing.navigationRailWidth
)
