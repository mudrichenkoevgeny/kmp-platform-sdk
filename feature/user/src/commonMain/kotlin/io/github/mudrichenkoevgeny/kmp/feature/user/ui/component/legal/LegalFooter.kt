package io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.legal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.Dimens
import io.github.mudrichenkoevgeny.kmp.feature.user.Res
import io.github.mudrichenkoevgeny.kmp.feature.user.and
import io.github.mudrichenkoevgeny.kmp.feature.user.legal_agreement_prefix
import io.github.mudrichenkoevgeny.kmp.feature.user.privacy_policy
import io.github.mudrichenkoevgeny.kmp.feature.user.terms_of_service
import org.jetbrains.compose.resources.stringResource

/**
 * Footer block for legal links (privacy policy and/or terms of service).
 *
 * Renders a centered [FlowRow] with a muted prefix and primary-colored, underlined link labels using
 * [Modifier.clickable]. If both flags are false, nothing is displayed.
 *
 * @param isPrivacyPolicyVisible whether to show the privacy policy link.
 * @param isTermsOfServiceVisible whether to show the terms of service link.
 * @param onPrivacyPolicyClick invoked when the privacy policy link is activated.
 * @param onTermsOfServiceClick invoked when the terms of service link is activated.
 */
@Composable
fun LegalFooter(
    isPrivacyPolicyVisible: Boolean,
    isTermsOfServiceVisible: Boolean,
    onPrivacyPolicyClick: () -> Unit,
    onTermsOfServiceClick: () -> Unit
) {
    if (!isPrivacyPolicyVisible && !isTermsOfServiceVisible) {
        return
    }

    val privacyText = stringResource(Res.string.privacy_policy)
    val termsText = stringResource(Res.string.terms_of_service)
    val andText = " ${stringResource(Res.string.and)} "
    val prefixText = stringResource(Res.string.legal_agreement_prefix)

    val bodySmall = MaterialTheme.typography.bodySmall
    val mutedColor = MaterialTheme.colorScheme.onSurfaceVariant
    val linkColor = MaterialTheme.colorScheme.primary
    val linkStyle = bodySmall.copy(
        color = linkColor,
        textDecoration = TextDecoration.Underline
    )

    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.paddingMedium),
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "$prefixText ",
            style = bodySmall,
            color = mutedColor,
        )
        when {
            isPrivacyPolicyVisible && isTermsOfServiceVisible -> {
                Text(
                    text = privacyText,
                    style = linkStyle,
                    modifier = Modifier.clickable { onPrivacyPolicyClick() }
                )
                Text(
                    text = andText,
                    style = bodySmall,
                    color = mutedColor,
                )
                Text(
                    text = termsText,
                    style = linkStyle,
                    modifier = Modifier.clickable { onTermsOfServiceClick() }
                )
            }
            isPrivacyPolicyVisible -> {
                Text(
                    text = privacyText,
                    style = linkStyle,
                    modifier = Modifier.clickable { onPrivacyPolicyClick() }
                )
            }
            isTermsOfServiceVisible -> {
                Text(
                    text = termsText,
                    style = linkStyle,
                    modifier = Modifier.clickable { onTermsOfServiceClick() }
                )
            }
        }
    }
}

private data class LegalFooterPreviewState(
    val isPrivacyPolicyVisible: Boolean,
    val isTermsOfServiceVisible: Boolean
)

private class LegalFooterPreviewProvider : PreviewParameterProvider<LegalFooterPreviewState> {
    override val values: Sequence<LegalFooterPreviewState> = sequenceOf(
        LegalFooterPreviewState(isPrivacyPolicyVisible = true, isTermsOfServiceVisible = false),
        LegalFooterPreviewState(isPrivacyPolicyVisible = false, isTermsOfServiceVisible = true),
        LegalFooterPreviewState(isPrivacyPolicyVisible = true, isTermsOfServiceVisible = true),
        LegalFooterPreviewState(isPrivacyPolicyVisible = false, isTermsOfServiceVisible = false)
    )
}

@Preview(showBackground = true, name = "Narrow", widthDp = 280)
@Preview(showBackground = true, name = "Standard", widthDp = 400)
@Composable
private fun LegalFooterPreview(
    @PreviewParameter(LegalFooterPreviewProvider::class) state: LegalFooterPreviewState
) {
    MaterialTheme {
        Box(modifier = Modifier.padding(Dimens.paddingLarge)) {
            LegalFooter(
                isPrivacyPolicyVisible = state.isPrivacyPolicyVisible,
                isTermsOfServiceVisible = state.isTermsOfServiceVisible,
                onPrivacyPolicyClick = {},
                onTermsOfServiceClick = {}
            )
        }
    }
}
