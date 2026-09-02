package io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.identifier.item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.Dimens
import io.github.mudrichenkoevgeny.kmp.feature.user.Res
import io.github.mudrichenkoevgeny.kmp.feature.user.identifier_delete
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.IdentifierListTestTags
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
import org.jetbrains.compose.resources.stringResource

/**
 * A list item representing a single user identifier (e.g., email or phone number).
 *
 * @param identifier The [UserIdentifier] data to display.
 * @param onDeleteClick Callback invoked when the delete icon is clicked.
 * @param enabled Whether the delete action and UI interactions are permitted.
 */
@Composable
fun IdentifierItem(
    identifier: UserIdentifier,
    onDeleteClick: () -> Unit,
    enabled: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(IdentifierListTestTags.IDENTIFIER_ITEM_PREFIX + identifier.id.value),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.elevationHeader)
    ) {
        Row(
            modifier = Modifier.padding(Dimens.paddingMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = identifier.identifier,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = identifier.userAuthProvider.name,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            IconButton(
                onClick = onDeleteClick,
                enabled = enabled,
                modifier = Modifier.testTag(IdentifierListTestTags.DELETE_BUTTON_PREFIX + identifier.id.value)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(Res.string.identifier_delete),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}