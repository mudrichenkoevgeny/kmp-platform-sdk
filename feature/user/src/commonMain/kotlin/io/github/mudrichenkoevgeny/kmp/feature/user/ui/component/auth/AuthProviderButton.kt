package io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.Dimens
import io.github.mudrichenkoevgeny.kmp.feature.user.Res
import io.github.mudrichenkoevgeny.kmp.feature.user.*
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserAuthProvider
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun AuthProviderButton(
    authProvider: UserAuthProvider,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.actionButtonHeight),
        shape = RoundedCornerShape(Dimens.roundedCornerShape),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        tonalElevation = Dimens.shadowElevation
    ) {
        Row(
            modifier = Modifier.padding(horizontal = Dimens.paddingMedium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
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

            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(Dimens.actionButtonIconSize)
            )

            Spacer(modifier = Modifier.width(Dimens.paddingSmall))

            val authProviderText = when (authProvider) {
                UserAuthProvider.EMAIL -> stringResource(Res.string.sign_in_with_email)
                UserAuthProvider.PHONE -> stringResource(Res.string.sign_in_with_phone)
                UserAuthProvider.GOOGLE -> stringResource(Res.string.sign_in_with_google)
                UserAuthProvider.APPLE -> stringResource(Res.string.sign_in_with_apple)
            }
            Text(
                text = authProviderText,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthProviderButtonPreview(
    @PreviewParameter(AuthProviderPreviewProvider::class) provider: UserAuthProvider
) {
    MaterialTheme {
        Box(
            modifier = Modifier
                .padding(Dimens.paddingLarge)
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