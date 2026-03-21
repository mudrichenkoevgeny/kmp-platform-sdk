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
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.MockAppErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource

/**
 * Fullscreen error screen with optional retry action.
 *
 * The displayed message comes from [error] via localization helpers.
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
            .padding(Dimens.paddingLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(Dimens.iconButtonSize)
        )

        Spacer(Modifier.height(Dimens.paddingMedium))

        Text(
            text = error.toLocalizedMessage(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        if (error.isRetryable) {
            Spacer(Modifier.height(Dimens.paddingLarge))

            Button(
                onClick = onRetry,
                shape = RoundedCornerShape(Dimens.roundedCornerShape)
            ) {
                Text(text = stringResource(Res.string.retry))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FullscreenErrorPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides MockAppErrorParser) {
            FullscreenError(
                error = CommonError.Unknown(isRetryable = false),
                onRetry = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FullscreenErrorRetryablePreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides MockAppErrorParser) {
            FullscreenError(
                error = CommonError.Unknown(isRetryable = true),
                onRetry = {}
            )
        }
    }
}