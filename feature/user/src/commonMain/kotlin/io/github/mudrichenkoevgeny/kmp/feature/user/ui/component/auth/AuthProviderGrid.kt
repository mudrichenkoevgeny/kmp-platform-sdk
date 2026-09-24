package io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ComponentSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
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
fun AuthProviderGrid(
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

/**
 * [PreviewParameterProvider] supplying sample lists of [UserAuthProvider] for [AuthProviderGrid] previews.
 */
class AuthGridPreviewProvider : PreviewParameterProvider<List<UserAuthProvider>> {
    /** Varying list sizes and provider mixes for layout previews. */
    override val values: Sequence<List<UserAuthProvider>> = sequenceOf(
        listOf(UserAuthProvider.GOOGLE, UserAuthProvider.APPLE),
        listOf(
            UserAuthProvider.EMAIL,
            UserAuthProvider.PHONE,
            UserAuthProvider.GOOGLE,
            UserAuthProvider.APPLE
        ),
        List(6) { if (it % 2 == 0) UserAuthProvider.GOOGLE else UserAuthProvider.APPLE }
    )
}

@InternalApi
@Composable
private fun AuthProviderGridPreviewContent(providers: List<UserAuthProvider>) {
    CoreTheme {
        Surface {
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
    AuthProviderGridPreviewContent(providers = providers)
}

@InternalApi
@ComponentSizePreviews
@Composable
private fun AuthProviderGridComponentSizePreview() {
    AuthProviderGridPreviewContent(providers = defaultAuthProviderGridPreviewState)
}

@InternalApi
@ThemePreviews
@Composable
private fun AuthProviderGridThemePreview() {
    AuthProviderGridPreviewContent(providers = defaultAuthProviderGridPreviewState)
}