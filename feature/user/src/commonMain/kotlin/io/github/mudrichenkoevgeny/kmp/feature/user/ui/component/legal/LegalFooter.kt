package io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.legal

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.Dimens
import io.github.mudrichenkoevgeny.kmp.feature.user.Res
import io.github.mudrichenkoevgeny.kmp.feature.user.and
import io.github.mudrichenkoevgeny.kmp.feature.user.legal_agreement_prefix
import io.github.mudrichenkoevgeny.kmp.feature.user.privacy_policy
import io.github.mudrichenkoevgeny.kmp.feature.user.terms_of_service
import org.jetbrains.compose.resources.stringResource

@Composable
fun LegalFooter(
    isPrivacyPolicyVisible: Boolean,
    isTermsOfServiceVisible: Boolean,
    onPrivacyPolicyClick: () -> Unit,
    onTermsOfServiceClick: () -> Unit
) {
    if (!isPrivacyPolicyVisible && !isTermsOfServiceVisible) return

    val privacyText = stringResource(Res.string.privacy_policy)
    val termsText = stringResource(Res.string.terms_of_service)
    val andText = " ${stringResource(Res.string.and)} "
    val prefixText = stringResource(Res.string.legal_agreement_prefix)

    val annotatedString = buildAnnotatedString {
        withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) {
            append("$prefixText ")
        }

        when {
            isPrivacyPolicyVisible && isTermsOfServiceVisible -> {
                AppendLink(privacyText, onPrivacyPolicyClick)
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) {
                    append(andText)
                }
                AppendLink(termsText, onTermsOfServiceClick)
            }
            isPrivacyPolicyVisible -> {
                AppendLink(privacyText, onPrivacyPolicyClick)
            }
            isTermsOfServiceVisible -> {
                AppendLink(termsText, onTermsOfServiceClick)
            }
        }
    }

    Text(
        text = annotatedString,
        style = MaterialTheme.typography.bodySmall,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = Dimens.paddingMedium)
    )
}

@Composable
private fun AnnotatedString.Builder.AppendLink(text: String, onClick: () -> Unit) {
    withLink(
        LinkAnnotation.Clickable(
            tag = text,
            styles = TextLinkStyles(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline
                )
            ),
            linkInteractionListener = { onClick() }
        )
    ) {
        append(text)
    }
}