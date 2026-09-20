package io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ComponentSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.user.Res
import io.github.mudrichenkoevgeny.kmp.feature.user.*
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import org.jetbrains.compose.resources.painterResource

/**
 * Compact square tile showing only the provider icon (for grids and secondary auth options).
 *
 * @param authProvider which provider icon to show.
 * @param onClick invoked when the user taps the tile.
 */
@Composable
fun AuthProviderItem(
    authProvider: UserAuthProvider,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(CoreTheme.dimens.roundedCornerShape),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = Modifier.size(CoreTheme.dimens.iconButtonSize)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(CoreTheme.dimens.paddingSmall)
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
                MaterialTheme.colorScheme.onSurface
            }

            val iconModifier = if (authProvider == UserAuthProvider.APPLE) {
                Modifier.fillMaxSize(0.65f).offset(y = (-1).dp)
            } else {
                Modifier.fillMaxSize(0.6f)
            }

            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = tint,
                modifier = iconModifier
            )
        }
    }
}

@InternalApi
@Composable
private fun AuthProviderItemPreviewContent(provider: UserAuthProvider) {
    CoreTheme {
        Surface {
            Box(
                modifier = Modifier.padding(CoreTheme.dimens.paddingLarge),
                contentAlignment = Alignment.Center
            ) {
                AuthProviderItem(
                    authProvider = provider,
                    onClick = {}
                )
            }
        }
    }
}

private val defaultAuthProviderItemPreviewState = UserAuthProvider.GOOGLE

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun AuthProviderItemStatesPreview(
    @PreviewParameter(AuthProviderPreviewProvider::class) provider: UserAuthProvider
) {
    AuthProviderItemPreviewContent(provider = provider)
}

@InternalApi
@ComponentSizePreviews
@Composable
private fun AuthProviderItemComponentSizePreview() {
    AuthProviderItemPreviewContent(provider = defaultAuthProviderItemPreviewState)
}

@InternalApi
@ThemePreviews
@Composable
private fun AuthProviderItemThemePreview() {
    AuthProviderItemPreviewContent(provider = defaultAuthProviderItemPreviewState)
}