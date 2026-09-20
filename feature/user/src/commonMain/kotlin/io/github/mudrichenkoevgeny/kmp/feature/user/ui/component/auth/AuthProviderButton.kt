package io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ComponentSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.user.Res
import io.github.mudrichenkoevgeny.kmp.feature.user.*
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * Full-width button for a single [UserAuthProvider] (icon + localized label), styled with [CoreButton].
 *
 * @param authProvider which sign-in method to display.
 * @param onClick invoked when the user taps the button.
 * @param modifier optional modifier for the button.
 * @param enabled controls the enabled state of the button.
 */
@Composable
fun AuthProviderButton(
    authProvider: UserAuthProvider,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    CoreButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled
    ) {
        val iconRes = when (authProvider) {
            UserAuthProvider.EMAIL -> Res.drawable.auth_logo_email
            UserAuthProvider.PHONE -> Res.drawable.auth_logo_phone
            UserAuthProvider.GOOGLE -> Res.drawable.auth_logo_google
            UserAuthProvider.APPLE -> Res.drawable.auth_logo_apple
        }

        val tint = if (authProvider == UserAuthProvider.GOOGLE) {
            Color.Unspecified
        } else {
            LocalContentColor.current
        }

        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(CoreTheme.dimens.actionButtonIconSize)
        )

        Spacer(modifier = Modifier.width(CoreTheme.dimens.paddingSmall))

        val authProviderText = when (authProvider) {
            UserAuthProvider.EMAIL -> stringResource(Res.string.sign_in_with_email)
            UserAuthProvider.PHONE -> stringResource(Res.string.sign_in_with_phone)
            UserAuthProvider.GOOGLE -> stringResource(Res.string.sign_in_with_google)
            UserAuthProvider.APPLE -> stringResource(Res.string.sign_in_with_apple)
        }

        Text(
            text = authProviderText,
            maxLines = 1
        )
    }
}

@InternalApi
@Composable
private fun AuthProviderButtonPreviewContent(provider: UserAuthProvider) {
    CoreTheme {
        Surface {
            Box(
                modifier = Modifier
                    .padding(CoreTheme.dimens.paddingLarge)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                AuthProviderButton(
                    authProvider = provider,
                    onClick = {}
                )
            }
        }
    }
}

private val defaultAuthProviderButtonPreviewState = UserAuthProvider.EMAIL

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun AuthProviderButtonStatesPreview(
    @PreviewParameter(AuthProviderPreviewProvider::class) provider: UserAuthProvider
) {
    AuthProviderButtonPreviewContent(provider = provider)
}

@InternalApi
@ComponentSizePreviews
@Composable
private fun AuthProviderButtonComponentSizePreview() {
    AuthProviderButtonPreviewContent(provider = defaultAuthProviderButtonPreviewState)
}

@InternalApi
@ThemePreviews
@Composable
private fun AuthProviderButtonThemePreview() {
    AuthProviderButtonPreviewContent(provider = defaultAuthProviderButtonPreviewState)
}

@InternalApi
@FontScalePreviews
@Composable
private fun AuthProviderButtonFontScalePreview() {
    AuthProviderButtonPreviewContent(provider = defaultAuthProviderButtonPreviewState)
}
