package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.success

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.user.Res
import io.github.mudrichenkoevgeny.kmp.feature.user.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun UnlockSuccessScreen(onFinished: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(CoreTheme.dimens.paddingLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(Res.string.unlock_success_title),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.testTag(UnlockSuccessTestTags.TITLE)
        )
        Spacer(Modifier.height(CoreTheme.dimens.paddingMedium))
        Text(
            text = stringResource(Res.string.unlock_success_desc),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.testTag(UnlockSuccessTestTags.DESC)
        )
        Spacer(Modifier.height(CoreTheme.dimens.paddingLarge))
        Button(
            onClick = onFinished,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(UnlockSuccessTestTags.FINISH_BUTTON)
        ) {
            Text(text = stringResource(Res.string.dialog_confirm))
        }
    }
}

internal object UnlockSuccessTestTags {
    const val TITLE = "UnlockSuccess_Title"
    const val DESC = "UnlockSuccess_Desc"
    const val FINISH_BUTTON = "UnlockSuccess_FinishButton"
}

@OptIn(InternalApi::class)
@Composable
private fun UnlockSuccessScreenPreviewContent() {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        Surface {
            UnlockSuccessScreen(onFinished = {})
        }
    }
}

@OptIn(InternalApi::class)
@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    ScreenPreviewContainer {
        UnlockSuccessScreenPreviewContent()
    }
}

@OptIn(InternalApi::class)
@ScreenSizePreviews
@Composable
private fun AdaptivePreview() {
    ScreenPreviewContainer {
        UnlockSuccessScreenPreviewContent()
    }
}

@OptIn(InternalApi::class)
@ThemePreviews
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        UnlockSuccessScreenPreviewContent()
    }
}

@OptIn(InternalApi::class)
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        UnlockSuccessScreenPreviewContent()
    }
}
