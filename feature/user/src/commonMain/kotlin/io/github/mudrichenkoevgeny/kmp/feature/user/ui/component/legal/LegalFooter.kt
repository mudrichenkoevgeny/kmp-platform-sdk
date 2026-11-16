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
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ComponentPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewComponentSizes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewFontScales
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewThemes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.user.localization.LocalUserStrings

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
public fun LegalFooter(
    isPrivacyPolicyVisible: Boolean,
    isTermsOfServiceVisible: Boolean,
    onPrivacyPolicyClick: () -> Unit,
    onTermsOfServiceClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isPrivacyPolicyVisible && !isTermsOfServiceVisible) {
        return
    }

    val userUi = LocalUserStrings.current.ui
    val privacyText = userUi.privacyPolicy
    val termsText = userUi.termsOfService
    val andText = " ${userUi.and} "
    val prefixText = userUi.legalAgreementPrefix

    val bodySmall = MaterialTheme.typography.bodySmall
    val mutedColor = MaterialTheme.colorScheme.onSurfaceVariant
    val linkColor = MaterialTheme.colorScheme.primary
    val linkStyle = bodySmall.copy(
        color = linkColor,
        textDecoration = TextDecoration.Underline
    )

    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = CoreTheme.dimens.paddingMedium),
        horizontalArrangement = Arrangement.Center
    ) {
        @Suppress("NoRawStringInComposeUiRule")
        Text(
            text = "$prefixText ",
            style = bodySmall,
            color = mutedColor
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
                    color = mutedColor
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
    private val items: List<Pair<String, LegalFooterPreviewState>> = listOf(
        "Privacy Policy Only" to LegalFooterPreviewState(
            isPrivacyPolicyVisible = true,
            isTermsOfServiceVisible = false
        ),
        "Terms Of Service Only" to LegalFooterPreviewState(
            isPrivacyPolicyVisible = false,
            isTermsOfServiceVisible = true
        ),
        "Both Visible" to LegalFooterPreviewState(
            isPrivacyPolicyVisible = true,
            isTermsOfServiceVisible = true
        ),
        "Both Hidden" to LegalFooterPreviewState(
            isPrivacyPolicyVisible = false,
            isTermsOfServiceVisible = false
        )
    )

    override val values: Sequence<LegalFooterPreviewState> =
        items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? =
        items.getOrNull(index)?.first
}

@InternalApi
@Composable
private fun LegalFooterPreviewContent(state: LegalFooterPreviewState) {
    Box(modifier = Modifier.padding(CoreTheme.dimens.paddingLarge)) {
        LegalFooter(
            isPrivacyPolicyVisible = state.isPrivacyPolicyVisible,
            isTermsOfServiceVisible = state.isTermsOfServiceVisible,
            onPrivacyPolicyClick = {},
            onTermsOfServiceClick = {}
        )
    }
}

private val defaultLegalFooterPreviewState = LegalFooterPreviewState(
    isPrivacyPolicyVisible = true,
    isTermsOfServiceVisible = true
)

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun LegalFooterStatesPreview(
    @PreviewParameter(LegalFooterPreviewProvider::class) state: LegalFooterPreviewState
) {
    ComponentPreviewContainer {
        LegalFooterPreviewContent(state = state)
    }
}

@InternalApi
@PreviewComponentSizes
@Composable
private fun LegalFooterComponentSizePreview() {
    ComponentPreviewContainer {
        LegalFooterPreviewContent(state = defaultLegalFooterPreviewState)
    }
}

@InternalApi
@PreviewThemes
@Composable
private fun LegalFooterThemePreview() {
    ComponentPreviewContainer {
        LegalFooterPreviewContent(state = defaultLegalFooterPreviewState)
    }
}

@InternalApi
@PreviewFontScales
@Composable
private fun LegalFooterFontScalePreview() {
    ComponentPreviewContainer {
        LegalFooterPreviewContent(state = defaultLegalFooterPreviewState)
    }
}
