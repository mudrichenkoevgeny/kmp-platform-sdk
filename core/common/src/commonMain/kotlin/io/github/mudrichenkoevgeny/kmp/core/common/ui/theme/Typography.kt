package io.github.mudrichenkoevgeny.kmp.core.common.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import io.github.mudrichenkoevgeny.kmp.core.common.Res
import io.github.mudrichenkoevgeny.kmp.core.common.pt_sans_bold
import io.github.mudrichenkoevgeny.kmp.core.common.pt_sans_bold_italic
import io.github.mudrichenkoevgeny.kmp.core.common.pt_sans_italic
import io.github.mudrichenkoevgeny.kmp.core.common.pt_sans_regular
import org.jetbrains.compose.resources.Font

/**
 * Creates and returns the [FontFamily] for PT Sans.
 *
 * @return The configured [FontFamily] containing regular, bold, italic, and bold-italic styles.
 */
@Composable
fun ptSansFontFamily(): FontFamily {
    return FontFamily(
        Font(Res.font.pt_sans_regular, FontWeight.Normal, FontStyle.Normal),
        Font(Res.font.pt_sans_bold, FontWeight.Bold, FontStyle.Normal),
        Font(Res.font.pt_sans_italic, FontWeight.Normal, FontStyle.Italic),
        Font(Res.font.pt_sans_bold_italic, FontWeight.Bold, FontStyle.Italic)
    )
}

/**
 * Creates and returns the SDK typography configured with the PT Sans font family.
 *
 * @return Configured Material 3 [Typography] instance.
 */
@Composable
fun createCoreTypography(): Typography {
    val fontFamily = ptSansFontFamily()
    val defaultTypography = Typography()

    return Typography(
        displayLarge = defaultTypography.displayLarge.copy(fontFamily = fontFamily),
        displayMedium = defaultTypography.displayMedium.copy(fontFamily = fontFamily),
        displaySmall = defaultTypography.displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = fontFamily),
        titleLarge = defaultTypography.titleLarge.copy(fontFamily = fontFamily),
        titleMedium = defaultTypography.titleMedium.copy(fontFamily = fontFamily),
        titleSmall = defaultTypography.titleSmall.copy(fontFamily = fontFamily),
        bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = fontFamily),
        bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = defaultTypography.bodySmall.copy(fontFamily = fontFamily),
        labelLarge = defaultTypography.labelLarge.copy(fontFamily = fontFamily),
        labelMedium = defaultTypography.labelMedium.copy(fontFamily = fontFamily),
        labelSmall = defaultTypography.labelSmall.copy(fontFamily = fontFamily)
    )
}

/**
 * Default typography definition for [CoreTheme].
 */
val CoreTypography: Typography
    @Composable
    get() = createCoreTypography()
