package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.login.welcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.Dimens
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.login.LoginDestination
import kmp_platform_sdk.feature.user.generated.resources.Res
import kmp_platform_sdk.feature.user.generated.resources.email
import kmp_platform_sdk.feature.user.generated.resources.login
import kmp_platform_sdk.feature.user.generated.resources.login_with
import kmp_platform_sdk.feature.user.generated.resources.phone_number
import kmp_platform_sdk.feature.user.generated.resources.privacy_policy
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginWelcomeScreen(
    component: LoginWelcomeComponent
) {
    LoginWelcomeContent(
        onMethodClick = { },
        onExternalLoginClick = { },
        onPrivacyPolicyClick = component::onPrivacyPolicyClick
    )
}

@Composable
fun LoginWelcomeContent(
    onMethodClick: (LoginDestination) -> Unit,
    onExternalLoginClick: (String) -> Unit,
    onPrivacyPolicyClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(Dimens.paddingLarge),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.login),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(Modifier.height(Dimens.paddingMedium))

        Text(
            text = stringResource(Res.string.login_with),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(Dimens.paddingMedium))

//        Button(
//            onClick = { onMethodClick(LoginDestination.EmailLogin) },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text(stringResource(Res.string.email))
//        }
//
//        Button(
//            onClick = { onMethodClick(LoginDestination.PhoneLogin) },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text(stringResource(Res.string.phone_number))
//        }

        Spacer(Modifier.height(Dimens.paddingMedium))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Dimens.paddingSmall, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(Dimens.paddingSmall)
        ) {
            val externalMethods = listOf("G", "A")
            externalMethods.forEach { logo ->
                Surface(
                    onClick = { onExternalLoginClick(logo) },
                    shape = RoundedCornerShape(Dimens.roundedCornerShape),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.size(Dimens.iconButtonSize)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(logo, style = MaterialTheme.typography.titleLarge)
                    }
                }
            }
        }

        Spacer(Modifier.height(Dimens.paddingLarge))

        val privacyText = buildAnnotatedString {
            withLink(
                LinkAnnotation.Url(
                    url = "https://example.com/privacy",
                    styles = TextLinkStyles(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline
                        )
                    ),
                    linkInteractionListener = { onPrivacyPolicyClick() }
                )
            ) {
                append(stringResource(Res.string.privacy_policy))
            }
        }

        Text(text = privacyText)
    }
}

@Preview
@Composable
fun LoginWelcomeContentPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = RoundedCornerShape(Dimens.roundedCornerShape),
                tonalElevation = Dimens.paddingSmall,
                modifier = Modifier.padding(Dimens.paddingLarge)
            ) {
                LoginWelcomeContent(
                    onMethodClick = {},
                    onExternalLoginClick = {},
                    onPrivacyPolicyClick = {}
                )
            }
        }
    }
}