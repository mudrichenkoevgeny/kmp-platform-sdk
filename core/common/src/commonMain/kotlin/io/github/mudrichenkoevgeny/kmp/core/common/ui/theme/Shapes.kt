package io.github.mudrichenkoevgeny.kmp.core.common.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.tokens.PlatformDesignTokens

/**
 * Custom shape definitions for the SDK components.
 */
public val CoreShapes: Shapes = Shapes(
    extraSmall = RoundedCornerShape(PlatformDesignTokens.Radius.extraSmall),
    small = RoundedCornerShape(PlatformDesignTokens.Radius.small),
    medium = RoundedCornerShape(PlatformDesignTokens.Radius.medium),
    large = RoundedCornerShape(PlatformDesignTokens.Radius.large),
    extraLarge = RoundedCornerShape(PlatformDesignTokens.Radius.extraLarge)
)
