package io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ComponentPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewComponentSizes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewFontScales
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewThemes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider

/**
 * Wraps [authProviders] in a flow layout of compact icon buttons ([AuthProviderItem]).
 *
 * @param authProviders providers to show as secondary-style tiles (e.g. OAuth).
 * @param onProviderClick invoked with the tapped [UserAuthProvider].
 * @param modifier optional modifier for the container.
 */
@Composable
public fun AuthProviderGrid(
    authProviders: List<UserAuthProvider>,
    onProviderClick: (UserAuthProvider) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingSmall, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingSmall),
        maxItemsInEachRow = Int.MAX_VALUE
    ) {
        authProviders.forEach { provider ->
            AuthProviderItem(
                authProvider = provider,
                onClick = { onProviderClick(provider) }
            )
        }
    }
}

@InternalApi
@Composable
private fun AuthProviderGridPreviewContent(providers: List<UserAuthProvider>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(CoreTheme.dimens.paddingLarge),
        contentAlignment = Alignment.Center
    ) {
        AuthProviderGrid(
            authProviders = providers,
            onProviderClick = {}
        )
    }
}

private val defaultAuthProviderGridPreviewState = listOf(
    UserAuthProvider.EMAIL,
    UserAuthProvider.PHONE,
    UserAuthProvider.GOOGLE,
    UserAuthProvider.APPLE
)

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun AuthProviderGridStatesPreview(
    @PreviewParameter(AuthGridPreviewProvider::class) providers: List<UserAuthProvider>
) {
    ComponentPreviewContainer {
        AuthProviderGridPreviewContent(providers = providers)
    }
}

@InternalApi
@PreviewComponentSizes
@Composable
private fun AuthProviderGridComponentSizePreview() {
    ComponentPreviewContainer {
        AuthProviderGridPreviewContent(providers = defaultAuthProviderGridPreviewState)
    }
}

@InternalApi
@PreviewThemes
@Composable
private fun AuthProviderGridThemePreview() {
    ComponentPreviewContainer {
        AuthProviderGridPreviewContent(providers = defaultAuthProviderGridPreviewState)
    }
}

@InternalApi
@PreviewFontScales
@Composable
private fun AuthProviderGridFontScalePreview() {
    ComponentPreviewContainer {
        AuthProviderGridPreviewContent(providers = defaultAuthProviderGridPreviewState)
    }
}
