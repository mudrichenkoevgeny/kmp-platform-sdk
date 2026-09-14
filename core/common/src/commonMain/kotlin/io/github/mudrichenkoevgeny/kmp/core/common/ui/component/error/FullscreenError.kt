package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.error

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import io.github.mudrichenkoevgeny.kmp.core.common.Res
import io.github.mudrichenkoevgeny.kmp.core.common.*
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ComponentSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import org.jetbrains.compose.resources.stringResource

/**
 * Fullscreen error screen with optional retry action.
 *
 * The displayed message comes from [error] via localization helpers.
 *
 * @param error Domain error to display.
 * @param onRetry Callback for the retry button action.
 * @param modifier Layout modifier.
 */
@Composable
fun FullscreenError(
    error: AppError,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(CoreTheme.dimens.paddingLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(CoreTheme.dimens.iconButtonSize)
        )

        Spacer(Modifier.height(CoreTheme.dimens.paddingMedium))

        Text(
            text = error.toLocalizedMessage(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        if (error.isRetryable) {
            Spacer(Modifier.height(CoreTheme.dimens.paddingLarge))

            Button(
                onClick = onRetry,
                shape = RoundedCornerShape(CoreTheme.dimens.roundedCornerShape)
            ) {
                Text(text = stringResource(Res.string.retry))
            }
        }
    }
}

private val defaultFullscreenErrorPreviewState = CommonError.Unknown(isRetryable = true)

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun FullscreenErrorNonRetryablePreview() {
    CoreTheme {
        Surface {
            CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
                FullscreenError(
                    error = CommonError.Unknown(isRetryable = false),
                    onRetry = {}
                )
            }
        }
    }
}

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun FullscreenErrorRetryablePreview() {
    CoreTheme {
        Surface {
            CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
                FullscreenError(
                    error = defaultFullscreenErrorPreviewState,
                    onRetry = {}
                )
            }
        }
    }
}

@InternalApi
@ComponentSizePreviews
@Composable
private fun FullscreenErrorComponentSizePreview() {
    CoreTheme {
        Surface {
            CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
                FullscreenError(
                    error = defaultFullscreenErrorPreviewState,
                    onRetry = {}
                )
            }
        }
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun FullscreenErrorThemePreview() {
    CoreTheme {
        Surface {
            CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
                FullscreenError(
                    error = defaultFullscreenErrorPreviewState,
                    onRetry = {}
                )
            }
        }
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FullscreenErrorFontScalePreview() {
    CoreTheme {
        Surface {
            CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
                FullscreenError(
                    error = defaultFullscreenErrorPreviewState,
                    onRetry = {}
                )
            }
        }
    }
}
