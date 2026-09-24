package io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.identifier.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ComponentSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.identifier.userIdentifierMock
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.list.IdentifierListTestTags
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier

/**
 * A list item representing a single user identifier (e.g., email or phone number).
 *
 * @param identifier The [UserIdentifier] data to display.
 * @param onClick Callback invoked when the item card is tapped.
 * @param isCurrentIdentifier Indicates if this identifier is the active session's identifier.
 * @param modifier Optional [Modifier] for layout adjustments.
 */
@Composable
fun IdentifierItem(
    identifier: UserIdentifier,
    onClick: () -> Unit,
    isCurrentIdentifier: Boolean = false,
    modifier: Modifier = Modifier
) {
    val cardColors = if (isCurrentIdentifier) {
        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    } else {
        CardDefaults.cardColors()
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag(IdentifierListTestTags.IDENTIFIER_ITEM_PREFIX + identifier.id.value),
        elevation = CardDefaults.cardElevation(defaultElevation = CoreTheme.dimens.elevationHeader),
        colors = cardColors
    ) {
        Row(
            modifier = Modifier.padding(CoreTheme.dimens.paddingMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = identifier.displayName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = identifier.userAuthProvider.name,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

private data class IdentifierItemPreviewState(
    val identifier: UserIdentifier,
    val isCurrentIdentifier: Boolean
)

@InternalApi
private class IdentifierItemPreviewProvider : PreviewParameterProvider<IdentifierItemPreviewState> {
    private val items = listOf(
        IdentifierItemPreviewState(
            identifier = userIdentifierMock(),
            isCurrentIdentifier = true
        ),
        IdentifierItemPreviewState(
            identifier = userIdentifierMock().copy(
                userAuthProvider = UserAuthProvider.PHONE,
                identifier = "+1234567890"
            ),
            isCurrentIdentifier = false
        )
    )

    override val values: Sequence<IdentifierItemPreviewState> = items.asSequence()
}

@InternalApi
@Composable
private fun IdentifierItemPreviewContent(state: IdentifierItemPreviewState) {
    CoreTheme {
        Surface {
            Box(modifier = Modifier.padding(CoreTheme.dimens.paddingLarge)) {
                IdentifierItem(
                    identifier = state.identifier,
                    onClick = {},
                    isCurrentIdentifier = state.isCurrentIdentifier
                )
            }
        }
    }
}

@InternalApi
private val defaultIdentifierItemPreviewState = IdentifierItemPreviewState(
    identifier = userIdentifierMock(),
    isCurrentIdentifier = false
)

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun IdentifierItemStatesPreview(
    @PreviewParameter(IdentifierItemPreviewProvider::class) state: IdentifierItemPreviewState
) {
    IdentifierItemPreviewContent(state = state)
}

@InternalApi
@ComponentSizePreviews
@Composable
private fun IdentifierItemComponentSizePreview() {
    IdentifierItemPreviewContent(state = defaultIdentifierItemPreviewState)
}

@InternalApi
@ThemePreviews
@Composable
private fun IdentifierItemThemePreview() {
    IdentifierItemPreviewContent(state = defaultIdentifierItemPreviewState)
}

@InternalApi
@FontScalePreviews
@Composable
private fun IdentifierItemFontScalePreview() {
    IdentifierItemPreviewContent(state = defaultIdentifierItemPreviewState)
}

