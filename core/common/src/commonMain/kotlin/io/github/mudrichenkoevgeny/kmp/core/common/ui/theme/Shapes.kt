package io.github.mudrichenkoevgeny.kmp.core.common.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.tokens.GeneratedDesignTokens

/**
 * Custom shape definitions for the SDK components.
 */
val CoreShapes: Shapes = Shapes(
    extraSmall = RoundedCornerShape(GeneratedDesignTokens.Radius.extraSmall),
    small = RoundedCornerShape(GeneratedDesignTokens.Radius.small),
    medium = RoundedCornerShape(GeneratedDesignTokens.Radius.medium),
    large = RoundedCornerShape(GeneratedDesignTokens.Radius.large),
    extraLarge = RoundedCornerShape(GeneratedDesignTokens.Radius.extraLarge)
)
